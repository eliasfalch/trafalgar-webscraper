package at.trafalgar.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Thread {
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public static void run(Runnable task) {
        task.run();
    }

    public static CompletableFuture<Void> runAsync(Runnable task) {
        return CompletableFuture.runAsync(task, EXECUTOR);
    }
}
