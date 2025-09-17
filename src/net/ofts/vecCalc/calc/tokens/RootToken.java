package net.ofts.vecCalc.calc.tokens;

public class RootToken extends OperatorToken {
    public RootToken() {
        super("", 114514);
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
