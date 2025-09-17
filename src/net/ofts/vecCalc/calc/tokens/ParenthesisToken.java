package net.ofts.vecCalc.calc.tokens;

import net.ofts.vecCalc.calc.Calculator;
import net.ofts.vecCalc.calc.StringMatcher;

public class ParenthesisToken extends IToken {
    public RootToken content;

    public ParenthesisToken(RootToken content, IToken parent){
        super(parent instanceof OperatorToken opt ? opt : null);
        this.content = content;
    }

    @Override
    public double evaluate() {
        return content.evaluate();
    }

    @Override
    public IToken parse(StringMatcher input, IToken lastInput) {
        input.push();
        if (!input.match("(")) { input.pop(); return null; }
        RootToken root = new RootToken();
        IToken matched = Calculator.matchNext(root);
        root.right = matched;

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
        return finalizeNumber(lastInput, new ParenthesisToken(root, lastInput));
    }

    @Override
    public IToken create() {
        return new ParenthesisToken(null, null);
    }

    @Override
    public void debug() {
        content.debug();
    }
}
