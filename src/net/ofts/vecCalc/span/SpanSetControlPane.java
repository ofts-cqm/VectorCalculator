package net.ofts.vecCalc.span;

import net.ofts.vecCalc.AbstractNumberPane;
import net.ofts.vecCalc.Main;
import net.ofts.vecCalc.matrix.MatrixCalcScreen;
import net.ofts.vecCalc.vector.BlankPane;
import net.ofts.vecCalc.vector.VecNPane;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SpanSetControlPane extends JPanel {
    public JButton currentOperation;
    public static final String[] operation = new String[]{"Find Base", "Is In"};
    public static final Class<? extends AbstractNumberPane>[] operandForm = new Class[]{BlankPane.class, VecNPane.class};
    public static final Class<? extends AbstractNumberPane>[] resultForm = new Class[]{SpanSetPane.class, BooleanPane.class};
    public int index = 0;
    public SpanSetScreen parent;
    public JLabel error;

    public SpanSetControlPane(SpanSetScreen root){
        this.parent = root;
        setBorder(new TitledBorder("Operation"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(100, 60)));
        error = new JLabel("<html>Press Enter to Calculate</html>");
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentOperation = new JButton("+");
        currentOperation.setActionCommand("next");
        currentOperation.addActionListener(this::onOperationChanged);
        currentOperation.setFocusable(false);
        currentOperation.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(currentOperation);
        add(Box.createRigidArea(new Dimension(100, 60)));
        add(parent.operandB.sizer);
    }

    public void setOperation(int opCode){
        index = opCode;
        currentOperation.setText(operation[index]);
        parent.operandA.displayPanel(operandForm[index]);
        parent.result.displayPanel(resultForm[index]);
        parent.refreshResult();
    }

    public void onOperationChanged(ActionEvent e){
        index++;
        if (index == operation.length) index = 0;

        currentOperation.setText(operation[index]);
        parent.operandA.displayPanel(operandForm[index]);
        parent.result.displayPanel(resultForm[index]);
        parent.refreshResult();
        Main.updateSelectedMenuItem(MatrixCalcScreen.items[index]);
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
