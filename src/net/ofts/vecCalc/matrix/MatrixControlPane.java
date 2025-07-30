package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.AbstractNumberPane;
import net.ofts.vecCalc.vector.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MatrixControlPane extends BlankPane {
    public JButton currentOperation;
    public JButton move;
    public String[] operation = new String[]{"+", "-", "scale", "x", "x", "Transpose"};
    public Class<? extends AbstractNumberPane>[] operandForm = new Class[]{MatrixPane.class, MatrixPane.class, NumPane.class, VecNPane.class, MatrixPane.class, BlankPane.class};
    public Class<? extends AbstractNumberPane>[] resultForm = new Class[]{MatrixPane.class, MatrixPane.class, MatrixPane.class, VecNPane.class, MatrixPane.class, MatrixPane.class};
    public int index = 0;
    public MatrixCalcScreen parent;

    public MatrixControlPane(MatrixCalcScreen root){
        this.parent = root;
        setBorder(new TitledBorder("Operation"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(100, 120)));
        currentOperation = new JButton("+");
        currentOperation.setActionCommand("next");
        currentOperation.addActionListener(this::onOperationChanged);
        currentOperation.setFocusable(false);
        currentOperation.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(currentOperation);
        add(Box.createRigidArea(new Dimension(100, 60)));
        move = new JButton("<html>Move<br>Result</html>");
        move.setActionCommand("move");
        move.addActionListener(this::onOperationChanged);
        move.setFocusable(false);
        move.setAlignmentX(Component.CENTER_ALIGNMENT);
        move.setMaximumSize(new Dimension(100, 50));
        add(move);
        add(Box.createRigidArea(new Dimension(100, 30)));
    }

    public void onOperationChanged(ActionEvent e){
        if(e.getActionCommand().equals("next")){
            index++;
            if (index == operation.length) index = 0;

            currentOperation.setText(operation[index]);
            parent.operandB.displayPanel(operandForm[index]);
            parent.result.displayPanel(resultForm[index]);
            parent.refreshResult();
        }
    }
}
