package net.ofts.vecCalc.vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VecNControlPane extends JPanel {
    public JButton currentOperation;
    public JButton move;
    public String[] operation = new String[]{"+", "-", "X", "·", "X", "Norm", "Len", "Proj", "Perp"};
    public byte[]operandForm = new byte[]{1, 1, 2, 1, 1, 0, 0, 1, 1};
    public byte[]resultForm = new byte[]{1, 1, 1, 2, 1, 1, 2, 1, 1};
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
        if (parent.result.current.equals(parent.result.number)){
            double num = parent.result.number.num;
            parent.a.fields[0].setText(num + "");
            parent.a.vector.elements[0] = num;

        }
        else{
            parent.a.setVector(parent.result.vecN.vector);
        }
        parent.a.repaint();
        parent.b.vecN.resetVector();
        parent.b.number.resetNum();
        parent.refreshResult();
    }

    public void nextOperator(){
        index++;
        if (index == operation.length) index = 0;
        if (index == 4) index = 5;
        currentOperation.setText(operation[index]);
        parent.b.setOperand(operandForm[index]);
        parent.result.setOperand(resultForm[index]);
        parent.refreshResult();
    }

    public void previousOperator(){
        index--;
        if (index == -1) index = operation.length - 1;
        if (index == 4) index = 3;
        currentOperation.setText(operation[index]);
        parent.b.setOperand(operandForm[index]);
        parent.result.setOperand(resultForm[index]);
        parent.refreshResult();
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
