package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.Main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPane extends JPanel {
    public JButton currentOperation;
    public JButton move;
    public static final String[] operation = new String[]{"+", "-", "Scale", "Dot", "Cross", "Normalize", "Length", "Proj", "Perp"};
    public byte[]operandForm = new byte[]{1, 1, 2, 1, 1, 0, 0, 1, 1};
    public byte[]resultForm = new byte[]{1, 1, 1, 2, 1, 1, 2, 1, 1};
    public int index = 0;
    public Vec3Screen parent;

    public ControlPane(Vec3Screen root){
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
        add(move);
        add(Box.createRigidArea(new Dimension(100, 30)));
    }

    public void move(){
        if (parent.result.current.equals(parent.result.number)){
            double num = parent.result.number.num;
            parent.a.x1.setText(num + "");
            parent.a.vector.x1 = num;

        }
        else{
            parent.a.setVector(parent.result.vector.vector);
        }
        parent.a.repaint();
        parent.b.vector.resetVector();
        parent.b.number.resetNum();
        parent.refreshResult();
    }

    public void setOperator(int operator){
        index = operator;
        currentOperation.setText(operation[index]);
        parent.b.setOperand(operandForm[index]);
        parent.result.setOperand(resultForm[index]);
        parent.refreshResult();
    }

    public void nextOperator(){
        index++;
        if (index == operation.length) index = 0;
        currentOperation.setText(operation[index]);
        parent.b.setOperand(operandForm[index]);
        parent.result.setOperand(resultForm[index]);
        parent.refreshResult();
        Main.updateSelectedMenuItem(Vec3Screen.items[index]);
    }

    public void previousOperator(){
        index--;
        if (index == -1) index = operation.length - 1;
        currentOperation.setText(operation[index]);
        parent.b.setOperand(operandForm[index]);
        parent.result.setOperand(resultForm[index]);
        parent.refreshResult();
        Main.updateSelectedMenuItem(Vec3Screen.items[index]);
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
