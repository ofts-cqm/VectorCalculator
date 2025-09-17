package net.ofts.vecCalc.calc.tokens;

import net.ofts.vecCalc.calc.StringMatcher;

public abstract class IToken {
    public OperatorToken parent;

    public IToken(OperatorToken parent){
        this.parent = parent;
    }

    public abstract double evaluate();
    public abstract IToken parse(StringMatcher input, IToken lastInput);
    public abstract IToken create();
    public abstract void debug();

    public static IToken finalizeNumber(IToken lastInput, IToken currentToken){
        if (lastInput instanceof NumberToken num){
            BinaryOperatorToken temp = new BinaryOperatorToken("*", 3);
            temp.left = num;
            temp.right = currentToken;
            currentToken.parent = temp;
            num.parent.right = temp;
            temp.parent = num.parent;
            num.parent = temp;
            return temp;
        }else if(lastInput instanceof OperatorToken opt && opt.right instanceof NumberToken num){
            BinaryOperatorToken temp = new BinaryOperatorToken("*", 3);
            temp.left = num;
            temp.right = currentToken;
            currentToken.parent = temp;
            num.parent = temp;
            opt.right = temp;
            temp.parent = opt;
            return temp;
        }
        return currentToken;
    }
}
