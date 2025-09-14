package net.ofts.vecCalc.calc.tokens;

import net.ofts.vecCalc.calc.Calculator;
import net.ofts.vecCalc.calc.StringMatcher;
import net.ofts.vecCalc.calc.StructureTreeLogger;

import java.util.Hashtable;
import java.util.function.Function;

public class OperatorToken implements IToken {
    public IToken right;
    public final int precedence;
    public final String matcher;

    public static Hashtable<String, Function<Double, Double>> algorithms = new Hashtable<>();

    public OperatorToken(String matcher, int precedence) {
        this.matcher = matcher;
        this.precedence = precedence;
    }

    @Override
    public double evaluate() {
        return algorithms.get(matcher).apply(right.evaluate());
    }

    @Override
    public IToken parse(StringMatcher input, IToken lastInput) {
        input.push();
        if (input.match(matcher)){
            OperatorToken currentToken = create();
            currentToken.right = Calculator.matchNext(currentToken);
            if (currentToken.right != null){
                input.ignore();
                return currentToken;
            }
            Calculator.error("No Token Matched");
        }
        input.pop();
        return null;
    }

    @Override
    public OperatorToken create() {
        return new OperatorToken(this.matcher, this.precedence);
    }

    @Override
    public void debug() {
        StructureTreeLogger.push();
        StructureTreeLogger.write("Unary Operator token: " + matcher, true);
        StructureTreeLogger.push();
        StructureTreeLogger.write("Right: ", true);
        right.debug();
        StructureTreeLogger.pop();
        StructureTreeLogger.pop();
    }

    public static double angleIn(double val) {return Calculator.isRadian ? val : Math.toRadians(val);}
    public static double angleOut(double val){return Calculator.isRadian ? val : Math.toDegrees(val);}

    // trigonometry
    private static double sin(double val) {return Math.sin(angleIn(val));}
    private static double cos(double val) {return Math.cos(angleIn(val));}
    private static double tan(double val) {return Math.tan(angleIn(val));}
    private static double csc(double val) {return 1 / sin(val);}
    private static double sec(double val) {return 1 / cos(val);}
    private static double cot(double val) {return 1 / tan(val);}

    private static double arcsin(double val) {return angleOut(Math.asin(val));}
    private static double arccos(double val) {return angleOut(Math.acos(val));}
    private static double arctan(double val) {return angleOut(Math.atan(val));}

    private static void register(String name, int precedence, Function<Double, Double> func){
        algorithms.put(name, func);
        Calculator.registeredTokens.add(new OperatorToken(name, precedence));
    }

    public static void init(){
        register("sin", 3, OperatorToken::sin);
        register("cos", 3, OperatorToken::cos);
        register("tan", 3, OperatorToken::tan);
        register("csc", 3, OperatorToken::csc);
        register("sec", 3, OperatorToken::sec);
        register("cot", 3, OperatorToken::cot);
        register("arcsin", 3, OperatorToken::arcsin);
        register("asin", 3, OperatorToken::arcsin);
        register("arccos", 3, OperatorToken::arccos);
        register("acos", 3, OperatorToken::arccos);
        register("arctan", 3, OperatorToken::arctan);
        register("atan", 3, OperatorToken::arctan);
        register("log", 3, Math::log10);
        register("ln", 3, Math::log);
        register("abs", 3, Math::abs);
        register("sqrt", 1, Math::sqrt);
        register("cbrt", 1, Math::cbrt);
        register("-", 0, a -> -a);
    }
}
