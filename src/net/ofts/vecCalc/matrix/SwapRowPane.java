package net.ofts.vecCalc.matrix;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SwapRowPane extends JPanel {
    public int rows;
    public MatrixPane matrix;

    public SwapRowPane(MatrixPane matrix){
        this.matrix = matrix;
        this.rows = matrix.matrixField.length;
        setLayout(new GridLayout(rows, 2, 10, 10));
        setBorder(new TitledBorder("Swap Rows"));
        add(new JLabel());
        add(getButton(0, false));
        for (int i = 1; i < rows - 1; i++) {
            add(getButton(i, true));
            add(getButton(i, false));
        }
        add(getButton(rows - 1, true));
        add(new JLabel());
        setPreferredSize(new Dimension(100, 300));
    }

    public JButton getButton(int row, boolean up){
        JButton button = new JButton(up ? "↑" : "↓");
        button.addActionListener(this::onButtonPressed);
        button.setActionCommand(row + button.getText());
        button.setFocusable(false);
        return button;
    }

    public void onButtonPressed(ActionEvent e){
        String command = e.getActionCommand();
        // nobody uses R11 right? as long as the dimension is within (include) 10, 1 digit is fine.
        // Programmers count from zero
        int targetRow = command.charAt(0) - '0';
        boolean up = command.charAt(1) == '↑';
        double[][] entries = matrix.matrix.entries;
        if (up){
            double[] temp = entries[targetRow];
            entries[targetRow] = entries[targetRow - 1];
            entries[targetRow - 1] = temp;
        }else{
            double[] temp = entries[targetRow];
            entries[targetRow] = entries[targetRow + 1];
            entries[targetRow + 1] = temp;
        }
        matrix.updateMatrixPane();
        matrix.revalidate();
        matrix.repaint();
    }
}
