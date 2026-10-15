package org.labs;

import java.util.concurrent.atomic.AtomicLong;

final class Kitchen {
    private final AtomicLong portionsLeft;

    Kitchen(long portions) {
        this.portionsLeft = new AtomicLong(portions);
    }

    boolean takePortion() {
        return portionsLeft.getAndDecrement() > 0;
    }

    long portionsLeft() {
        return Math.max(0, portionsLeft.get());
    }
}
