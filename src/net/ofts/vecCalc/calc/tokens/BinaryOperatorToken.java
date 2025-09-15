package net.ofts.vecCalc.calc.tokens;

import net.ofts.vecCalc.calc.*;

import java.util.Hashtable;
import java.util.function.BiFunction;

public class BinaryOperatorToken extends OperatorToken {
    public IToken left;

    public static Hashtable<String, BiFunction<Double, Double, Double>> algorithms = new Hashtable<>();

    public BinaryOperatorToken(String matcher, int precedence) {
        super(matcher, precedence);
    }

    @Override
    public IToken parse(StringMatcher input, IToken lastToken){
        input.push();
        if (input.match(matcher)){
            BinaryOperatorToken currentToken = create();
            //IToken previousToken;

            if (lastToken instanceof OperatorToken opToken && opToken.precedence > precedence){
                if (opToken.right == null){
                    if(!matcher.equals("-")) Calculator.error("No Matched Token Before Binary Operator " + matcher);
                    input.pop();
                    return null;
                }
                currentToken.left = opToken.right;
                opToken.right = currentToken;
                //previousToken = lastToken;
                currentParent = lastToken;
            }else{
                //previousToken = currentToken;
                currentToken.left = lastToken;

                if (lastToken == null){
                    if(!matcher.equals("-")) Calculator.error("No Matched Token Before Binary Operator " + matcher);
                    input.pop();
                    return null;
                }

                if (lastToken.parent instanceof OperatorToken opt){
                    opt.right = currentToken;
                }

                currentParent = lastToken.parent;
                if (lastToken instanceof OperatorToken op2 && op2.right == null) {
                    input.pop();
                    return null;
                }
            }

            currentToken.right = Calculator.matchNext(currentToken);//previousToken);
            currentToken.parent = currentParent;
            if (currentToken.right != null){
                input.ignore();
                return currentToken;
            }
            Calculator.error("No Token Matched");

            if (lastToken instanceof OperatorToken opToken && opToken.right == currentToken){
                opToken.right = currentToken.left;
            }else if (lastToken.parent instanceof OperatorToken opt){
                opt.right = currentToken.left;
            }
        }
        input.pop();
        return null;
    }

    @Override
    public double evaluate() {
        return algorithms.get(matcher).apply(left.evaluate(), right.evaluate());
    }

    @Override
    public BinaryOperatorToken create() {
        return new BinaryOperatorToken(this.matcher, this.precedence);
    }

    @Override
    public void debug() {
        StructureTreeLogger.push();
        StructureTreeLogger.write("Binary Operator token: " + matcher, true);
        StructureTreeLogger.push();
        StructureTreeLogger.write("Left: ", false);
        left.debug();
        StructureTreeLogger.write("Right: ", true);
        right.debug();
        StructureTreeLogger.pop();
        StructureTreeLogger.pop();
    }

    private static void register(String name, int precedence, BiFunction<Double, Double, Double> func){
        algorithms.put(name, func);
        Calculator.registeredTokens.add(new BinaryOperatorToken(name, precedence));
    }

    public static void init(){
        register("+", 4, Double::sum);
        register("-", 4, (a, b)-> a - b);
        register("*", 3, (a, b) -> a * b);
        register("/", 3, (a, b) -> a / b);
        register("^", 1, Math::pow);
        register("mod", 1, (a, b) -> a % b);
    }
}
