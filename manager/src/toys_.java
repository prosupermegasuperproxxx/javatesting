import java.io.File;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class toys_ {
    public static class DiskSpaceDetail {
        public static void main(String[] args) {
            File file = new File("c:");
            //            File file = new
            // File("C:\\Sandbox\\Andrew\\DefaultSettingsMy\\drive\\E\\IDA");
            long totalSpace = file.getTotalSpace(); // total disk space in bytes.
            long freeSpace = file.getFreeSpace(); // FASTER. unallocated / free disk space in bytes.
            long usableSpace = file.getUsableSpace(); // /unallocated / free disk space in bytes.

            System.out.println(" === Partition Detail ===");

            System.out.println(" === bytes ===");
            System.out.println("Total size : " + totalSpace + " bytes");
            System.out.println("Space free : " + usableSpace + " bytes");
            System.out.println("Space free : " + freeSpace + " bytes");

            System.out.println(" === mega bytes ===");
            System.out.println("Total size : " + totalSpace / 1024 / 1024 + " mb");
            System.out.println("Space free : " + usableSpace / 1024 / 1024 + " mb");
            System.out.println("Space free : " + freeSpace / 1024 / 1024 + " mb");
        }
    }

    public static final class OpJobThreadFactory implements ThreadFactory {
        private int priority;
        private boolean daemon;
        private final String namePrefix;
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public OpJobThreadFactory(int priority) {
            this(priority, true);
        }

        public OpJobThreadFactory(int priority, boolean daemon) {
            this.priority = priority;
            this.daemon = daemon;
            namePrefix = "jobpool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            t.setDaemon(daemon);
            t.setPriority(priority);
            return t;
        }
    }
}
