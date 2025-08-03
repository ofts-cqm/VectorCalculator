package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.*;
import net.ofts.vecCalc.vector.BlankPane;
import net.ofts.vecCalc.vector.NumPane;
import net.ofts.vecCalc.vector.VecN;
import net.ofts.vecCalc.vector.VecNPane;

import javax.swing.*;
import java.awt.*;

public class MatrixCalcScreen extends ICalculatorScreen implements IMultipleOperation {
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
        matrixA.sizer.associatedVector = operandB;
        result = new GenericPane(
                new MatrixPane("Result", 3, 3, this, false),
                new VecNPane("x", 3, this, false)
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
        Matrix matA = matrixA.matrix.matrix;
        Matrix matB = operandB.getPanel(MatrixWithSizeBarPane.class).matrix.matrix;
        VecN vecB = operandB.getPanel(VecNPane.class).vector;
        double numB = operandB.getPanel(NumPane.class).num;

        try{
            AbstractNumberPane ans= switch (control.index){
                //+
                case 0 -> new MatrixPane("Result", Matrix.add(matA, matB), this);
                //-
                case 1 -> new MatrixPane("Result", Matrix.sub(matA, matB), this);
                //scale
                case 2 -> new MatrixPane("Result", Matrix.scale(matA, numB), this);
                //vector mul
                case 3 -> new VecNPane("Result", matA.height, this, false).setVector(Matrix.vecMul(matA, vecB));
                //mat mul
                case 4 -> new MatrixPane("Result", Matrix.matMul(matA, matB), this);
                //transpose
                case 5 -> new MatrixPane("Result", Matrix.transpose(matA), this);
                //inversion
                case 6 -> {
                    Matrix mat = new MatrixInversionHelper(matA).getMatrix();
                    if (mat == null) throw new AssertionError("Matrix Not Invertible");
                    yield new MatrixPane("Result", mat, this);
                }
                default -> throw new IllegalStateException("Unexpected value: " + control.index);
            };

            result.setPanel(ans);
            result.displayPanel(ans.getClass());
            control.setNoError();
        }catch (AssertionError e){
            control.setError(e.getMessage());
        }
    }

    public static void addMenuItem(JMenu menu){
        for (int i = 0; i < MatrixControlPane.operation.length; i++) {
            JMenuItem item = new JMenuItem(MatrixControlPane.operation[i]);
            item.addActionListener(Main::operationListener);
            item.setActionCommand("matx" + i);
            menu.add(item);
        }
    }

    @Override
    public void onPageOpened(JFrame frame) {
        frame.setSize(720, 480);
        frame.setTitle("Matrix Calculator");
    }

    @Override
    public void setOperation(int operation) {
        control.setOperation(operation);
    }
}
