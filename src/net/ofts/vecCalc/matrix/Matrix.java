package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.vector.VecN;

public class Matrix {
    public double[][] values;

    public Matrix(int size){
        values = new double[size][size];
    }

    public String RREF(VecN answer){
        int size = values.length;

        for (int i = 0; i < size; i++){
            if (values[i][i] == 0){
                return "(" + (i + 1) + ", " +  (i + 1) + ")cannot be zero!";
            }
        }

        for (int i = 0; i < size; i++) {
            // check if we have something to work on
            if(!isNonZero(values[i][i])){
                // if we found full zero lines it means we reached the bottom
                if (isFullZero(values[i])){
                    return "Infinite Many Solutions";
                }

                boolean foundSomething = false;
                for (int j = i + 1; j < size; j++) {
                    // if we found some row with target value, swap
                    if (isNonZero(values[j][i])){
                        swapRowsDuringREFF(i, j, answer);
                        foundSomething = true;
                    }
                }
                // if we can't find anything then we have to end here
                // it is possible that we can swap some previous row to make this work
                // but that means we need to redo the whole process
                // since we are in an awt thread, it's better to let the user to the swap
                // fuck u linear algebra
                if (!foundSomething){
                    return "(" + (i + 1) + ", " + (i + 1) + ") becomes zero during calculation, consider swap rows?";
                }
            }

            answer.elements[i] = reduceToOne(values[i], answer.elements[i], i);
            for (int j = 0; j < size; j++) {
                if (j == i) continue;
                answer.elements[j] = reducedToZero(values[j], values[i], answer.elements[j], answer.elements[i], i);
                if (isFullZero(values[j])){
                    if (isNonZero(answer.elements[j])) return "No Solution";

                    // make sure to put the empty row to the last
                    if (j != size - 1){
                        for (int k = size - 1; k > j; k--) {
                            if (!isFullZero(values[k])) {
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
        double[] temp = values[a];
        values[a] = values[b];
        values[b] = temp;

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
}
