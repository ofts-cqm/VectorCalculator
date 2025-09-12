package net.ofts.vecCalc.calc;

public class RootToken extends OperatorToken{
    public RootToken(IToken start) {
        super("", 114514);
        right = start;
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
