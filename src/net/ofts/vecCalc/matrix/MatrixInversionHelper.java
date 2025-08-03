package net.ofts.vecCalc.matrix;

public class MatrixInversionHelper {
    private final double[][] entries;
    private boolean invertible = true;
    private final int size;

    public MatrixInversionHelper(Matrix matrix){
        assert matrix.width == matrix.height : "Not Square Matrix";
        size = matrix.width;
        entries = new double[matrix.height][matrix.width * 2];

        for (int i = 0; i < matrix.height; i++) {
            for (int j = 0; j < matrix.width; j++) {
                entries[i][j] = matrix.entries[j][i];
            }
            entries[i][matrix.width + i] = 1;
        }

        for (int i = 0; i < size; i++) {
            // check if we have something to work on
            if(!isNonZero(entries[i][i])){
                invertible = false;
                return;
            }

            reduceToOne(entries[i], i);
            for (int j = 0; j < size; j++) {
                if (j == i) continue;
                reducedToZero(entries[j], entries[i], i);
                if (isFullZero(entries[j])){
                    invertible = false;
                    return;
                }
            }
        }
    }

    public Matrix getMatrix(){
        if (!invertible) return null;

        Matrix matrix = new Matrix(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix.entries[j][i] = entries[i][j + size];
            }
        }
        return matrix;
    }

    public void reduceToOne(double[] row, int index){
        double ratio = 1 / (row[index]);

        for (int i = 0; i < row.length; i++){
            row[i] *= ratio;
        }
    }

    public void reducedToZero(double[] row, double[] helper, int index){
        double ratio = row[index] / helper[index];

        for (int i = 0; i < row.length; i++){
            row[i] -= ratio * helper[i];
        }
    }

    public boolean isFullZero(double[] row){
        for (double v : row) {
            if (isNonZero(v)) return false;
        }
        return true;
    }

    public boolean isNonZero(double v){
        return v > 1e-10 || v < -(1e-10);
    }
}
