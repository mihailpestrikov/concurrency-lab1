package org.labs;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.jupiter.api.Assertions.*;

class KitchenTest {

    @Test
    void givesExactlyAvailablePortions() {
        Kitchen kitchen = new Kitchen(3);
        assertTrue(kitchen.takePortion());
        assertTrue(kitchen.takePortion());
        assertTrue(kitchen.takePortion());
        assertFalse(kitchen.takePortion());
        assertEquals(0, kitchen.portionsLeft());
    }

    @Test
    void neverOversellsUnderContention() throws InterruptedException {
        int portions = 100_000;
        Kitchen kitchen = new Kitchen(portions);
        LongAdder taken = new LongAdder();
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService pool = Executors.newFixedThreadPool(8)) {
            for (int t = 0; t < 8; t++) {
                pool.submit(() -> {
                    start.await();
                    while (kitchen.takePortion()) {
                        taken.increment();
                    }
                    return null;
                });
            }
            start.countDown();
        }
        assertEquals(portions, taken.sum());
        assertEquals(0, kitchen.portionsLeft());
    }
}
