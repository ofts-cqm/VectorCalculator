package net.ofts.vecCalc;

import com.google.gson.Gson;
import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.VecN;

public class Test {
    public static void main(String[] args) {
        Matrix mat = new Matrix(new VecN[]{
                new VecN(new double[]{1, 2, 3}),
                new VecN(new double[]{4, 5, 6}),
                new VecN(new double[]{7, 8, 9})
        });

        Gson gson = new Gson();
        System.out.println(gson.toJson(mat));
        System.out.println(gson.fromJson(gson.toJson(mat), Matrix.class).toString());
        /*Calculator.debugMode = true;
        Calculator.logLevel = Calculator.LogLevel.INFO;
        Calculator.setUp();
        double val = Calculator.evaluate("45(1+1)", true);
        System.out.println("Value: " + val);
        System.out.println("Log: ");
        System.out.println(Calculator.getLog());*/
    }
    //45(1+1)
    // 4pi + 5e
    // 4abs-2
    // sin5pi
}
