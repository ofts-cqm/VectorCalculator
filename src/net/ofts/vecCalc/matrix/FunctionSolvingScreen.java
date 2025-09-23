package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.history.HistoryItem;
import net.ofts.vecCalc.numberPane.MatrixWithSizeBarPane;
import net.ofts.vecCalc.numberPane.VecNPane;
import net.ofts.vecCalc.vector.VecN;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FunctionSolvingScreen extends ICalculatorScreen {
    public GenericPane pane = new GenericPane(new MatrixWithSizeBarPane("Matrix", 3, 3, this, true));
    public GenericPane result = new GenericPane(new VecNPane("Vector", 3, this, true));
    public JButton calculateButton = new JButton("Solve");
    public JLabel infoLabel = new JLabel("Click \"Solve\" to solve the function set!");

    public FunctionSolvingScreen(){
        setLayout(new BorderLayout());

        add(pane, BorderLayout.CENTER);
        add(result, BorderLayout.EAST);

        JPanel resCtrl = new JPanel(new FlowLayout());
        infoLabel.setForeground(Color.GREEN);
        resCtrl.add(infoLabel);
        calculateButton.setFocusable(false);
        calculateButton.addActionListener(e -> refreshResult(true));
        resCtrl.add(calculateButton);
        add(resCtrl, BorderLayout.SOUTH);
        pane.getPanel(MatrixWithSizeBarPane.class).sizer.associatedFuncSolvScreen = this;
    }

    public void onDimensionChanged(int length){
        //currentDimension = length;

        remove(result);
        result.setPanel(new VecNPane("Vector", length, this, true));
        add(result, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    @Override
    public void refreshResult(boolean recordResult) {
        MatrixWithSizeBarPane pane = this.pane.getPanel(MatrixWithSizeBarPane.class);
        for (int i = 0; i < pane.matrix.matrix.height; i++) {
            for (int j = 0; j < pane.matrix.matrix.width; j++) {
                if (pane.matrix.matrixField[i][j].getBackground().equals(Color.red)) {
                    infoLabel.setText("Please Complete The Matrix First!");
                    infoLabel.setForeground(Color.red);
                    return;
                }
            }
        }

        VecN previousFunction = (VecN) result.getPanel(VecNPane.class).vector.clone();

        String info = pane.matrix.matrix.solveFunction(result.getPanel(VecNPane.class).vector);
        if(!Objects.equals(info, "")){
            infoLabel.setText(info);
            infoLabel.setForeground(Color.red);
        } else {
            infoLabel.setText("Click \"Solve\" to solve the function set!");
            infoLabel.setForeground(Color.GREEN);
        }

        pane.matrix.updateMatrixPane();
        result.getPanel(VecNPane.class).setVector(result.getPanel(VecNPane.class).vector);
        revalidate();
        repaint();

        if (recordResult){
            HistoryItem.recordHistory("func1", pane, new VecNPane("", previousFunction.elements.length, this, false).setVector(previousFunction), result);
        }
    }

    @Override
    public GenericPane getPaneByIndex(int index) {
        if (index == 0) return pane;
        return result;
    }

    @Override
    public String getOperationName(String opcode) {
        return "Function";
    }

    @Override
    public void onPageOpened(JFrame frame) {
        frame.setSize(540, 440);
        frame.setTitle("Function Solver (Matrix)");
    }
}
