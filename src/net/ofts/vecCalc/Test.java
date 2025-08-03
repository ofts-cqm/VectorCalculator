package net.ofts.vecCalc;

import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.matrix.MatrixInversionHelper;
import net.ofts.vecCalc.vector.VecN;

public class Test {
    public static void main(String[] args) {
        Matrix matrix = new Matrix(3);
        matrix.entries = new double[][]{{1, 1, 1}, {0, 1, 2}, {-1, -2, -2}};
        Matrix result = new MatrixInversionHelper(matrix).getMatrix();
    }
}
