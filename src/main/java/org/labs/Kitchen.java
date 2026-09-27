package org.labs;

import java.util.concurrent.atomic.AtomicLong;

final class Kitchen {
    private final AtomicLong portionsLeft;

    Kitchen(long portions) {
        this.portionsLeft = new AtomicLong(portions);
    }

    boolean takePortion() {
        while (true) {
            long left = portionsLeft.get();
            if (left == 0) {
                return false;
            }
            if (portionsLeft.compareAndSet(left, left - 1)) {
                return true;
            }
        }
    }

    long portionsLeft() {
        return portionsLeft.get();
    }
}
