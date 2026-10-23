package com.portfolio.db;

public class QualityGate {
    private static final double MIN_PASS_RATE = 90.0;

    private QualityGate() {
    }

    public static void main(String[] args) {
        double rate = ResultsRepository.passRate();
        int passed = ResultsRepository.count("PASS");
        int failed = ResultsRepository.count("FAIL");

        System.out.printf("Quality gate: %d passed, %d failed, pass rate %.1f%% (minimum %.1f%%)%n",
                passed, failed, rate, MIN_PASS_RATE);

        if (rate < MIN_PASS_RATE) {
            System.out.println("QUALITY GATE FAILED");
            System.exit(1);
        }
        System.out.println("QUALITY GATE PASSED");
    }
}
