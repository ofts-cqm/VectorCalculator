package net.ofts.vecCalc.matrix.rref;

import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.VecN;

public class AugmentedMatrix {
    public double[][] entries;
    public final int height;
    public final int width;
    public final int boundry;

    public AugmentedMatrix(Matrix matrix){
        this(matrix, new Matrix(matrix.height, 0));
    }

    public AugmentedMatrix(Matrix matrix, VecN vector)
    {
        this(matrix, new Matrix(new VecN[]{vector}));
    }

    public AugmentedMatrix(Matrix left, Matrix right){
        assert left.height == right.height;
        this.height = left.height;
        this.width = left.width + right.width;
        this.boundry = left.width;
        this.entries = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < left.width; j++) {
                entries[i][j] = left.entries[j][i];
            }
            for (int j = 0; j < right.width; j++) {
                entries[i][j + boundry] = right.entries[j][i];
            }
        }
    }

    public Matrix getMainMatrix(){
        Matrix matrix = new Matrix(height, boundry);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < boundry; j++) {
                matrix.entries[j][i] = entries[i][j];
            }
        }
        return matrix;
    }

    public Matrix getRightMatrix(){
        assert boundry != width;
        Matrix matrix = new Matrix(height, boundry);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - boundry; j++) {
                matrix.entries[j][i] = entries[i][j + boundry];
            }
        }
        return matrix;
    }

    public Matrix getCombinedMatrix(){
        Matrix matrix = new Matrix(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix.entries[j][i] = entries[i][j];
            }
        }
        return matrix;
    }

    public VecN getVector(){
        assert boundry == width - 1;
        VecN vec = new VecN(height);
        for (int i = 0; i < height; i++) {
            vec.elements[i] = entries[i][width - 1];
        }
        return vec;
    }

    /**
     * whether a number should be treated as zero
     * @param n the number
     * @return whether it should be treated as zero
     */
    private boolean isZero(double n){
        return n < 1e-10 && n > -(1e-10);
    }

    /**
     * find the first Non-Zero number in the column
     * @param column the column to search
     * @param fromIndex the index where search starts
     * @return the index of the first non-zero. -1 if all zero.
     */
    private int firstNonZeroInColumn(int column, int fromIndex){
        for (int i = fromIndex; i < height; i++) {
            if (!isZero(entries[i][column])) return i;
        }
        return -1;
    }

    private void swapRow(int rowA, int rowB){
        double[] tmp = entries[rowA];
        entries[rowA] = entries[rowB];
        entries[rowB] = tmp;
    }

    /**
     * divide the entire row by a number
     * @param row the index of the row to perform this operation
     * @param number the number to divide
     * @param startingIndex the index in this row where operation starts
     */
    private void divideBy(int row, double number, int startingIndex){
        for (int i = startingIndex; i < width; i++) {
            entries[row][i] /= number;
        }
    }

    /**
     * set the targeted entry to zero using elementary row operations
     * @param row the row where the operation is performed
     * @param pivot the pivot row
     * @param startingIndex the index in the row to start this operation
     */
    private void clearEntry(int row, int pivot, int startingIndex){
        if (isZero(entries[row][startingIndex])) return;

        double multiplier = entries[row][startingIndex];
        for (int i = startingIndex; i < width; i++) {
            entries[row][i] -= multiplier * entries[pivot][i];
        }
    }

    /**
     * helper function used to ref a sub-matrix
     * @param fromIndex the index of the top line to be considered
     * @param startingColumn the starting column of ref procedure
     * @return the new starting column
     */
    private int ref(int fromIndex, int startingColumn){
        int nonZeroIndex = firstNonZeroInColumn(startingColumn, fromIndex);
        while (nonZeroIndex == -1){
            startingColumn++;
            if (startingColumn == width) return width;
            nonZeroIndex = firstNonZeroInColumn(startingColumn, fromIndex);
        }

        if (fromIndex != nonZeroIndex){
            swapRow(fromIndex, nonZeroIndex);
            nonZeroIndex = fromIndex;
        }

        divideBy(nonZeroIndex, entries[nonZeroIndex][startingColumn], startingColumn);
        for (int i = nonZeroIndex + 1; i < height; i++) {
            clearEntry(i, nonZeroIndex, startingColumn);
        }
        return startingColumn + 1;
    }

    /**
     * Used to RREF Matrices
     * @return this ptr
     */
    public AugmentedMatrix rref(){
        int startingColumn = 0, refed_row = 0;
        while (startingColumn != width){
            startingColumn = ref(refed_row, startingColumn);
            refed_row++;
            if (refed_row == height) break;
        }

        int currentRow = 0;
        for (int i = 0; i < width; i++) {
            if (currentRow == height) break;
            if (isZero(entries[currentRow][i])) continue;
            for (int j = 0; j < currentRow; j++) {
                clearEntry(j, currentRow, i);
            }
            currentRow++;
        }
        return this;
    }
}
