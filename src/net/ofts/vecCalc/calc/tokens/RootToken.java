package net.ofts.vecCalc.calc.tokens;

public class RootToken extends OperatorToken {
    public RootToken(IToken start) {
        super("", 114514);
        right = start;
        if (start != null) start.parent = this;
    }

    @Override
    public double evaluate() {
        return right.evaluate();
    }

    @Override
    public void debug() {
        right.debug();
    }
}
