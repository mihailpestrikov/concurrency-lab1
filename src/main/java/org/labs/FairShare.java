package org.labs;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

final class FairShare {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition minRaised = lock.newCondition();
    private final long[] served;
    private final int maxLead;
    private boolean closed;

    FairShare(int programmers, int maxLead) {
        this.served = new long[programmers];
        this.maxLead = maxLead;
    }

    boolean awaitTurn(int id) throws InterruptedException {
        lock.lock();
        try {
            while (!closed && served[id] - min() >= maxLead) {
                minRaised.await();
            }
            return !closed;
        } finally {
            lock.unlock();
        }
    }

    void recordServed(int id) {
        lock.lock();
        try {
            long before = min();
            served[id]++;
            if (min() > before) {
                minRaised.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    void close() {
        lock.lock();
        try {
            closed = true;
            minRaised.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private long min() {
        long min = Long.MAX_VALUE;
        for (long s : served) {
            min = Math.min(min, s);
        }
        return min;
    }
}
