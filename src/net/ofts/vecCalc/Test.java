package net.ofts.vecCalc;

import net.ofts.vecCalc.calc.Calculator;

public class Test {
    public static void main(String[] args) {
        Calculator.debugMode = true;
        Calculator.logLevel = Calculator.LogLevel.INFO;
        Calculator.setUp();
        double val = Calculator.evaluate("45(1+1)", true);
        System.out.println("Value: " + val);
        System.out.println("Log: ");
        System.out.println(Calculator.getLog());
    }
    // 4pi + 5e
    // 4abs-2
    // sin5pi
}
