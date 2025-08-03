package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.IMultipleOperation;
import net.ofts.vecCalc.Main;
import net.ofts.vecCalc.matrix.MatrixControlPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Vec3Screen extends ICalculatorScreen implements IMultipleOperation {
    public Vec3Pane a;
    public VolatilePane b, result;
    public ControlPane control;

    public Vec3Screen(){
        setLayout(new FlowLayout());
        setSize(540, 380);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 + 270, screenSize.height / 2);
        add(a = new Vec3Pane("Vector A", this, true));
        add(control = new ControlPane(this));
        add(b = new VolatilePane("Vector B", "Number B", this, true));
        add(result = new VolatilePane("Result", "Result", this, false));
        addKeyListener(new KeyboardListener());
    }

    public void refreshResult(){
        Vec3 v1 = a.vector;
        Vec3 v2 = b.vector.vector;
        double num2 = b.number.num;

        //"+", "-", "X", "·", "X", "Norm", "Len", "Proj", "Perp"
        switch (control.index){
            case 0: {
                result.vector.setVector(Vec3.add(v1, v2));
                return;
            }
            case 1: {
                result.vector.setVector(Vec3.sub(v1, v2));
                return;
            }
            case 2: {
                result.vector.setVector(Vec3.scale(v1, num2));
                return;
            }
            case 3: {
                result.number.setNum(Vec3.dot(v1, v2));
                return;
            }
            case 4: {
                result.vector.setVector(Vec3.cross(v1, v2));
                return;
            }
            case 5: {
                result.vector.setVector(Vec3.norm(v1));
                return;
            }
            case 6: {
                result.number.setNum(Vec3.len(v1));
                return;
            }
            case 7: {
                result.vector.setVector(Vec3.Proj(v1, v2));
                return;
            }
            case 8: {
                result.vector.setVector(Vec3.Perp(v1, v2));
                return;
            }
            default:
        }
    }

    @Override
    public void onPageOpened(JFrame frame) {
        frame.setSize(540, 380);
        frame.setTitle("Vector Calculator (Vector 3)");
    }

    @Override
    public void setOperation(int operation) {
        control.setOperator(operation);
    }

    public static void addMenuItem(JMenu menu){
        for (int i = 0; i < ControlPane.operation.length; i++) {
            JMenuItem item = new JMenuItem(ControlPane.operation[i]);
            item.addActionListener(Main::operationListener);
            item.setActionCommand("vec3" + i);
            menu.add(item);
        }
    }

    public class KeyboardListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) control.nextOperator();
            else if(e.getKeyCode() == KeyEvent.VK_LEFT) control.previousOperator();
            else if(e.getKeyCode() == KeyEvent.VK_ENTER) control.move();
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
