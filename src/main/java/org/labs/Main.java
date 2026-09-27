package org.labs;

import java.time.Duration;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        DinnerConfig config;
        try {
            config = parseArgs(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
            return;
        }

        System.out.printf("Dinner: %d programmers, %d waiters, %d portions, max lead %d%n",
                config.programmers(), config.waiters(), config.food(), config.maxLead());
        DinnerResult result = new Dinner(config).run();

        for (int i = 0; i < result.eatenByProgrammer().size(); i++) {
            System.out.printf("Programmer %d ate %d%n", i, result.eatenByProgrammer().get(i));
        }
        System.out.printf("Total eaten: %d, left in kitchen: %d%n", result.totalEaten(), result.foodLeft());
        System.out.printf("Min/max per programmer: %d / %d (difference %d)%n",
                result.minEaten(), result.maxEaten(), result.maxEaten() - result.minEaten());
        System.out.printf("Time: %d ms%n", result.elapsed().toMillis());
    }

    static DinnerConfig parseArgs(String[] args) {
        DinnerConfig d = DinnerConfig.defaults();
        int programmers = d.programmers();
        int waiters = d.waiters();
        long food = d.food();
        int maxLead = d.maxLead();
        Duration think = d.thinkTime();
        Duration eat = d.eatTime();

        for (String arg : args) {
            String[] kv = arg.split("=", 2);
            if (kv.length != 2) {
                throw new IllegalArgumentException("Expected --key=value, got: " + arg);
            }
            String value = kv[1];
            switch (kv[0]) {
                case "--programmers" -> programmers = Integer.parseInt(value);
                case "--waiters" -> waiters = Integer.parseInt(value);
                case "--food" -> food = Long.parseLong(value);
                case "--max-lead" -> maxLead = Integer.parseInt(value);
                case "--think-ms" -> think = Duration.ofMillis(Long.parseLong(value));
                case "--eat-ms" -> eat = Duration.ofMillis(Long.parseLong(value));
                default -> throw new IllegalArgumentException("Unknown option: " + kv[0]);
            }
        }
        return new DinnerConfig(programmers, waiters, food, maxLead, think, eat);
    }
}
