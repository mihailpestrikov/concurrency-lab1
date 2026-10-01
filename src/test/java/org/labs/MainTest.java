package org.labs;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void defaultsMatchTask() {
        assertEquals(DinnerConfig.defaults(), Main.parseArgs(new String[0]));
    }

    @Test
    void parsesAllOptions() {
        DinnerConfig c = Main.parseArgs(new String[]{
                "--programmers=5", "--waiters=3", "--food=100", "--max-lead=2", "--think-ms=4", "--eat-ms=6"});
        assertEquals(new DinnerConfig(5, 3, 100, 2, Duration.ofMillis(4), Duration.ofMillis(6)), c);
    }

    @Test
    void rejectsBadArguments() {
        assertThrows(IllegalArgumentException.class, () -> Main.parseArgs(new String[]{"--unknown=1"}));
        assertThrows(IllegalArgumentException.class, () -> Main.parseArgs(new String[]{"--food"}));
        assertThrows(IllegalArgumentException.class, () -> Main.parseArgs(new String[]{"--food=abc"}));
    }
}
