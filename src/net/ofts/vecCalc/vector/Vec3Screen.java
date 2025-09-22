package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.*;
import net.ofts.vecCalc.numberPane.AbstractNumberPane;
import net.ofts.vecCalc.numberPane.BlankPane;
import net.ofts.vecCalc.numberPane.NumPane;
import net.ofts.vecCalc.numberPane.Vec3Pane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Vec3Screen extends ICalculatorScreen implements IMultipleOperation {
    public GenericPane operandA, operandB, operandC;
    public Vec3ControlPane control;

    public static JMenuItem[] items;

    public Vec3Screen(){
        setLayout(new FlowLayout());
        setSize(540, 380);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 + 270, screenSize.height / 2);
        operandA = new GenericPane(
                new Vec3Pane("Vector A", this, true)
        );
        operandB = new GenericPane(
                new Vec3Pane("Vector B", this, true),
                new NumPane("Number B", this, true),
                new BlankPane()
        );
        operandC = new GenericPane(
                new Vec3Pane("Result", this, false),
                new NumPane("Result", this, false)
        );
        add(operandA);
        add(control = new Vec3ControlPane(this));
        add(operandB);
        add(operandC);
    }

    public void refreshResult(){
        Vec3 v1 = operandA.getPanel(Vec3Pane.class).vector;
        Vec3 v2 = operandB.getPanel(Vec3Pane.class).vector;
        double num2 = operandB.getPanel(NumPane.class).num;

        //"+", "-", "X", "·", "X", "Norm", "Len", "Proj", "Perp"
        AbstractNumberPane result = switch (control.index){
            case 0 -> new Vec3Pane("Result", this, false).setVector(Vec3.add(v1, v2));
            case 1 -> new Vec3Pane("Result", this, false).setVector(Vec3.sub(v1, v2));
            case 2 -> new Vec3Pane("Result", this, false).setVector(Vec3.scale(v1, num2));
            case 3 -> new NumPane("Result", this, false).setNum(Vec3.dot(v1, v2));
            case 4 -> new Vec3Pane("Result", this, false).setVector(Vec3.cross(v1, v2));
            case 5 -> new Vec3Pane("Result", this, false).setVector(Vec3.norm(v1));
            case 6 -> new NumPane("Result", this, false).setNum(Vec3.len(v1));
            case 7 -> new Vec3Pane("Result", this, false).setVector(Vec3.Proj(v1, v2));
            case 8 -> new Vec3Pane("Result", this, false).setVector(Vec3.Perp(v1, v2));
            default -> null;
        };
        operandC.setPanel(result);
    }

    @Override
    public void onPageOpened(JFrame frame) {
        frame.setSize(540, 380);
        frame.setTitle("Vector Calculator (Vector 3)");
    }

    @Override
    public GenericPane getPaneByIndex(int index) {
        if (index == 0) return operandA;
        else return operandB;
    }

    @Override
    public void setOperation(int operation) {
        control.setOperator(operation);
    }

    public static void addMenuItem(JMenu menu){
        items = new JMenuItem[Vec3ControlPane.operation.length];
        for (int i = 0; i < Vec3ControlPane.operation.length; i++) {
            JMenuItem item = new JMenuItem(Vec3ControlPane.operation[i]);
            item.addActionListener(Main::operationListener);
            item.setActionCommand("vec3" + i);
            Main.menuItemMap.put("vec3" + i, item);
            menu.add(item);
            items[i] = item;
        }
    }

    @Deprecated
    public class KeyboardListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            /*if (e.getKeyCode() == KeyEvent.VK_RIGHT) control.nextOperator();
            else if(e.getKeyCode() == KeyEvent.VK_LEFT) control.previousOperator();
            else*/ if(e.getKeyCode() == KeyEvent.VK_ENTER) control.move();
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
