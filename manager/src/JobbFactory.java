import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.*;

public class JobbFactory extends ArrayList<Callable<loader.JOB>> implements Callable<JobbFactory> {
    private int maximumThreads = 2;
    private int minimalSizeofSection = 12345000;
    private int maximalSizeofSection = 2048;
    private int sizeOfBuf = 12048;
    private int countSections = 0;
    private int countXXX = 0;
    public ExecutorService sv = Executors.newFixedThreadPool(maximumThreads);
    public ThreadPoolExecutor svx = (ThreadPoolExecutor) sv;

    public int Thread_PRIORITY = Thread.NORM_PRIORITY;
    public String URL = null;
    public String filename = null;
    public byte[] filedata = null; //
    public RandomAccessFile factoryOUT = null;
    public boolean tryAllocateFile = false;
    private long fileSIZE = -1;
    private final Object sync = new Object();

    //            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1,
    // numOfWorkerThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    {

        //                svx.getQueue().
        //                svx.setThreadFactory(new
        // OpJobThreadFactory(Thread.NORM_PRIORITY+2));

    }

    public loader.JOB getJOB(int i) {
        return (loader.JOB) this.get(i);
    }

    public loader.JOB addJOB() {
        boolean add;
        countSections++;
        countXXX++;
        loader.JOB e =
                new loader.JOB() {

                    @Override
                    public loader.JOB call() throws Exception {
                        //                        return super.call();
                        this.run();
                        // во время выполнения первой проеверяем, а неадо ли ещё потоки
                        // добавить

                        //                        сработает вызов со статусом вначале
                        //                        status = -2;
                        //                        whenbufover.apply(this);   // start read

                        //                        System.out.printf("getActiveCount: %d,
                        // getCompletedTaskCount: %d, getTaskCount: %d",
                        //                                svx.getActiveCount(),
                        // svx.getCompletedTaskCount(), svx.getTaskCount());
                        //                        , svx.getQueue().toArray().toString());
                        //                        System.out.println(countXXX +
                        // "<count:size>" + size());
                        countSections--;
                        if (countSections <= 0) sv.shutdown();
                        //                        sv.execute(() ->{
                        //                            try {
                        // sv.awaitTermination(0,TimeUnit.NANOSECONDS);
                        //                            } catch (InterruptedException ex) {
                        // ex.printStackTrace(); }
                        //                            sv.shutdown();
                        //                        });
                        return null;
                    }
                };
        add = this.add(e);
        e.setURL(this.URL);
        e.out = factoryOUT;
        e.setup(new byte[512], this::onwork);

        // if first work
        //                if resume...

        //                e.filename
        //                e.status = this.size();
        // it is JOB part jobfactory
        return e;
    }

    public long calcRange(long cur) {
        if (cur > fileSIZE) return 0;
        if (cur + minimalSizeofSection > fileSIZE) return fileSIZE;
        if (cur + minimalSizeofSection + minimalSizeofSection > fileSIZE)
            return -(fileSIZE);
        return cur + minimalSizeofSection;
    }

    private synchronized Object onwork(loader.JOB job) {
        // check if possible make thread
        if (job.status == -2) {
            if (countSections == 1 && maximumThreads > 1) { // ranges ok
                //                        this.size()<
                if (job.responseCode == 200 && job.contentLengthLong > 0) {
                    fileSIZE = job.contentLengthLong;
                    String hh = job.con.getHeaderField("Accept-Ranges");

                    if (hh != null && !hh.equalsIgnoreCase("none")) {
                        long readneed = (job.contentLengthLong - minimalSizeofSection);
                        if (readneed > 0) {
                            job.baosWriteFor =
                                    minimalSizeofSection; // первая секция остановится тут,
                            // а вторая будет создана

                            loader.JOB job1 = null;
                            long lastpos = job.baosWriteFor;
                            boolean lastloop = false;
                            for (int i = 1; i < maximumThreads; i++) {
                                lastpos++;
                                if ((readneed = calcRange(lastpos)) == 0) break;
                                if (readneed < 0) {
                                    lastloop = true;
                                    readneed = -readneed;
                                }
                                job1 = addJOB();

                                job1.setupRange(lastpos, lastpos, readneed);
                                sv.submit(job1);
                                if (lastloop) break;
                                lastpos = readneed;
                            }
                        }
                    }
                }
            }
            //                        if(rangeset)
            //                            baosWriteTo =
            // begin
        } else if (job.status == -3) {
            // exception
        } else if (job.status == 0) {
            // можно добавлять секцию по завершению одной...
            //                    анализировать текущее состояние синхронизированно.
            System.out.println(
                    "writing comlete in pos: "
                            + job.writesBytesIn
                            + ", bytes: "
                            + job.writesBytesCount);
            synchronized (sync) {
                if (this.fileSIZE > 0) {
                    loader.JOB job1 = this.getJOB(this.size() - 1);
                    if (job1.partialSetupE == -1) // означает, не то что надо...
                        return null;
                    //                          TODO: проверка на то что файл закачан...
                    //                           должна быть

                    //                           System.out.println(job1.partialSetupE);
                    long lastpos = job1.partialSetupE + 1;
                    long readneed = 0;
                    if ((readneed = calcRange(lastpos)) == 0) return null;
                    if (readneed < 0) {
                        readneed = -readneed;
                    }
                    job1 = addJOB();

                    job1.setupRange(lastpos, lastpos, readneed);
                    sv.submit(job1);
                }
            }
            // end
        }
        if (job.status > 0) {
            // buffer ended. need save to file or another

            synchronized (sync) {
                try {
                    factoryOUT.seek(
                            job.baosWriteTo); // много раз не будет лучше, а будет прыгать в
                    // разные места диска
                    factoryOUT.write(job.baos, 0, job.status);
                    //                            System.out.println("write to: " +
                    // job.baosWriteTo);
                    //                          update progress bar

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    ;

    public void jobToFile() {}

    public void jobFromFile() {}

    @Override
    public JobbFactory call() throws Exception {
        sv.invokeAll(this);

        StringBuilder sb = new StringBuilder(100);
        while (!sv.isShutdown()) {
            Thread.sleep(100);
            sb.delete(0,  sb.length()-1);
            sb.append("0,0,");
            for (int i = 0; i < this.size(); i++) {
                int cur_pos = (int) this.getJOB(i).baosWriteTo;
                int max_pos = (int) this.getJOB(i).partialSetupE;
                sb.append(max_pos).append(",").append(cur_pos).append(",");
            }

        }
        sv.awaitTermination(1, TimeUnit.DAYS);
        //                sv.shutdown();
        if (factoryOUT != null) {
            factoryOUT.close();
            factoryOUT = null;
        }

        System.out.println("after addjobs");

        //                Thread.sleep(500);
        return null;
    }

    public void setMaximumThreads(int maximumThreads) {
        this.maximumThreads = maximumThreads;
        svx.setMaximumPoolSize(maximumThreads);
        svx.setCorePoolSize(maximumThreads);
    }

    public int getMaximumThreads() {
        return maximumThreads;
    }

    public void setURL(String url) {
        this.URL = url;
    }

    public void saveToFile(String filename) {
        this.filename = filename;
        Path p = Paths.get(filename);

        try {
            factoryOUT = new RandomAccessFile(p.toFile(), "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //            @Override
    //            public loader.JOB call() throws Exception {
    //                System.out.println("ddd");
    //                return null;
    //            }

}