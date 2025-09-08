package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.vector.VecN;

public class Matrix {
    public double[][] entries;
    public int height, width;

    public Matrix(int size){
        entries = new double[size][size];
        this.height = size;
        this.width = size;
    }

    public Matrix(int height, int width){
        entries = new double[width][height];
        this.height = height;
        this.width = width;
    }

    public Matrix(VecN[] columns){
        entries = new double[columns.length][];

        for (int i = 0; i < columns.length; i++) {
            entries[i] = columns[i].elements;
        }

        this.height = entries[0].length;
        this.width = entries.length;
    }

    public static Matrix identity(int size){
        Matrix mat = new Matrix(size);
        for (int i = 0; i < size; i++){
            mat.entries[i][i] = 1;
        }
        return mat;
    }

    public static Matrix add(Matrix A, Matrix B){
        assert A.height == B.height && A.width == B.width : "Matrix Size Not Equal";

        Matrix result = new Matrix(A.height, A.width);
        for (int i = 0; i < A.width; i++) {
            for (int j = 0; j < A.height; j++) {
                result.entries[i][j] = A.entries[i][j] + B.entries[i][j];
            }
        }
        return result;
    }

    public static Matrix sub(Matrix A, Matrix B){
        assert A.height == B.height && A.width == B.width : "Matrix Size Not Equal";

        Matrix result = new Matrix(A.height, A.width);
        for (int i = 0; i < A.width; i++) {
            for (int j = 0; j < A.height; j++) {
                result.entries[i][j] = A.entries[i][j] - B.entries[i][j];
            }
        }
        return result;
    }

    public static Matrix scale(Matrix A, double B){
        Matrix result = new Matrix(A.height, A.width);
        for (int i = 0; i < A.width; i++) {
            for (int j = 0; j < A.height; j++) {
                result.entries[i][j] = A.entries[i][j] * B;
            }
        }
        return result;
    }

    public static VecN vecMul(Matrix A, VecN B){
        assert A.width == B.elements.length : "Matrix Width Must Be Equal to Vector Length";

        VecN result = new VecN(A.height);
        for (int i = 0; i < A.width; i++) {
            result.mutableAdd(VecN.scale(new VecN(A.entries[i]), B.elements[i]));
        }
        return result;
    }

    public static Matrix matMul(Matrix A, Matrix B){
        assert A.width == B.height  : "Am Must Be Equal To Bn";

        VecN[] vecs = new VecN[B.width];
        for (int i = 0; i < B.width; i++) {
            vecs[i] = vecMul(A, new VecN(B.entries[i]));
        }
        return new Matrix(vecs);
    }

    public static Matrix transpose(Matrix A){
        Matrix result = new Matrix(A.width, A.height);

        for (int i = 0; i < A.height; i++) {
            for (int j = 0; j < A.width; j++) {
                result.entries[i][j] = A.entries[j][i];
            }
        }
        return result;
    }

    // for RREF, we treat the matrix differently:
    // we treat entries[i] as rows and entries[][i] as columns
    // which is opposite for others.
    public String RREF(VecN answer){
        if (width != height) return "Not Square Matrix";
        int size = entries.length;

        for (int i = 0; i < size; i++){
            if (entries[i][i] == 0){
                // if we found a full zero line that probably means it is intended
                // thus we just make sure it is the last row
                if (isFullZero(entries[i])){
                    // find a line that is not full zero
                    int swapWith = -1;
                    for (int j = i + 1; j < size; j++) {
                        if (!isFullZero(entries[j])) {
                            swapWith = j;
                            break;
                        }
                    }

                    // if we found, swap
                    if (swapWith != -1){
                        swapRowsDuringREFF(i, swapWith, answer);
                        i--;
                    }

                    // if we didn't find, do nothing because we are in the last row

                    continue;
                }

                // if it is not full zero then we can make it work by swaping rows
                // however, since we are in an awt thread, we can let the user do this
                //return "(" + (i + 1) + ", " +  (i + 1) + ")cannot be zero, consider swap lines?";
            }
        }

        for (int i = 0; i < size; i++) {
            // check if we have something to work on
            if(!isNonZero(entries[i][i])){
                // if we found full zero lines it means we reached the bottom
                if (isFullZero(entries[i])){
                    return "Infinite Many Solutions";
                }

                boolean foundSomething = false;
                for (int j = i + 1; j < size; j++) {
                    // if we found some row with target value, swap
                    if (isNonZero(entries[j][i])){
                        swapRowsDuringREFF(i, j, answer);
                        foundSomething = true;
                    }
                }
                // if we can't find anything then we have to end here
                // it is possible that we can swap some previous row to make this work
                // but that means we need to redo the whole process
                // therefore, we can just skip this line and calculate the next.
                // fuck u linear algebra
                // Todo: check if we can swap with previous rows
                if (!foundSomething){
                    continue;
                }
            }

            answer.elements[i] = reduceToOne(entries[i], answer.elements[i], i);
            for (int j = 0; j < size; j++) {
                if (j == i) continue;
                answer.elements[j] = reducedToZero(entries[j], entries[i], answer.elements[j], answer.elements[i], i);
                if (isFullZero(entries[j])){
                    if (isNonZero(answer.elements[j])) return "No Solution";

                    // make sure to put the empty row to the last
                    if (j != size - 1){
                        for (int k = size - 1; k > j; k--) {
                            if (!isFullZero(entries[k])) {
                                swapRowsDuringREFF(j, k, answer);
                                break;
                            }
                        }
                        // no need for special case. if no other row contain something, then this is the last row
                    }
                }
            }
        }

        return "";
    }

    public void swapRowsDuringREFF(int a, int b, VecN vector){
        double[] temp = entries[a];
        entries[a] = entries[b];
        entries[b] = temp;

        double v = vector.elements[a];
        vector.elements[a] = vector.elements[b];
        vector.elements[b] = v;
    }

    public double reduceToOne(double[] row, double ans1, int index){
        double ratio = 1 / (row[index]);

        for (int i = 0; i < row.length; i++){
            row[i] *= ratio;
        }

        return ans1 * ratio;
    }

    public double reducedToZero(double[] row, double[] helper, double ans1, double ans2, int index){
        double ratio = row[index] / helper[index];

        for (int i = 0; i < row.length; i++){
            row[i] -= ratio * helper[i];
        }

        ans1 -= ratio * ans2;
        return ans1;
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

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            builder.append('|');
            for (int j = 0; j < width; j++) {
                builder.append(entries[j][i]).append(' ');
            }
            builder.append("|\n");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Matrix matrix))return false;
        if (width != matrix.width || height != matrix.height) return false;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (entries[i][j] != matrix.entries[i][j]) return false;
            }
        }
        return true;
    }
}
