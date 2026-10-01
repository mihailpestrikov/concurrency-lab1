package org.labs;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DinnerConfigTest {

    @Test
    void rejectsInvalidValues() {
        Duration z = Duration.ZERO;
        assertThrows(IllegalArgumentException.class, () -> new DinnerConfig(1, 2, 10, 1, z, z));
        assertThrows(IllegalArgumentException.class, () -> new DinnerConfig(7, 0, 10, 1, z, z));
        assertThrows(IllegalArgumentException.class, () -> new DinnerConfig(7, 2, -1, 1, z, z));
        assertThrows(IllegalArgumentException.class, () -> new DinnerConfig(7, 2, 10, 0, z, z));
        assertThrows(IllegalArgumentException.class, () -> new DinnerConfig(7, 2, 10, 1, Duration.ofMillis(-1), z));
        assertThrows(NullPointerException.class, () -> new DinnerConfig(7, 2, 10, 1, null, z));
    }

    @Test
    void acceptsMinimalValues() {
        assertDoesNotThrow(() -> DinnerConfig.of(2, 1, 0));
    }
}
