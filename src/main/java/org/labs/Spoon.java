package org.labs;

import java.util.concurrent.locks.ReentrantLock;

final class Spoon {
    private final int id;
    private final ReentrantLock lock = new ReentrantLock(true);

    Spoon(int id) {
        this.id = id;
    }

    int id() {
        return id;
    }

    void take() throws InterruptedException {
        lock.lockInterruptibly();
    }

    void putDown() {
        lock.unlock();
    }
}
