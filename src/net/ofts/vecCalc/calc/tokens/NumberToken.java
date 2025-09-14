package net.ofts.vecCalc.calc.tokens;

import net.ofts.vecCalc.calc.Calculator;
import net.ofts.vecCalc.calc.StringMatcher;
import net.ofts.vecCalc.calc.StructureTreeLogger;

public record NumberToken(double value) implements IToken {

    @Override
    public double evaluate() {
        return value;
    }

    private int getNextDigit(StringMatcher input) {
        String tmp = input.get(1);
        if (tmp.isEmpty()) return -1;
        int c = tmp.charAt(0) - '0';
        if (c >= 0 && c <= 9) {
            input.skip(1);
            return c;
        }
        return -1;
    }

    public long matchNum(StringMatcher input, int start) {
        long number = 0;
        while (start != -1) {
            number *= 10;
            number += start;
            start = getNextDigit(input);
        }
        return number;
    }

    @Override
    public IToken parse(StringMatcher input, IToken lastInput) {
        if (input.matchIgnoreCase("E")) return new NumberToken(Math.E);
        if (input.matchIgnoreCase("PI")) return new NumberToken(Math.PI);
        if (input.match("π")) return new NumberToken(Math.PI);
        if (input.matchIgnoreCase("ans")) return new NumberToken(Calculator.previousAnswer);

        input.push();
        int digit = getNextDigit(input);
        if (digit == -1) {
            input.pop();
            return null;
        }
        long number = matchNum(input, digit);

        if (input.match(".")) {
            digit = getNextDigit(input);
            if (digit != -1) {
                long decimal = matchNum(input, digit);
                double fp = decimal / Math.pow(10, String.valueOf(decimal).length());
                input.ignore();
                return new NumberToken(number + fp);
            }
        }
        if (input.matchIgnoreCase("e")) {
            boolean flip = input.match("-");
            digit = getNextDigit(input);
            if (digit != -1) {
                long exponent = matchNum(input, digit);
                if (flip) exponent *= -1;
                input.ignore();
                return new NumberToken(number * Math.pow(10, exponent));
            }
            Calculator.error("Missing Expression After E");
            input.pop();
            return null;
        }

        input.ignore();
        return new NumberToken(number);
    }

    @Override
    public IToken create() {
        return new NumberToken(0);
    }

    @Override
    public void debug() {
        StructureTreeLogger.push();
        StructureTreeLogger.write("Number Token: " + value, true);
        StructureTreeLogger.pop();
    }
}
