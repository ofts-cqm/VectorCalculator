package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.*;
import net.ofts.vecCalc.matrix.rref.AugmentedMatrix;
import net.ofts.vecCalc.matrix.rref.FunctionAnalyzer;
import net.ofts.vecCalc.numberPane.*;
import net.ofts.vecCalc.span.SpanSetScreen;
import net.ofts.vecCalc.vector.Vec3ControlPane;
import net.ofts.vecCalc.vector.VecN;

import javax.swing.*;
import java.awt.*;

public class MatrixCalcScreen extends ICalculatorScreen implements IMultipleOperation {
    public GenericPane matrixA;
    public GenericPane operandB;
    public GenericPane result;
    public MatrixControlPane control;

    public static JMenuItem[] items;

    public MatrixCalcScreen(){
        matrixA = new GenericPane(
                new MatrixWithSizeBarPane("A", 3, 3, this, true)
        );
        operandB = new GenericPane(
                new MatrixWithSizeBarPane("B", 3, 3, this, true),
                new NumPane("Scale", this, true),
                new BlankPane(),
                new VecNPane("x", 3, this, true)
        );
        operandB.setOverrideSize(null);
        matrixA.getPanel(MatrixWithSizeBarPane.class).sizer.associatedVector = operandB;
        result = new GenericPane(
                new MatrixPane("Result", 3, 3, this, false),
                new VecNPane("Result", 3, this, false),
                new NumPane("Result", this, false),
                new SpanSetPane("Span", 0, 0, this, false)
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
    public void refreshResult(boolean recordResult) {
        Matrix matA = matrixA.getPanel(MatrixWithSizeBarPane.class).matrix.matrix;
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
                    if (matA.width != matA.height) throw new AssertionError("Matrix Not Invertible");

                    Matrix identity = Matrix.identity(matA.width);
                    AugmentedMatrix mat = new AugmentedMatrix(matA, identity).rref();//new MatrixInversionHelper(matA).getMatrix();
                    if (!mat.getMainMatrix().equals(identity)) throw new AssertionError("Matrix Not Invertible");
                    yield new MatrixPane("Result", mat.getRightMatrix(), this);
                }
                //rref
                case 7 -> new MatrixPane("Result", new AugmentedMatrix(matA).rref().getMainMatrix(), this);

                //rank
                case 8 -> {
                    int rank = new AugmentedMatrix(matA).rref().rank();
                    NumPane num = new NumPane("Result", this, false);
                    num.setNum(rank);
                    yield  num;
                }

                // nullspace
                case 9 -> {
                    VecN[] nullspace = new FunctionAnalyzer(new AugmentedMatrix(matA, new VecN(matA.height)).rref().getMainMatrix(), new VecN(matA.height)).analyzeSolution().toVectors();
                    VecN[] ansVec = new VecN[nullspace.length - 1];
                    System.arraycopy(nullspace, 1, ansVec, 0, nullspace.length - 1);
                    yield new SpanSetPane("Result", this, ansVec, false);
                }

                // kernal space
                case 10 -> new SpanSetPane("Result", this, SpanSetScreen.findBase(new SpanSetPane("", this, matA.toVectors(), false)), false);

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
        items = new JMenuItem[MatrixControlPane.operation.length];
        for (int i = 0; i < MatrixControlPane.operation.length; i++) {
            JMenuItem item = new JMenuItem(MatrixControlPane.operation[i]);
            item.addActionListener(Main::operationListener);
            item.setActionCommand("matx" + (i > 9 ? ":" : i));
            Main.menuItemMap.put("matx" + (i > 9 ? ":" : i), item);
            menu.add(item);
            items[i] = item;
        }
    }

    @Override
    public void onPageOpened(JFrame frame) {
        frame.setSize(720, 480);
        frame.setTitle("Matrix Calculator");
    }

    @Override
    public GenericPane getPaneByIndex(int index) {
        if (index == 0) return matrixA;
        return operandB;
    }

    @Override
    public String getOperationName(String opcode) {
        return "Matrix " + MatrixControlPane.operation[opcode.charAt(4) - '0'];
    }

    @Override
    public void setOperation(int operation) {
        control.setOperation(operation);
    }
}
