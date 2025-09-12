package net.ofts.vecCalc.calc;

public class ParenthesisToken implements IToken {
    public RootToken content;

    public ParenthesisToken(RootToken content){
        this.content = content;
    }

    @Override
    public double evaluate() {
        return content.evaluate();
    }

    @Override
    public IToken parse(StringMatcher input, IToken lastInput) {
        if (!input.match("(")) return null;
        input.push();
        IToken matched = Calculator.matchNext(null);
        RootToken root = new RootToken(matched);

        if (matched == null){
            Calculator.error("Nothing In Parenthesis");
            input.pop();
            return null;
        }

        while (!input.match(")")){
            matched = Calculator.matchNext(root);
            if (matched == null){
                Calculator.error("Closing Parenthesis Not Found");
                input.pop();
                return null;
            }
        }

        input.ignore();
        return new ParenthesisToken(root);
    }

    @Override
    public IToken create() {
        return new ParenthesisToken(null);
    }

    @Override
    public void debug() {
        content.debug();
    }
}
