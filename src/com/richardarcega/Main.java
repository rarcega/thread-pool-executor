package com.richardarcega;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args)
    {

        ExecutorService pool = Executors.newFixedThreadPool(10);
        Set<Callable<Object>> tasks = new HashSet<Callable<Object>>();

        tasks.add(new MyCallable());

        List<Future<Object>> results = new ArrayList<Future<Object>>();

        try
        {
            results = pool.invokeAll(tasks);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        shutdownAndAwaitTermination(pool);

        for (Future<Object> result : results)
        {
            try
            {
                System.out.println(result.get());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void shutdownAndAwaitTermination(ExecutorService pool)
    {
        pool.shutdown(); // Disable new tasks from being submitted

        try
        {
            // Wait a while for existing tasks to terminate
            if ( ! pool.awaitTermination(30, TimeUnit.MINUTES) )
            {
                pool.shutdownNow(); // Cancel currently executing tasks

                // Wait a while for tasks to respond to being cancelled
                if ( ! pool.awaitTermination(30, TimeUnit.MINUTES) )
                {
                    System.err.println("Pool did not terminate");
                }
            }
        }
        catch (InterruptedException ie)
        {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();

            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

}
