package org.labs;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertSame;

class ProgrammerTest {

    @Test
    void takesLowerNumberedSpoonFirst() {
        Spoon s0 = new Spoon(0);
        Spoon s1 = new Spoon(1);
        Spoon s6 = new Spoon(6);

        Programmer first = new Programmer(0, s0, s1, null, Duration.ZERO, Duration.ZERO);
        assertSame(s0, first.firstSpoon());
        assertSame(s1, first.secondSpoon());

        Programmer last = new Programmer(6, s6, s0, null, Duration.ZERO, Duration.ZERO);
        assertSame(s0, last.firstSpoon());
        assertSame(s6, last.secondSpoon());
    }
}
