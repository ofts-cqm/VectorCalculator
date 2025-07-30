package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.vector.VecN;

public class Matrix {
    public double[][] values;

    public Matrix(int size){
        values = new double[size][size];
    }

    public Matrix(int height, int width){
        values = new double[height][width];
    }

    public String RREF(VecN answer){
        if (values.length != values[0].length) return "Not Square Matrix";
        int size = values.length;

        for (int i = 0; i < size; i++){
            if (values[i][i] == 0){
                // if we found a full zero line that probably means it is intended
                // thus we just make sure it is the last row
                if (isFullZero(values[i])){
                    // find a line that is not full zero
                    int swapWith = -1;
                    for (int j = i + 1; j < size; j++) {
                        if (!isFullZero(values[j])) {
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
                // therefore, we can just skip this line and calculate the next.
                // fuck u linear algebra
                // Todo: check if we can swap with previous rows
                if (!foundSomething){
                    continue;
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
