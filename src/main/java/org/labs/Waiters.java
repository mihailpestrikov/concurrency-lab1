package org.labs;

import java.util.concurrent.Semaphore;

final class Waiters {
    private final Semaphore freeWaiters;
    private final Kitchen kitchen;
    private final FairShare fairShare;

    Waiters(int count, Kitchen kitchen, FairShare fairShare) {
        this.freeWaiters = new Semaphore(count, true);
        this.kitchen = kitchen;
        this.fairShare = fairShare;
    }

    boolean bringPortion(int programmerId) throws InterruptedException {
        if (!fairShare.awaitTurn(programmerId)) {
            return false;
        }
        boolean got;
        freeWaiters.acquire();
        try {
            got = kitchen.takePortion();
        } finally {
            freeWaiters.release();
        }
        if (got) {
            fairShare.recordServed(programmerId);
        } else {
            fairShare.close();
        }
        return got;
    }
}
