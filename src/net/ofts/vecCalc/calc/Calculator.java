package net.ofts.vecCalc.calc;

import net.ofts.vecCalc.calc.tokens.*;

import java.io.StringWriter;
import java.util.ArrayList;

public class Calculator {
    public enum LogLevel{
        INFO(2),
        WARNING(1),
        ERROR(0);

        final int level;
        LogLevel(int level){
            this.level = level;
        }
    }

    public static StringMatcher inputStream;
    private static StringWriter log;
    public static boolean isRadian = true;
    public static boolean debugMode = false;
    public static double previousAnswer = 0;
    public static LogLevel logLevel = LogLevel.ERROR;

    public static final ArrayList<IToken> registeredTokens = new ArrayList<>();

    public static void setUp(){
        BinaryOperatorToken.init();
        OperatorToken.init();
        registeredTokens.add(new NumberToken(0, null));
        registeredTokens.add(new ParenthesisToken(null, null));
    }

    private static void initialize(String input){
        log = new StringWriter();
        inputStream = new StringMatcher(input);
    }

    public static void info(String info){
        if (logLevel.level >= 2) log.append(info).append('\n');
    }

    public static void warning(String warning){
        if (logLevel.level < 1) return;
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

    public static double evaluate(String expression, boolean recordResult){
        initialize(expression);
        IToken matched = matchNext(null);
        RootToken root = new RootToken(matched);
        IToken lastOpToken = matched instanceof OperatorToken opt ? opt : root;

        while (matched != null){
            matched = matchNext(lastOpToken);
            lastOpToken = matched instanceof OperatorToken opt ? opt : root;
        }

        if (!inputStream.isEnd()){
            warning("Some Expressions Are Not Evaluated!");
        }

        if (!inputStream.isClear()){
            warning("Some Internal Errors Occurred");
            inputStream.stack.clear();
        }

        if (root.right == null){
            info("Nothing Evaluated");
            return 0;
        }

        if (debugMode){
            root.debug();
        }

        double result = root.evaluate();
        info("Evaluation Done!");
        if (result < 1e-10 && result > -1e-10) result = 0;
        if (recordResult) previousAnswer = result;
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
