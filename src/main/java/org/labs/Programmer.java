package org.labs;

import java.time.Duration;
import java.util.concurrent.Callable;

final class Programmer implements Callable<Long> {
    private final int id;
    private final Spoon firstSpoon;
    private final Spoon secondSpoon;
    private final Waiters waiters;
    private final Duration thinkTime;
    private final Duration eatTime;

    Programmer(int id, Spoon left, Spoon right, Waiters waiters, Duration thinkTime, Duration eatTime) {
        this.id = id;
        this.firstSpoon = left.id() < right.id() ? left : right;
        this.secondSpoon = left.id() < right.id() ? right : left;
        this.waiters = waiters;
        this.thinkTime = thinkTime;
        this.eatTime = eatTime;
    }

    @Override
    public Long call() throws InterruptedException {
        long eaten = 0;
        while (waiters.bringPortion(id)) {
            eat();
            eaten++;
            pause(thinkTime);
        }
        return eaten;
    }

    private void eat() throws InterruptedException {
        firstSpoon.take();
        try {
            secondSpoon.take();
            try {
                pause(eatTime);
            } finally {
                secondSpoon.putDown();
            }
        } finally {
            firstSpoon.putDown();
        }
    }

    private static void pause(Duration duration) throws InterruptedException {
        if (!duration.isZero()) {
            Thread.sleep(duration);
        }
    }

    Spoon firstSpoon() {
        return firstSpoon;
    }

    Spoon secondSpoon() {
        return secondSpoon;
    }
}
