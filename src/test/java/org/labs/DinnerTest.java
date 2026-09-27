package org.labs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(60)
class DinnerTest {

    private static void assertCorrect(DinnerConfig config, DinnerResult result) {
        assertEquals(config.programmers(), result.eatenByProgrammer().size());
        assertEquals(config.food(), result.totalEaten(), "all food must be eaten exactly once");
        assertEquals(0, result.foodLeft());
        assertTrue(result.maxEaten() - result.minEaten() <= config.maxLead(),
                () -> "unfair: " + result.eatenByProgrammer());
    }

    @ParameterizedTest
    @CsvSource({
            "2, 1, 1000",
            "5, 2, 10000",
            "7, 2, 100000",
            "7, 7, 10000",
            "7, 10, 10000",
            "50, 3, 50000",
            "7, 2, 0",
            "7, 2, 3",
    })
    void eatsAllFoodFairly(int programmers, int waiters, long food) throws InterruptedException {
        DinnerConfig config = DinnerConfig.of(programmers, waiters, food);
        assertCorrect(config, new Dinner(config).run());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 10})
    void respectsMaxLead(int maxLead) throws InterruptedException {
        DinnerConfig config = new DinnerConfig(7, 2, 100_000, maxLead, Duration.ZERO, Duration.ZERO);
        assertCorrect(config, new Dinner(config).run());
    }

    @Test
    void taskScenario() throws InterruptedException {
        DinnerConfig config = DinnerConfig.defaults();
        assertCorrect(config, new Dinner(config).run());
    }

    @Test
    void worksWithThinkAndEatTime() throws InterruptedException {
        DinnerConfig config = new DinnerConfig(5, 2, 200, 1, Duration.ofMillis(1), Duration.ofMillis(1));
        assertCorrect(config, new Dinner(config).run());
    }

    @Test
    void stressRandomConfigs() throws InterruptedException {
        Random random = new Random(42);
        for (int i = 0; i < 200; i++) {
            DinnerConfig config = new DinnerConfig(
                    2 + random.nextInt(20), 1 + random.nextInt(5), random.nextInt(5000),
                    1 + random.nextInt(3), Duration.ZERO, Duration.ZERO);
            assertCorrect(config, new Dinner(config).run());
        }
    }
}
