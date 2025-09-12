package net.ofts.vecCalc.calc;

import java.io.StringWriter;
import java.util.ArrayList;

public class Calculator {
    public static StringMatcher inputStream;

    public static final ArrayList<IToken> registeredTokens = new ArrayList<>();

    private static StringWriter log;

    public static boolean isRadian = true;

    public static boolean debugMode = false;

    public static void setUp(){
        BinaryOperatorToken.init();
        OperatorToken.init();
        registeredTokens.add(new NumberToken(0));
        registeredTokens.add(new ParenthesisToken(null));
    }

    private static void initialize(String input){
        log = new StringWriter();
        inputStream = new StringMatcher(input);
    }

    public static void info(String info){
        log.append(info).append('\n');
    }

    public static void warning(String warning){
        log.append("Warning: ").append(warning).append('\n');
        log.append("At index = ").append(String.valueOf(inputStream.index)).append(": ")
                .append(inputStream.getIfExist(10)).append('\n');
    }

    public static void error(String error){
        log.append("Error: ").append(error).append('\n');
        log.append("At index = ").append(String.valueOf(inputStream.index)).append(": ")
                .append(inputStream.getIfExist(10)).append('\n');
    }

    public static String getLog(){
        return log.toString();
    }

    public static double evaluate(String expression){
        initialize(expression);
        IToken matched = matchNext(null);
        RootToken root = new RootToken(matched);

        while (matched != null){
            matched = matchNext(root);
        }

        if (!inputStream.isEnd()){
            warning("Some Expressions Are Not Evaluated!");
        }

        if (!inputStream.isClear()){
            warning("Some Internal Errors Occurred");
            inputStream.stack.clear();
        }

        if (root.right == null){
            warning("Nothing Evaluated");
            return 0;
        }

        if (debugMode){
            root.debug();
        }

        double result = root.evaluate();
        info("Evaluation Done!");
        if (result < 1e-10 && result > -1e-10) result = 0;
        return result;
    }

    public static IToken matchNext(IToken lastInput){
        IToken matched;
        for (IToken token : registeredTokens) {
            matched = token.parse(inputStream, lastInput);
            if (matched != null) return matched;
        }
        return null;
    }
}
