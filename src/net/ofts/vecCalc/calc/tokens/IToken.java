package net.ofts.vecCalc.calc.tokens;

import net.ofts.vecCalc.calc.StringMatcher;

public abstract class IToken {
    public IToken parent;

    public IToken(IToken parent){
        this.parent = parent;
    }

    public abstract double evaluate();
    public abstract IToken parse(StringMatcher input, IToken lastInput);
    public abstract IToken create();
    public abstract void debug();
}
