package net.ofts.vecCalc;

import net.ofts.vecCalc.calc.Calculator;

public class Test {
    public static void main(String[] args) {
        Calculator.debugMode = true;
        Calculator.setUp();
        double val = Calculator.evaluate("5 * (7 - 13) + 8 - sin (PI / 2)");
        System.out.println("Value: " + val);
        System.out.println("Log: ");
        System.out.println(Calculator.getLog());
    }
}
