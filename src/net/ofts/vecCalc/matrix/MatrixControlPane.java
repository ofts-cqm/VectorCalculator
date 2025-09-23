package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.numberPane.*;
import net.ofts.vecCalc.Main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MatrixControlPane extends BlankPane {
    public JButton currentOperation;
    public JButton move;
    public JLabel error;
    public static final String[] operation = new String[]{"+", "-", "scale", "x Vec", "x Mat", "Transpose", "Inversion", "RREF", "rank", "Null/Kernal", "Column/Range"};
    @SuppressWarnings("unchecked")
    public static final Class<? extends AbstractNumberPane>[] operandForm = new Class[]{MatrixPane.class, MatrixPane.class, NumPane.class, VecNPane.class, MatrixPane.class, BlankPane.class, BlankPane.class, BlankPane.class, BlankPane.class, BlankPane.class, BlankPane.class};
    @SuppressWarnings("unchecked")
    public static final Class<? extends AbstractNumberPane>[] resultForm = new Class[]{MatrixPane.class, MatrixPane.class, MatrixPane.class, VecNPane.class, MatrixPane.class, MatrixPane.class, MatrixPane.class, MatrixPane.class, NumPane.class, SpanSetPane.class, SpanSetPane.class};
    public int index = 0;
    public MatrixCalcScreen parent;

    public MatrixControlPane(MatrixCalcScreen root){
        this.parent = root;
        setBorder(new TitledBorder("Operation"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(100, 100)));
        error = new JLabel("<html>Press Enter to Calculate</html>");
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(error);
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

    public void setOperation(int opCode){
        index = opCode;
        currentOperation.setText(operation[index]);
        parent.operandB.displayPanel(operandForm[index]);
        parent.result.displayPanel(resultForm[index]);
        parent.refreshResult(false);
    }

    public void onOperationChanged(ActionEvent e){
        if(e.getActionCommand().equals("next")){
            index++;
            if (index == operation.length) index = 0;

            currentOperation.setText(operation[index]);
            parent.operandB.displayPanel(operandForm[index]);
            parent.result.displayPanel(resultForm[index]);
            parent.refreshResult(false);
            Main.updateSelectedMenuItem(MatrixCalcScreen.items[index]);
        }else{
            MatrixPane matA = parent.matrixA.getPanel(MatrixWithSizeBarPane.class).matrix;
            MatrixPane matR = parent.result.getPanel(MatrixPane.class);
            if (matA.matrix.height == matR.matrix.height && matA.matrix.width == matR.matrix.width){
                matA.setMatrix(matR.matrix);
            }else{
                matA.setMatrixPreserveSize(matR.matrix);
            }
        }
    }

    public void setError(String error){
        this.error.setText("<html>" + error + "</html>");
        this.error.setForeground(Color.red);
        repaint();
    }

    public void setNoError(){
        this.error.setText("<html>Press Enter to Calculate</html>");
        this.error.setForeground(Color.BLACK);
        repaint();
    }
}
