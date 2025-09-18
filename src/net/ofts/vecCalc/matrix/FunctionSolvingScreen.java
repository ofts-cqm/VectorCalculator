package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.numberPane.MatrixWithSizeBarPane;
import net.ofts.vecCalc.numberPane.VecNPane;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FunctionSolvingScreen extends ICalculatorScreen {
    public MatrixWithSizeBarPane pane = new MatrixWithSizeBarPane("Matrix", 3, 3, this, true);
    //public MatrixPane pane = new MatrixPane("Matrix", 3, 3, this, true).setInverseRowsAndColumns();
    //public SwapRowPane swapPane = new SwapRowPane(pane);
    public VecNPane result = new VecNPane("Vector", 3, this, true);
    //public JSlider dimension = new JSlider(SwingConstants.HORIZONTAL, 2, 10, 3);
    public JButton calculateButton = new JButton("Solve");
    public JLabel infoLabel = new JLabel("Click \"Solve\" to solve the function set!");

    //public int currentDimension = 3;

    public FunctionSolvingScreen(){
        setLayout(new BorderLayout());

        add(pane, BorderLayout.CENTER);
        add(result, BorderLayout.EAST);

        JPanel resCtrl = new JPanel(new FlowLayout());
        infoLabel.setForeground(Color.GREEN);
        resCtrl.add(infoLabel);
        calculateButton.setFocusable(false);
        calculateButton.addActionListener(e -> refreshResult());
        resCtrl.add(calculateButton);
        add(resCtrl, BorderLayout.SOUTH);
        pane.sizer.associatedFuncSolvScreen = this;
    }

    public void onDimensionChanged(int length){
        //currentDimension = length;

        remove(result);
        result = new VecNPane("Vector", length, this, true);
        add(result, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    @Override
    public void refreshResult() {
        for (int i = 0; i < pane.matrix.matrix.height; i++) {
            for (int j = 0; j < pane.matrix.matrix.width; j++) {
                if (pane.matrix.matrixField[i][j].getBackground().equals(Color.red)) {
                    infoLabel.setText("Please Complete The Matrix First!");
                    infoLabel.setForeground(Color.red);
                    return;
                }
            }
        }

        String info = pane.matrix.matrix.solveFunction(result.vector);
        if(!Objects.equals(info, "")){
            infoLabel.setText(info);
            infoLabel.setForeground(Color.red);
        } else {
            infoLabel.setText("Click \"Solve\" to solve the function set!");
            infoLabel.setForeground(Color.GREEN);
        }

        pane.matrix.updateMatrixPane();
        result.setVector(result.vector);
        revalidate();
        repaint();
    }

    @Override
    public void onPageOpened(JFrame frame) {
        frame.setSize(540, 440);
        frame.setTitle("Function Solver (Matrix)");
    }
}
