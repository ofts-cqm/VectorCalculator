package net.ofts.vecCalc.calc.tokens;

import net.ofts.vecCalc.calc.StringMatcher;

public interface IToken {
    double evaluate();
    IToken parse(StringMatcher input, IToken lastInput);
    IToken create();
    void debug();
}
