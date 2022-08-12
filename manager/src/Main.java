import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.*;
import java.util.function.Function;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

// import static loader.*;

public class Main {
    private static long timeee = 0;

    public static void time2(String m) { // throws InterruptedException {
        //			time2(null);  time2("resized .  %n");
        long c = System.currentTimeMillis(); // nanoTime();
        if (m != null)

            //		    System.out.printf("TIME MS: %d\t: "+ m /*+"%n"*/+"\n", c - timeee);
            System.out.println("TIME MS: " + (c - timeee) + "\t: " + m);
        timeee =
                c =
                        System
                                .currentTimeMillis(); // корректировка после println... а может и не
                                                      // надо...
        // Thread.sleep(10);
    }



    /*
    //    Resumable Download
    //    Considering internet connections fail from time to time, it's useful to be able to resume a download, instead of downloading the file again from byte zero.
    //
    //    Let's rewrite the first example from earlier to add this functionality.
    //
    //    The first thing to know is that we can read the size of a file from a given URL without actually downloading it by using the HTTP HEAD method:
    //
        URL url = new URL(FILE_URL);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("HEAD");
        long removeFileSize = httpConnection.getContentLengthLong();
    //    Now that we have the total content size of the file, we can check whether our file is partially downloaded.

    //    If so, we'll resume the download from the last byte recorded on disk:

        long existingFileSize = outputFile.length();
        if (existingFileSize < fileLength) {
            httpFileConnection.setRequestProperty(
              "Range",
              "bytes=" + existingFileSize + "-" + fileLength
            );
        }
    //    Here we've configured the URLConnection to request the file bytes in a specific range. The range will start from the last
    //    downloaded byte and will end at the byte corresponding to the size of the remote file.
    //
    //    Another common way to use the Range header is for downloading a file in chunks by setting different byte ranges.
    //    For example, to download 2 KB file, we can use the range 0 – 1024 and 1024 – 2048.
    //
    //    Another subtle difference from the code in Section 2 is that the FileOutputStream is opened with the append parameter set to true:

        OutputStream os = new FileOutputStream(FILE_NAME, true);
    //    After we've made this change, the rest of the code is identical to the one from Section 2 below.


    //    When reading from an InputStream, it's recommended to wrap it in a BufferedInputStream to increase the performance.
    //
    //    The performance increase comes from buffering. When reading one byte at a time using the read() method,
    //    each method call implies a system call to the underlying file system. When the JVM invokes the read() system call,
    //    the program execution context switches from user mode to kernel mode and back.
    //
    //    This context switch is expensive from a performance perspective. When we read a large number of bytes, the application
    //    performance will be poor, due to a large number of context switches involved.
    //
    //    For writing the bytes read from the URL to our local file, we'll use the write() method from the FileOutputStream class:

        try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // handle exception
        }*
        ////////////////
        We can use the Files.copy() method to read all the bytes from an InputStream and copy them to a local file:

        InputStream in = new URL(FILE_URL).openStream();
        Files.copy(in, Paths.get(FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
        //////////////////

        To read the file from our URL, we'll create a new ReadableByteChannel from the URL stream:

        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        // The bytes read from the ReadableByteChannel will be transferred to a FileChannel corresponding to the file that will be downloaded:

        FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
        FileChannel fileChannel = fileOutputStream.getChannel();
        // We'll use the transferFrom() method from the ReadableByteChannel class to download the bytes from the given URL to our FileChannel:

        fileOutputStream.getChannel()
          .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

     */


    public static void main(String[] args) {
//        loadTables();

        //        cclass.CookieFromFile();
        //        ImageIO.setUseCache(false);
        //        https://www.tabnine.com/code/java/methods/java.io.RandomAccessFile/setLength
        //
        // https://www.programcreek.com/java-api-examples/?action=search_nlp&CodeExample=qrcode&submit=Search
        //        https://www.ibm.com/support/pages/how-pre-allocate-file-blocks-less-fragmentation
        //        jdk.internal.access.SharedSecrets.getJavaIOFileDescriptorAccess().get(descriptor);

        //        System.out.println("aaa");
        ////        String theText =
        // "https://filesamples.com/samples/video/mp4/sample_960x400_ocean_with_audio.mp4";
        //        String theText = "https://i.imgur.com/z4d4kWk.jpg";
        //
        //        DiskSpaceDetail.main(null);
        //        CompletableFuture<String> ff = new CompletableFuture<>();
        //
        //
        //        ff.defaultExecutor().execute(() -> {
        //            System.out.println(System.nanoTime());
        //
        //        }); //  sparse
        //        ff.obtrudeValue("123456");
        //        try {
        //            ff.thenApplyAsync(x -> (x+"789"))
        //                    .thenAcceptAsync(x -> System.out.print(x))
        //                    .thenRun(() -> System.out.println());
        ////
        ////            ff.thenApplyAsync(o -> {
        ////                System.out.println("111");
        ////                System.out.println(o.toString());
        ////                return null;
        ////            }).get();
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        ff.defaultExecutor().execute(() -> {
        //            System.out.println(System.nanoTime());
        //        });
        //
        //        loader.RemoteDataClass rc = new loader.RemoteDataClass();
        ////        rc.FILE_URL = "xxx";
        //        rc.progressFunc(() -> {
        //            System.out.println("NEW FILE: " + rc.FILENAME);
        //
        //            return null;
        //        });
        //
        //        rc.setFILE_URL(theText).setDIR_SAVE(".");

        //        rc.GO();
        //        rc.loop(true);
        //        rc.loop(false);
        ArrayList<loader.JOB> mj = new ArrayList<>(10);
        // mj.add()
        //        Collection<? extends Callable<T>>
        //        Collection<? extends Callable<loader.JOB>>





        //        JobbFactory sm = new JobbFactory();
        //        sm.addJOB();
        //        sm.addJOB();
        //        sm.addJOB();
        //        sm.addJOB();
        //
        //
        //        try {
        //
        //
        //            sm.sv.invokeAll(sm);
        ////            sv.invokeAll(sm);
        //
        //            System.out.println(sm.sv.isShutdown());
        //            System.out.println(sm.sv.isTerminated());
        //            System.out.println(sm.sv.isTerminated());
        //            time2();
        //
        ////            sm.sv.shutdown();
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }

        /*
                MegaFactory m = new MegaFactory();


                m.asyncProgress((jobbFactories, o) -> {
                    synchronized (jobbFactories) {
                        System.out.println(o);
                        return null;
                    }
                }, theText);

                m.push(theText[0]);
                m.push(theText[1]);
                m.push(theText[2]);
                // errors control...
                m.run();
                while(!m.isDone()) {
                    try {
                        Thread.sleep(333);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        //        m.get()
                m.sv.shutdown();*/

        //        m.forEach(callables -> {
        //            System.out.println(callables.getJOB(0).status);
        //        });

        //                public static Path write1(Path path, byte[] bytes, OpenOption... options)
        ////            throws IOException
        //                {
        //                    final int BUFFER_SIZE = 10*1024*1024;
        //                    // ensure bytes is not null before opening file
        //                    Objects.requireNonNull(bytes);
        //                    try (OutputStream out = Files.newOutputStream(path, options)) {
        //                        int len = bytes.length;
        //                        int rem = len;
        //                        while (rem > 0) {
        //                            int n = Math.min(rem, BUFFER_SIZE);
        //                            out.write(bytes, (len-rem), n);
        //                            rem -= n;
        //                        }
        //                    } catch (IOException e) {
        //                        e.printStackTrace();
        //                    }
        //                    return path;
        //                }

        //
        //            ExecutorService service = Executors.newFixedThreadPool(5);
        //            ArrayList<Callable<Integer>> al = new ArrayList<>();
        //
        //            new Thread(() -> {
        //                long max = Long.MAX_VALUE;
        //            });

        //        write1(Paths.get("unk.jpg"), rc.baos);
        ////
        //        System.out.println(RemoteDataClass.headersToString(rc.headerFieldsRes));
        //        System.out.println(rc.headersToString(rc.headerFieldsReq));
        //        System.out.println("---------------------------");
        //        rc.setFILE_URL(theText).setDIR_SAVE(".").setRANGE("0","1024").setCall();
        //        write1(Paths.get("sample_960x400_ocean_with_audio_0-1024.mp4"), rc.baos);
        //        System.out.println(rc.headersToString());
        // перед запуском потока нужно уметь анализировать всё что уже скачано предыдущим потоком. и
        // потом это всё в циклы и потоки
        //

        //        byte[] baisx = GetFromURL(theText, null, true);
        //        if(baisx.length==0) return;
        //        try {
        ////            Files.write(Paths.get("test.file"), baisx, new StandardOpenOption[]{
        ////            write1(Paths.get("test.file"), baisx, new StandardOpenOption[]{
        ////                    StandardOpenOption.CREATE, StandardOpenOption.WRITE,
        // StandardOpenOption.TRUNCATE_EXISTING});
        //
        //        } finally {
        //
        //        }
        //        new ByteArrayInputStream(baisx)
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {


                        //                new Main3().main(null);
                        //                new SetViewportPosition().main(null);
                        //                new progressForm2.ProgressBarUITest().main(null);

                        //                new progressForm2.ProgressBarUITest().main(null);
                        // ProgressBarUITest().main(null);
                        //                new Main2().main(null);
                        //                new Main().mainpro(null);
                        //                createAndShowGUI();
                        final managerForm form = new managerForm();
                        form.tablesLoad();
                        loader.RuntimeHookOnExit.addRuntimeHookOnExit(0, ()->{
                            System.out.println("saving tables");
                            form.tablesSave();
                        });
                        TreeMap<Integer, Integer> ranges2 = new TreeMap<>();
                        ranges2.clear();
                        ranges2.put(0, 0);
                        ranges2.put(750, 700); // value 0
                        ranges2.put(3000, 2950); // value 0

                        // form.setupRanges(ranges2);
                        ////                form.setSize(800,400);
                        ////                form.setLocationRelativeTo(null);
                        form.setVisible(true);
                        
//                        form.b_start.addActionListener(
//                                getActionListenerfd(form));
                        //                ranges2.replace()
                    }
                });

        String[] theText = {
            "https://filesamples.com/samples/video/mp4/sample_960x400_ocean_with_audio.mp4",
            "https://steamuserimages-a.akamaihd.net/ugc/1937129321840786682/0E4BB96523716CD774BC47327E707D180374B7D3/",
            "https://i.imgur.com/z4d4kWk.jpg"
        };

        System.out.println("xxx");
        //        loader.JOB j = new loader.JOB();
        //        j.setURL(theText[1]).saveToFile("FILENAMEss2.jpg");
        //        j.setupRange(110L,110L, null); // asked OR returned
        ////        j.setupRange(0L,0L, 1024L);
        ////        j.setupRange(110L,110L, null);
        ////        j.setupRange(110L,110L, null);
        //        j.run();


    }



    private static ActionListener getActionListenerfd(managerForm form) {
        return e -> {
            ListSelectionModel sm = form.table_current.getSelectionModel();
            //                    form.table1.column
            String theURL = null;
            if (sm != null && sm.getMinSelectionIndex() >= 0) {
                theURL = form.table_current
                        .getModel()
                        .getValueAt(sm.getMinSelectionIndex(), 4).toString();
                System.out.println( theURL );
                int xx = sm.getMinSelectionIndex();
            } else return;

              JobbFactory jf = new JobbFactory();
                jf.jobFromFile();
                jf.setURL(theURL);
                jf.saveToFile("FILENAMEss211.jpg");
                jf.addJOB();
        //        jf.setMaximumThreads(7);
        //        j = jf.addJOB();
                Function<JobbFactory, Object> afterwork = (in) -> {

                    System.out.println("DONE. saved to:" + in.getJOB(0).filename);
                    return null;
                };
                new Thread(() -> {
                    try {
                        jf.call();
                        afterwork.apply(jf);
                    } catch (Exception exx) {
                        exx.printStackTrace();
                    }

                }).start();
                System.out.println("THREAD works");



        //        j.baos


        };
    }
    private static ActionListener getActionListenerf(managerForm form) {
        return e -> {
            ListSelectionModel sm = form.table_current.getSelectionModel();
            //                    form.table_current.column
            if (sm != null && sm.getMinSelectionIndex() >= 0) {
                System.out.println(
                        form.table_current
                                .getModel()
                                .getValueAt(sm.getMinSelectionIndex(), 4));
                int xx = sm.getMinSelectionIndex();
                TreeMap<Integer, Integer> rang = new TreeMap<>();
                rang.clear();
                rang.put(0, 0);
                rang.put(750, 700); // value 0
                rang.put(1000, 950); // value 0
                rang.put(2000, 2000 - 50); // value 0
                rang.put(3000, 2950); // value 0
                // 0-145/100,146-211/146,
                int[] oz = {0};
                rang.entrySet().forEach((t) -> {
                    // rang.put(kk, vv);
                    if(t.getKey()==0)
                        return;
                    // t.setValue(oz[0]);
                    rang.put(t.getKey(), oz[0]);
                    oz[0] =t.getKey()+1;
                    // int vv =t.getValue()+1;
                    // if(vv<kk) {

                    //     oo[0]++;
                    // }
                });

                form.table_current.getModel().setValueAt(rang, xx, 1);

                new Thread(() -> {
                    while (true) {
                        int[] oo = {0};
                        rang.entrySet().forEach((t) -> {
                            int kk =t.getKey();
                            if(kk==0) return;
                            int vv =t.getValue()+1;
                            if(vv<kk) {
                                rang.put(kk, vv);
                                oo[0]++;
                            }
                        });


                        form.table_current.getModel().setValueAt(rang, xx, 1);

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        if(oo[0]==0)
                            break;
                    }

                }).start();
            }
        };
    }
    //        new Game1().setVisible(true);

    /*
    //======== panel6 ========
    {
        panel6.setLayout(new BorderLayout());

        //======== scrollPane5 ========
        {
            scrollPane5.setViewportView(tree1);
        }
        panel6.add(scrollPane5, BorderLayout.WEST);

        //======== scrollPane6 ========
        {

            //---- table1 ----
            table1.setModel(new DefaultTableModel(
                    new Object[][] {
                            {"www", "ggg", null, null, null},
                            {"dfb", "dbdf", "", null, null},
                            {null, null, null, null, null},
                            {null, null, "", null, null},
                    },
                    new String[] {
                            "fd", "b", null, null, null
                    }
            ));
            {
                TableColumnModel cm = table1.getColumnModel();
                cm.getColumn(3).setCellEditor(new DefaultCellEditor(
                        new JComboBox(new DefaultComboBoxModel(new String[] {
                                "gwg",
                                "weg",
                                "wg",
                                "weg",
                                "weg",
                                "wegw",
                                "eg",
                                "gg"
                        }))));
            }
            scrollPane6.setViewportView(table1);
        }
        panel6.add(scrollPane6, BorderLayout.CENTER);
    }
                tabbedPane1.addTab("text", panel6);

                tabbedPane1.setSelectedIndex(1);
                */
}
