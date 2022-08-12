import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

//https://codereview.stackexchange.com/questions/202916/simple-java-download-manager?rq=1
public class loader {

    //   job. url.
    public static class JOB implements Callable<JOB> {
        public boolean closethis = false;
        URL url;
        byte[] baos;
        long baosWriteTo = 0;
        long baosWriteFor = 0;
        byte[] POSTbuf = null;

        String sURL;
        //        URL url = new URL(sURL);
        String filename = null; // auto
        int fileSize;
        int maxSections;
        int[] cSections = new int[maxSections];
        HttpsURLConnection con;
        Function whenbufover = null;
        public int status = -10;
        public long partialSetupB = -1;
        public long partialSetupE = -1;
        public int responseCode = -1;
        public long contentLengthLong = -1;
        public RandomAccessFile out = null;
        private boolean rangeset = false;
        private boolean closeOUTonEND = false;
        public long writesBytesCount = 0;
        public long writesBytesIn = 0;

        {
            cSections = new int[1000];
        }

        JOB setURL(String u) {
            URL uu = null;
            try { uu = new URL(u); } catch (MalformedURLException e) { e.printStackTrace(); }
            return setURL(uu);
        }

        JOB setURL(URL u) {
            reset();
            this.sURL = u.toString();
            this.url = u;
            try {
                con = (HttpsURLConnection) this.url.openConnection();
//                con.setRequestProperty("User-Agent", USER_AGsENT); // Setting the user agent
                con.setConnectTimeout(3000);
                con.setReadTimeout(3000);
                con.setUseCaches(false);
                con.setRequestProperty("dnt", "1");
//                con.setRequestProperty("accept-encoding", "gzip");
//                UNKNOWN navigate
//                accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
//                accept-encoding: gzip, deflate, br

//                RESPONSE pic
//                accept-ranges: bytes
//                cache-control: private, max-age=520997
//                content-disposition: inline; filename*=UTF-8''1659129909_FB_IMG_1659130143237.jpg;
//                content-length: 69951
//                content-type: image/jpeg

//                RESPONSE vid
//                content-type: video/mp4

//                request WHEN VIDEO
//                accept: */*
//                accept-encoding: identity;q=1, *;q=0
//                range: bytes=0-



//                unchanged every request
//                accept-language: en-US,en;q=0.9
//                cache-control: no-cache
//                dnt: 1
//                pragma: no-cache


            } catch (IOException e) {
                e.printStackTrace();
            }
            return this;
        }

        private void reset() {

        }

        @Override
        public String toString() {
            return "JOB{" +
                    "partialSetupB=" + partialSetupB +
                    ", partialSetupE=" + partialSetupE +
                    '}';
        }

        public JOB setupRange (Long posinfile, Long rb, Long re) {
            rangeset = true;
            if(rb!=null && rb>=0) {
                String re_ = re==null || re<=rb ? "": re.toString();
                con.setRequestProperty("Range", "bytes=" + rb + "-" + re_);
                baosWriteTo = posinfile;
                partialSetupB = Math.toIntExact(rb);
                partialSetupE = re_.length()==0?Long.MAX_VALUE: Math.toIntExact(re); // корректировать при получении ответа
            }
            System.out.println("p:"+posinfile + ":" + rb + "-" + re);
            return this;
        }
        public JOB setup (byte[] buf, Function<JOB, Object> whenbufover) {
            this.baos = buf;
            this.whenbufover = whenbufover;
            return this;}
        public JOB METHOD (String meth) {
            try {
                con.setRequestMethod(meth); } catch (ProtocolException e) { e.printStackTrace(); }
            return this;}

        public JOB setupPOST (byte[] buf) {
            if (con.getRequestMethod().equals("GET"))
                METHOD("POST");
            con.setDoOutput(true);
            POSTbuf = buf;
            return this;}

        public JOB run () {
            try {

                if (POSTbuf != null) {
//                    OutputStreamWriter writer = new OutputStreamWriter(httpUrlConnection.getOutputStream());
//                    writer.append(content);
//                    writer.flush();
//                    writer.close();
                    final OutputStream O = con.getOutputStream();// before connect. connecting!
                    O.write(POSTbuf);
                    O.flush();
                    O.close();
                }

                con.connect();

                contentLengthLong = con.getContentLengthLong();

//                System.out.println(toy.headersToString(con.getHeaderFields()));

                responseCode = con.getResponseCode();
                // 1xx: Informational  2xx: Success 3xx: Redirection 4xx: Client Error 5xx: Server Error
                InputStream inputStream = null;

                try {



//                    HTTP/1.1 206 Partial Content
//                    Content-Range: bytes 0-1023/146515
//                    Content-Length: 1024

//                int baosSize = -1;
//                if (contentLengthLong <= Integer.MAX_VALUE)
//                    baosSize = (int) contentLengthLong;
//                if(baosSize<0)
//                    baosSize = MAX_BUFFER_SIZE;
//                baos = new byte[baosSize];
//
//                if (baosSize >= 0 && baosSize < 3 * 1024 * 1024) {
//                    baos = new byte[baosSize];
                    if(!rangeset) {
                        baosWriteTo = 0; // range start !
                    }
                    baosWriteFor = Long.MAX_VALUE; // range end !

                    status = -2;
                    whenbufover.apply(this);   // start read
                    if(closethis)
                        return this;

                    int pos = 0, off = 0;

                    //0-111
                    //10-111
                    //10-50,51-111

                    //123-178,199-553,777-
                    //156-178,
                    //178-178
                    int baosSize = baos.length;
                    int toWrite = 0;
                    writesBytesCount = 0; // for logs
                    writesBytesIn = baosWriteTo; // for logs
                    boolean valid = (responseCode >= 200 && responseCode < 400);
                    inputStream = valid ? con.getInputStream():con.getErrorStream();
                    if(baosSize>0)
                        do {
                            if(baosSize+baosWriteTo>baosWriteFor)
                                baosSize = (int) (baosWriteFor-baosWriteTo) + 1;

                            off = inputStream.readNBytes(baos, pos, baosSize);

//                        off = inputStream.read(baos, pos, baosSize);
//                            System.out.println("reads: "+off);
                            if(off==0) break; // -1 - end stream, 0 - baos.length == 0
                            status = off;
                            whenbufover.apply(this);
                            baosWriteTo += off;
                            writesBytesCount+=off;

                            if(off<0) break; // -1 - end stream, 0 - baos.length == 0
                        }
                        while (true);

//                    if(off<=0)
//                        throw new Exception("XXX");
                    // mini ne
//                    status = 0;
//                    whenbufover.apply(this); // end read

                } catch (Exception ee) {
                    if(inputStream!=null) {
                        inputStream.close();
                        inputStream = null;
                    }
                    System.out.println("CATCHED catch");
                    ee.printStackTrace();
                    status = -3;
                    whenbufover.apply(this);
                }
                finally {
                    if(inputStream!=null) {
                        inputStream.close();
                        inputStream = null;
                    }
//                    System.out.println("finally finallyfinallyfinallyfinally");
                    // end read
                    status = 0;
//                  set close OUT if need
                    whenbufover.apply(this);
                }

            } catch (Exception e) {


            }

            return this;
        }


        @Override
        public JOB call() throws Exception {
//            System.out.println(status);
//            Thread.sleep(500);
//            throw new Exception("yyy");
            run();
            return this;
        }

        public JOB saveToFile(String filename) {

            Path p = Paths.get(filename);
//            Path p = Paths.get("./");
//            p = p.resolve("FILENAME.jpg");

            try {

//            if(FILESIZE>0) {
//                if(FILESIZE>100*1024*1024)
//                    FILESIZE = 100 * 1024 * 1024;
//                out.setLength(FILESIZE);
//            }
//                RandomAccessFile finalOut = out;
                this.setup(new byte[10000], job -> {
                    if(job.status==-2) {
//                        if(rangeset)
//                            baosWriteTo =
                        // begin
                    } else if(job.status==-3) {
                        // exception
                    } else if(job.status==0) {
                        if(closeOUTonEND && out!=null) {
                            try {
                                out.close();
                                out = null;
                                System.out.println("CLOSED");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        // end
                    } if(job.status>0) {
                        // buffer ended. need save to file or another

                        synchronized (this) {
                            if(out==null) {
                                try {

                                    out = new RandomAccessFile(p.toFile(), "rw");
                                    closeOUTonEND = true;
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        synchronized (out) {
                            try {
                                out.seek(job.baosWriteTo);
                                out.write(job.baos, 0, job.status);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    return null;
                });//.run();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            return this;
        }
    }

    public static class FSection {
        public int MAX_BUFFER_SIZE = 3*1024*1024;

        String sRange = "";
        int rBegin = 0;
        int rEnd = 0;
        HttpsURLConnection con = null;

        Function work = (o) -> o;
        private Map<String, List<String>> headerFieldsReq;
        private Map<String, List<String>> headerFieldsRes;
        private byte[] baos;// = new byte[MAX_BUFFER_SIZE];


        // when stack is overflow. do some like save data. and go next loop
        // CompletableFuture.AsynchronousCompletionTask
    }

    public static class CookieClass {
        private final String CookieFile;
        private final CookieManager cookieManager_old;
        private final CookieManager cookieManager;
        public CookieClass(String fn) {
            CookieFile = fn;
            //        CookieManager cookieManager = new CookieManager();
//        CookieStore cs =
            this.cookieManager_old = (CookieManager) CookieHandler.getDefault();
            this.cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(this.cookieManager);
        }
        public int CookieFromFile() {
            ArrayList<String> cc = new ArrayList<>(50);
            final CookieStore ces = this.cookieManager.getCookieStore();
            int ret = -1;
            if(FileReadToList(CookieFile, cc)==1){
                ret = 0;
                cc.forEach(s -> {
                    try {
                        final String[] st = s.split(";");
                        final int L = st.length;
                        HttpCookie h = new HttpCookie(st[1].trim(), st[2].trim());
                        String cC;
                        long ma = 0;
                        int iI = 3;
                        if(L>iI && (cC = st[iI++].trim()).length()>0) // -1 или 0 это длина <= 2. tckb
                            if((ma = Long.parseLong( cC ))>0)
                                h.setMaxAge(ma);
                        if(L>iI && (cC = st[iI++].trim()).length()>0)
                            h.setPath(cC);
                        if(L>iI && (cC = st[iI++].trim()).length()>0)
                            h.setDomain(cC);
                        ces.add(URI.create(st[0].trim()), h );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            return ret;
        }
        public int CookieToFile() {
//        ArrayList<String> cc = new ArrayList<>(50);
            int ret = -1;
            try {
                final CookieStore ces = this.cookieManager.getCookieStore();

                StringBuilder sb = new StringBuilder("");
                ces.getURIs().forEach(U -> {
                    ces.get(U).forEach(C -> {
                        if(C.getMaxAge()<=0) return;
                        sb.append(U);

                        sb.append("; ");//.append(C.toString());

                        sb.append(C.getName()).append("; ").append(C.getValue());
                        //                sb.append(C.getName()).append("=\"").append(C.getValue()).append('"');
                        sb.append("; ").append(C.getMaxAge());
                        sb.append("; ").append(C.getPath());
                        //                if (getPortlist() != null)
                        sb.append("; ").append(C.getDomain());

                        sb.append("\n");
                    });

                });
                StringToFile0(sb.toString(), CookieFile, false);
//                cookieManager.getCookieStore().getCookies().forEach(c -> {
//                    System.out.println(c);
//                });
                ret = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner(", ", CookieClass.class.getSimpleName() + "[", "]");
            cookieManager.getCookieStore().getCookies().forEach(httpCookie -> sj.add(httpCookie.toString()));
//                    .add("CookieFile='" + CookieFile + "'").add("cookieManager_old=" + cookieManager_old).add("cookieManager=" + cookieManager)
            return sj.toString();
        }
    }

    public static void StringToFile0(String data, String filename, boolean append) {
        Path path = Paths.get(filename);
        //Use try-with-resource to get auto-closeable writer instance
        try {
            if (append) {
                OpenOption xxxo = StandardOpenOption.APPEND;
                try (BufferedWriter writer = Files.newBufferedWriter(path, xxxo)) {
                    writer.write(data.toString());
                }
            } else {
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(data.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int FileReadToList(String path, ArrayList<String> r) {
        Path fM3U = Paths.get(path).toAbsolutePath();
        if (!Files.exists(fM3U)) return 0;
        File file = new File(path);
//        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "Cp1251"));

        try ( BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8" ))) { // открывает файл в кодировке а хранит всёравно в utf-8
            String line = "";
            while (true) {
                if ((line = in.readLine()) == null) break;
                r.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public static class RemoteDataClass {
        private static final CookieClass cclass = new CookieClass("cookiesx.txt");

        public String[] USERRAGENT = new String[]{"User-Agent",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36"
        };
        //user-agent:
        private String FILE_URL;
        public String FILENAME = null;
        private URL _URL = null;
        public String DIR_SAVE = "./";
        public Boolean allocateSpace = true;
        public int maxtimeoutcon = 3000;
        public int maxtimeotread = 3000;
        public int maxerrors = 10;
        public int maxfiles = 3;
        public int maxsection = 5;
        public int minsectionsize = 512*1024;
        public int MAX_BUFFER_SIZE = 3*1024*1024;
        public long FILESIZE = 0;

        //        HttpsURLConnection httpClient = null;
        public byte[] baos = new byte[0];
        private Object POSTDATA = null;
        public Map<String, List<String>> headerFieldsRes = null;
        public Map<String, List<String>> headerFieldsReq = null;

        public HashMap<Integer, JOB> joblist = new HashMap<>();
        Callable progress = null;
        //        OutputStream out = null;
        RandomAccessFile out = null;

        ExecutorService exeserv = Executors.newFixedThreadPool(10);
//            ((ThreadPoolExecutor) service).setCorePoolSize();

        public URL get_URL() {
            try {
                if(_URL==null)
                    _URL = new URL(FILE_URL);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return _URL;
        }
//  Status Code: 304    https://i.imgur.com/z4d4kWk.jpg


        public void open(File outputFile) throws IOException {
            outputFile.mkdirs();
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
            RandomAccessFile outputRAF = new RandomAccessFile(outputFile, "rw");
            long mappedFileSizeBytes = 0;
            outputRAF.setLength(mappedFileSizeBytes);
            outputRAF.seek(mappedFileSizeBytes - 1);
            outputRAF.writeByte(0);
            outputRAF.seek(0);
            FileChannel outputChannel = outputRAF.getChannel();
            MappedByteBuffer fileBuffer = outputChannel.map(FileChannel.MapMode.READ_WRITE, 0, mappedFileSizeBytes);
//            fileBuffer.
        }

//        @Private static RandomAccessFile initializeFromFile(File file, boolean forceLegacy)
//                throws IOException {
//            if (!file.exists()) {
//                // Use a temp file so we don't leave a partially-initialized file.
//                File tempFile = new File(file.getPath() + ".tmp");
//                RandomAccessFile raf = open(tempFile);
//                try {
//                    raf.setLength(INITIAL_LENGTH);
//                    raf.seek(0);
//                    if (forceLegacy) {
//                        raf.writeInt(INITIAL_LENGTH);
//                    } else {
//                        raf.writeInt(VERSIONED_HEADER);
//                        raf.writeLong(INITIAL_LENGTH);
//                    }
//                } finally {
//                    raf.close();
//                }
//                // A rename is atomic.
//                if (!tempFile.renameTo(file)) {
//                    throw new IOException("Rename failed!");
//                }
//            }
//            return open(file);
//        }

//        Delete last line in text file
//        RandomAccessFile f = new RandomAccessFile(fileName, "rw");
//        long length = f.length() - 1;
//do {
//            length -= 1;
//            f.seek(length);
//            byte b = f.readByte();
//        } while(b != 10);
//f.setLength(length+1);
//f.close();

        public void makeOUT(JOB o) {
            if (out != null)
                return;
            headerFieldsRes = o.con.getHeaderFields();
            if (FILENAME == null) {
                for (Map.Entry<String, List<String>> entry : headerFieldsRes.entrySet()) {
                    final String key = entry.getKey();
                    if(key!=null)
                        if(key.equalsIgnoreCase("ContEnt-DispOsition")){
                            final List<String> list = entry.getValue();
                            if(list!=null)
                                for (int i = 0; i < list.size(); i++) {
                                    final String s = list.get(i);
                                    if(s!=null && s.length()>0) {
                                        //    filename *= UTF - 8 ''1659129909_F B_IMG_1659130143237.jpg;
                                        if (s.toLowerCase().indexOf("filename")>=0) {
                                            int ii = s.lastIndexOf("'")+1;
                                            if(ii>0) {
                                                FILENAME = s.substring(ii,s.length()-1);
                                            }
                                        }
                                    }
                                }
                        }

                }


//                filesize
//                    FILENAME
//                content-disposition: inline; filename*=UTF-8''filename.jpg;

            }
            if (FILENAME == null || FILENAME.length() == 0) {
                FILENAME = "1234.jpg";
//                FILE_URL
            }
            Path p = Paths.get(DIR_SAVE);
            p = p.resolve(FILENAME);

            boolean e = Files.exists(p);

//            while (e = Files.exists(p)){
//            p = p.resolvesiblings(FILENAME+random);
//            }
            FILESIZE = o.con.getContentLengthLong();
            try {
                if(out!=null)
                    out.close();

                out = new RandomAccessFile(p.toFile(), "rw");
                if(FILESIZE>0) {
                    if(FILESIZE>100*1024*1024)
                        FILESIZE = 100 * 1024 * 1024;
                    out.setLength(FILESIZE);
                }
//                w.
            } catch (IOException ex) {
                ex.printStackTrace();
            }



            OpenOption[] oo = new StandardOpenOption[]{
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};
//            try {
////                out = Files.newOutputStream(p, oo);
////                out.
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        }


        public Object whensectioncomplete(JOB o) {
            // write to result buffer or file, some data
            // from some buffer, to pos, some size
            // для начала сохраняем всё в файл, потом в буфер в оперативе
            if(out==null) {
                synchronized(this) {
                    makeOUT(o);
                }
            }
            synchronized(out) {
//                o.
                if(o.status==-3) { // exception
                    System.out.println("BUG");
                }
                if(o.status==-2) {
//                    Accept-Ranges: bytes
//                    Accept-Ranges: none


                    if(o.partialSetupB!=-1) {

                        if(o.responseCode!=206 || o.con.getContentLengthLong()<0) {

                            o.closethis = true;
                        } else {
                            String hh = o.con.getHeaderField("Content-Range");
                            o.baosWriteTo = o.partialSetupB;
                            o.baosWriteFor = o.partialSetupB+o.con.getContentLengthLong();


                        }

                    }

                     {

                        String hh = o.con.getHeaderField("Accept-Ranges");

                        if (hh != null && !hh.equalsIgnoreCase("none")) {
//                            makeSectionList(o.con.getContentLengthLong());
//                            o.baosWriteFor = filemap.firstKey();
//                            trygetdata(0, 0);
//                    HTTP/1.1 206 Partial Content
//                    Content-Range: bytes 0-1023/146515
//                    Content-Length: 1024

//                            o.baosWriteTo =
//                            o.baosWriteFor =
//                        create

//                        o.baosWriteFor

//                        lets try
                        } else {
//                            askaboutsections = false;
//                            maxsection = 1;
//                            rangesyes=false;
                        }
                    }
                    // check возмоность по кускам качать
//                    а пока не надо. вроде каркас уже есть. по кускам потом прикрутим.
//                    пока по подной части можно доделать

                }
                if(o.status==0) {
                    // закончена секция
//                    тут данных нет что надо записать в файл
                }
                if(o.status>0) {
                    try {
//                        System.out.println("TRY WRITE: posinfile:"+ o.baosWriteTo+" ; count bytes:"+ o.status);
                        out.seek(o.baosWriteTo);
                        out.write(o.baos, 0, o.status);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
            return o;
        }

        public RemoteDataClass() {

        }

        public RemoteDataClass(String in, Object out) {

        }



        

//        10 03.08.2022 20:10:23 GET /ugc/1930374311228956823/BFD03EE761CF2B1B34A8BA0B7F74608231F5D3EF/ HTTP/1.1
//        User-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36
//        Accept: */*
//                       Accept-Encoding: identity
//                       Host: .net

//
//The issued request looks like this:
//
//GET /z4d4kWk.jpg HTTP/1.1
//Host: i.imgur.com
//Range: bytes=0-1023
//The server responses with the 206 Partial Content status:
//
//HTTP/1.1 206 Partial Content
//Content-Range: bytes 0-1023/146515
//Content-Length: 1024


        

        public void Resumable() {
        /*    URL url = null;
            HttpsURLConnection httpConnection =null;
            try {
                url = new URL(FILE_URL);


                httpConnection = (HttpsURLConnection) url.openConnection();

                httpConnection.setRequestMethod("HEAD");
//                httpConnection.setAuthenticator();
//                httpConnection.setRequestProperty();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                httpConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpConnection.setInstanceFollowRedirects();
            long removeFileSize = httpConnection.getContentLengthLong();

            httpConnection.disconnect();
//    Now that we have the total content size of the file, we can check whether our file is partially downloaded.

//    If so, we'll resume the download from the last byte recorded on disk:

            long existingFileSize = outputFile.length();
            if (existingFileSize < fileLength) {
                httpConnection.setRequestProperty(
                        "Range",
                        "bytes=" + existingFileSize + "-" + fileLength
                );
            }

            OutputStream os = new FileOutputStream(FILE_NAME, true);

            try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                // handle exception
            }
            */
        }

        public RemoteDataClass setFILE_URL(String FILE_URL) {
            this.FILE_URL = FILE_URL;

            //            USERRAGENT
//            try {

//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            //add request header
//            httpClient.setRequestProperty(USERRAGENT[0], USERRAGENT[1]);

            return this;
        }
        public RemoteDataClass setDIR_SAVE(String DIR_SAVE) {
            this.DIR_SAVE = DIR_SAVE;
            return this;
        }
        public RemoteDataClass setPOSTDATA(Object data) {
//            httpClient.setDoOutput(true);
//
//            this.POSTDATA = DIR_SAVE;
            return this;
        }

    }

    public final static class RuntimeHookOnExit {
        /**
         * <br>        usage<br>
         * <code>      new loader.RuntimeHookOnExit(0, ()->{
         * <br>             System.out.println("23232323");
         * <br>        });
         * </code>
         * <br>        or  <br>
         * <code>      loader.RuntimeHookOnExit.addRuntimeHookOnExit(0, ()->{
         * <br>             System.out.println("1231231");
         * </code>     });
         *
         * @param millis - 0 infinite wait
         * @param action - ()->{ code }
         */
        public RuntimeHookOnExit(long millis, Runnable action) {
            Runtime.getRuntime().addShutdownHook(
                    new Thread() {
                        class PrimeThread extends Thread {
                            long minPrime;

                            PrimeThread(long minPrime) {
                                this.minPrime = minPrime;
                            }

                            public void run() {
                                // compute primes larger than minPrime
//                                try {
//                                    Thread.sleep(1500);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                                action.run();
//                                System.out.println("eeee "+ this.minPrime);
                            }
                        }

                        public void run() {
//                            System.out.println("iiiii");
                            try {

                                PrimeThread et = new PrimeThread(System.currentTimeMillis());
                                et.start();
                                et.join(millis);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("ending");

                        }
                    }
            );
        }
        public static void addRuntimeHookOnExit(long millis, Runnable action) {
            new RuntimeHookOnExit(millis, action);
        }
    }
}
