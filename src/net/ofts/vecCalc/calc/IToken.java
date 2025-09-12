package net.ofts.vecCalc.calc;

public interface IToken {
    double evaluate();
    IToken parse(StringMatcher input, IToken lastInput);
    IToken create();
    void debug();
}
