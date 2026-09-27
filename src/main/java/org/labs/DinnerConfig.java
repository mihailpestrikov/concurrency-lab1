package org.labs;

import java.time.Duration;
import java.util.Objects;

public record DinnerConfig(int programmers, int waiters, long food, int maxLead,
                           Duration thinkTime, Duration eatTime) {

    public DinnerConfig {
        if (programmers < 2) throw new IllegalArgumentException("programmers must be >= 2");
        if (waiters < 1) throw new IllegalArgumentException("waiters must be >= 1");
        if (food < 0) throw new IllegalArgumentException("food must be >= 0");
        if (maxLead < 1) throw new IllegalArgumentException("maxLead must be >= 1");
        Objects.requireNonNull(thinkTime, "thinkTime");
        Objects.requireNonNull(eatTime, "eatTime");
        if (thinkTime.isNegative() || eatTime.isNegative()) {
            throw new IllegalArgumentException("durations must be >= 0");
        }
    }

    public static DinnerConfig of(int programmers, int waiters, long food) {
        return new DinnerConfig(programmers, waiters, food, 1, Duration.ZERO, Duration.ZERO);
    }

    public static DinnerConfig defaults() {
        return of(7, 2, 1_000_000);
    }
}
