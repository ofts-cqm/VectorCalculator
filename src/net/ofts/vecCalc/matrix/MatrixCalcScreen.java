package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.vector.BlankPane;
import net.ofts.vecCalc.vector.NumPane;
import net.ofts.vecCalc.vector.VecNPane;

import javax.swing.*;
import java.awt.*;

public class MatrixCalcScreen extends ICalculatorScreen {
    public MatrixWithSizeBarPane matrixA;
    public GenericPane operandB;
    public GenericPane result;
    public MatrixControlPane control;

    public MatrixCalcScreen(){
        matrixA = new MatrixWithSizeBarPane("A", 3, 3, this, true);
        operandB = new GenericPane(
                new MatrixWithSizeBarPane("B", 3, 3, this, true),
                new NumPane("Scale", this, true),
                new BlankPane(),
                new VecNPane("x", 3, this, true)
        );
        operandB.setOverrideSize(null);
        result = new GenericPane(
                new MatrixPane("Result", 3, 3, this, true),
                new VecNPane("x", 3, this, true)
        );
        result.setOverrideSize(null);
        control = new MatrixControlPane(this);

        setLayout(new GridLayout(1, 4));
        add(matrixA);
        add(control);
        add(operandB);
        add(result);
    }

    @Override
    public void refreshResult() {

    }

    @Override
    public void onPageOpened(JFrame frame) {
        frame.setSize(640, 480);
        frame.setTitle("Matrix Calculator");
    }
}
