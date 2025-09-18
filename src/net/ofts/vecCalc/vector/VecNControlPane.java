package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.Main;
import net.ofts.vecCalc.numberPane.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VecNControlPane extends JPanel {
    public JButton currentOperation;
    public JButton move;
    public static final String[] operation = new String[]{"+", "-", "Scale", "Dot", "Normalize", "Length", "Proj", "Perp"};
    public static Class<? extends AbstractNumberPane>[] operandForm = new Class[]{VecNPane.class, VecNPane.class, NumPane.class, VecNPane.class, BlankPane.class, BlankPane.class, VecNPane.class, VecNPane.class};
    public static Class<? extends AbstractNumberPane>[] resultForm = new Class[]{VecNPane.class, VecNPane.class, VecNPane.class, NumPane.class, VecNPane.class, NumPane.class, VecNPane.class, VecNPane.class};
    public int index = 0;
    public VecNScreen parent;

    public VecNControlPane(VecNScreen root){
        this.parent = root;
        setBorder(new TitledBorder("Operation"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(100, 120)));
        currentOperation = new JButton("+");
        currentOperation.setActionCommand("next");
        currentOperation.addActionListener(new ClickHandler());
        currentOperation.setFocusable(false);
        currentOperation.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(currentOperation);
        add(Box.createRigidArea(new Dimension(100, 60)));
        move = new JButton("<html>Move<br>Result</html>");
        move.setActionCommand("move");
        move.addActionListener(new ClickHandler());
        move.setFocusable(false);
        move.setAlignmentX(Component.CENTER_ALIGNMENT);
        move.setMaximumSize(new Dimension(100, 50));
        add(move);
        add(Box.createRigidArea(new Dimension(100, 30)));
    }

    public void move(){
        if (parent.result.getCurrent() instanceof NumPane number){
            double num = number.num;
            VecN vec = new VecN(VecNScreen.currentLength);
            vec.elements[0] = num;
            parent.operandA.getPanel(VecNPane.class).setVector(vec);
        }
        else{
            parent.operandA.getPanel(VecNPane.class).setVector(parent.result.getPanel(VecNPane.class).vector);
        }
        parent.refreshResult();
    }

    public void setOperation(int operator){
        index = operator;
        currentOperation.setText(operation[index]);
        parent.operandB.displayPanel(operandForm[index]);
        parent.result.displayPanel(resultForm[index]);
        parent.refreshResult();
    }

    public void nextOperator(){
        index++;
        if (index == operation.length) index = 0;
        currentOperation.setText(operation[index]);
        parent.operandB.displayPanel(operandForm[index]);
        parent.result.displayPanel(resultForm[index]);
        parent.refreshResult();
        Main.updateSelectedMenuItem(VecNScreen.items[index]);
    }

    public class ClickHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("next")){
                nextOperator();
            } else if(e.getActionCommand().equals("move")){
                move();
            }
        }
    }
}
