import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.*;
import java.util.function.BiFunction;

public class MegaFactory extends ArrayList<JobbFactory> implements RunnableFuture {
    public ExecutorService sv = Executors.newFixedThreadPool(3);
    public ThreadPoolExecutor svx = (ThreadPoolExecutor) sv;
    public BiFunction<MegaFactory, Object, Object> callbackk = null;
    public int Thread_PRIORITY = Thread.NORM_PRIORITY;
    //            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1,
    // numOfWorkerThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    {
        svx.setMaximumPoolSize(3);
        svx.setCorePoolSize(3);
        //                svx.getQueue().
        //                svx.setThreadFactory(new
        // OpJobThreadFactory(Thread.NORM_PRIORITY+2));

    }

    @Override
    public void run() {
        try {
            //                    for (int i = 0; i < this.size(); i++) {
            //                        this.get(i)
            //                    }
            //                    this.get(0).call();
            sv.invokeAll(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (mayInterruptIfRunning) sv.shutdownNow();
        else sv.shutdown();
        return false;
    }

    @Override
    public boolean isCancelled() {
        return sv.isTerminated();
    }

    @Override
    public boolean isDone() {
        ListIterator<JobbFactory> fi = this.listIterator();
        boolean ret = true;
        if (fi != null)
            while (fi.hasNext()) {
                JobbFactory next = fi.next();

                if (!next.sv.isTerminated() || !next.sv.isShutdown()) {
                    ret = false;
                    break;
                }
            }
        return ret;
        //                return sv.isShutdown();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return sv.awaitTermination(timeout, unit);
        //                return null;
    }

    void push(String url) {
        JobbFactory j = new JobbFactory();
        this.add(j);
        callbackk.apply(this, "this message after push. count elements:" + this.size());
    }

    public synchronized Object asyncProgress(
            BiFunction<MegaFactory, Object, Object> o, Object p) {
        callbackk = o;
        //                return o.apply(this, p);
        return callbackk;
    }
}