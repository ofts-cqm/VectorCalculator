package net.ofts.vecCalc;

import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.matrix.rref.AugmentedMatrix;

public class Test {
    public static void main(String[] args) {
        /*
         * |1 2 3 4
         * |3 1 4 7
         * |1 1 2 3
         * **/
        Matrix matrix = new Matrix(3, 4);
        matrix.entries = new double[][]{{1, 3, 1}, {2, 1, 1}, {3, 4, 2}, {4, 7, 3}};
        System.out.println(new AugmentedMatrix(matrix).rref().getCombinedMatrix().toString());
    }
}
