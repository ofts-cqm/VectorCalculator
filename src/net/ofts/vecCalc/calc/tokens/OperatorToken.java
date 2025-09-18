package net.ofts.vecCalc.calc.tokens;

import net.ofts.vecCalc.calc.Calculator;
import net.ofts.vecCalc.calc.StringMatcher;
import net.ofts.vecCalc.calc.StructureTreeLogger;

import java.util.Hashtable;
import java.util.function.Function;

public class OperatorToken extends IToken {
    public IToken right;
    public final int precedence;
    public final String matcher;

    public static OperatorToken currentParent = null;

    public static Hashtable<String, Function<Double, Double>> algorithms = new Hashtable<>();

    public OperatorToken(String matcher, int precedence) {
        super(currentParent);
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
            if (lastInput instanceof OperatorToken o) currentParent = o;
            OperatorToken currentToken = create();
            currentToken.right = Calculator.matchNext(currentToken);
            if (currentToken.right != null){
                input.ignore();
                return finalizeNumber(lastInput, currentToken);
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

    private static double arcsinh(double val){
        return Math.log(val + Math.sqrt(val * val + 1));
    }

    private static double arccosh(double val){
        return Math.log(val + Math.sqrt(val * val - 1));
    }

    private static double arctanh(double val){
        return 0.5 * Math.log((1 + val) / (1 - val));
    }

    private static void register(String name, int precedence, Function<Double, Double> func){
        algorithms.put(name, func);
        Calculator.registeredTokens.add(new OperatorToken(name, precedence));
    }

    public static void init(){
        register("sinh", 2, Math::sinh);
        register("cosh", 2, Math::cosh);
        register("tanh", 2, Math::tanh);
        register("csch", 2, a -> 1 / Math.sinh(a));
        register("sech", 2, a -> 1 / Math.cosh(a));
        register("coth", 2, a -> 1 / Math.tanh(a));
        register("asinh", 2, OperatorToken::arcsinh);
        register("arsinh", 2, OperatorToken::arcsinh);
        register("arcsinh", 2, OperatorToken::arcsinh);
        register("acosh", 2, OperatorToken::arccosh);
        register("arcosh", 2, OperatorToken::arccosh);
        register("arccosh", 2, OperatorToken::arccosh);
        register("atanh", 2, OperatorToken::arctanh);
        register("artanh", 2, OperatorToken::arctanh);
        register("arctanh", 2, OperatorToken::arctanh);
        register("sin", 2, OperatorToken::sin);
        register("cos", 2, OperatorToken::cos);
        register("tan", 2, OperatorToken::tan);
        register("csc", 2, OperatorToken::csc);
        register("sec", 2, OperatorToken::sec);
        register("cot", 2, OperatorToken::cot);
        register("arcsin", 2, OperatorToken::arcsin);
        register("asin", 2, OperatorToken::arcsin);
        register("arccos", 2, OperatorToken::arccos);
        register("acos", 2, OperatorToken::arccos);
        register("arctan", 2, OperatorToken::arctan);
        register("atan", 2, OperatorToken::arctan);
        register("log", 2, Math::log10);
        register("ln", 2, Math::log);
        register("abs", 2, Math::abs);
        register("sqrt", 1, Math::sqrt);
        register("cbrt", 1, Math::cbrt);
        register("-", 0, a -> -a);
    }
}
