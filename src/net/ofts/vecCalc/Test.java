package net.ofts.vecCalc;

import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.VecN;

public class Test {
    public static void main(String[] args) {
        Matrix matrix = new Matrix(3);
        //matrix.values = new double[][]{{1, 3}, {1, 1}};
        matrix.values = new double[][]{{2, 1, 9}, {0, 1, 2}, {1, 0, 3}};
        VecN vecn = new VecN(3);
        //vecn.elements = new double[]{-1, 3};
        vecn.elements = new double[]{31, 8, 10};
        String result = matrix.RREF(vecn);
        System.out.println(result);
    }
}
