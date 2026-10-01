package org.labs;

import java.time.Duration;
import java.util.List;

public record DinnerResult(List<Long> eatenByProgrammer, long foodLeft, Duration elapsed) {

    public DinnerResult {
        eatenByProgrammer = List.copyOf(eatenByProgrammer);
    }

    public long totalEaten() {
        return eatenByProgrammer.stream().mapToLong(Long::longValue).sum();
    }

    public long minEaten() {
        return eatenByProgrammer.stream().mapToLong(Long::longValue).min().orElse(0);
    }

    public long maxEaten() {
        return eatenByProgrammer.stream().mapToLong(Long::longValue).max().orElse(0);
    }
}
