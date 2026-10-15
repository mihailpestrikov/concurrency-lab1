package org.labs;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class Dinner {
    private final DinnerConfig config;

    public Dinner(DinnerConfig config) {
        this.config = config;
    }

    public DinnerResult run() throws InterruptedException {
        int n = config.programmers();
        Spoon[] spoons = new Spoon[n];
        for (int i = 0; i < n; i++) {
            spoons[i] = new Spoon(i);
        }
        Kitchen kitchen = new Kitchen(config.food());
        Waiters waiters = new Waiters(config.waiters(), kitchen, new FairShare(n, config.maxLead()));

        List<Programmer> programmers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            programmers.add(new Programmer(i, spoons[i], spoons[(i + 1) % n], waiters,
                    config.thinkTime(), config.eatTime()));
        }

        long start = System.nanoTime();
        List<Future<Long>> futures;
        try (ExecutorService pool = Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name("programmer-", 0).factory())) {
            futures = pool.invokeAll(programmers);
        }
        Duration elapsed = Duration.ofNanos(System.nanoTime() - start);

        List<Long> eaten = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            try {
                eaten.add(futures.get(i).get());
            } catch (ExecutionException e) {
                throw new IllegalStateException("Programmer " + i + " failed", e.getCause());
            }
        }
        return new DinnerResult(eaten, kitchen.portionsLeft(), elapsed);
    }
}
