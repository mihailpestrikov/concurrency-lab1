package org.labs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(10)
class FairShareTest {

    @Test
    void leaderWaitsForLaggard() throws Exception {
        FairShare fairShare = new FairShare(2, 1);
        assertTrue(fairShare.awaitTurn(0));
        fairShare.recordServed(0);

        try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
            Future<Boolean> turn = pool.submit(() -> fairShare.awaitTurn(0));
            assertThrows(TimeoutException.class, () -> turn.get(100, TimeUnit.MILLISECONDS));

            fairShare.recordServed(1);
            assertTrue(turn.get(1, TimeUnit.SECONDS));
        }
    }

    @Test
    void closeReleasesWaiting() throws Exception {
        FairShare fairShare = new FairShare(2, 1);
        fairShare.recordServed(0);

        try (ExecutorService pool = Executors.newSingleThreadExecutor()) {
            Future<Boolean> turn = pool.submit(() -> fairShare.awaitTurn(0));
            assertThrows(TimeoutException.class, () -> turn.get(100, TimeUnit.MILLISECONDS));

            fairShare.close();
            assertFalse(turn.get(1, TimeUnit.SECONDS));
        }
    }

    @Test
    void allowsLeadUpToMaxLead() throws InterruptedException {
        FairShare fairShare = new FairShare(3, 3);
        for (int i = 0; i < 3; i++) {
            assertTrue(fairShare.awaitTurn(0));
            fairShare.recordServed(0);
        }
    }
}
