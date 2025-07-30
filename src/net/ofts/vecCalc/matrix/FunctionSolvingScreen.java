package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.vector.VecNPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.Objects;

public class FunctionSolvingScreen extends ICalculatorScreen {
    public MatrixPane pane = new MatrixPane("Matrix", 3, 3, this, true);
    public SwapRowPane swapPane = new SwapRowPane(pane);
    public VecNPane result = new VecNPane("Vector", 3, this, true);
    public JSlider dimension = new JSlider(SwingConstants.HORIZONTAL, 2, 10, 3);
    public JButton calculateButton = new JButton("Solve");
    public JLabel infoLabel = new JLabel("Click \"Solve\" to solve the function set!");

    public int currentDimension = 3;

    public FunctionSolvingScreen(){
        setLayout(new BorderLayout());

        add(swapPane, BorderLayout.WEST);

        add(pane, BorderLayout.CENTER);

        JPanel dimCtrl = new JPanel(new FlowLayout());
        JLabel dimText = new JLabel("Dimension: ");
        dimText.setForeground(Color.red);
        dimCtrl.add(dimText);
        dimension.setPaintTicks(true);
        dimension.setPaintLabels(true);
        dimension.setMajorTickSpacing(1);
        dimension.setFocusable(false);
        dimension.addChangeListener(this::onDimensionChanged);
        dimCtrl.add(dimension);
        add(dimCtrl, BorderLayout.NORTH);

        add(result, BorderLayout.EAST);

        JPanel resCtrl = new JPanel(new FlowLayout());
        infoLabel.setForeground(Color.GREEN);
        resCtrl.add(infoLabel);
        calculateButton.setFocusable(false);
        calculateButton.addActionListener(e -> refreshResult());
        resCtrl.add(calculateButton);
        add(resCtrl, BorderLayout.SOUTH);
    }

    public void onDimensionChanged(ChangeEvent e){
        currentDimension = dimension.getValue();

        remove(pane);
        pane = new MatrixPane("Matrix", currentDimension, currentDimension, this, true);
        add(pane, BorderLayout.CENTER);

        remove(swapPane);
        swapPane = new SwapRowPane(pane);
        add(swapPane, BorderLayout.WEST);

        remove(result);
        result = new VecNPane("Vector", currentDimension, this, true);
        add(result, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    @Override
    public void refreshResult() {
        for (int i = 0; i < currentDimension; i++) {
            for (int j = 0; j < currentDimension; j++) {
                if (pane.matrixField[i][j].getBackground().equals(Color.red)) {
                    infoLabel.setText("Please Complete The Matrix First!");
                    infoLabel.setForeground(Color.red);
                    return;
                }
            }
        }

        String info = pane.matrix.RREF(result.vector);
        if(!Objects.equals(info, "")){
            infoLabel.setText(info);
            infoLabel.setForeground(Color.red);
        } else {
            infoLabel.setText("Click \"Solve\" to solve the function set!");
            infoLabel.setForeground(Color.GREEN);
        }

        pane.updateMatrixPane();
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
