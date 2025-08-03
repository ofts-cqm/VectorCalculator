package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.IMultipleOperation;
import net.ofts.vecCalc.Main;
import net.ofts.vecCalc.matrix.MatrixControlPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class VecNScreen extends ICalculatorScreen implements IMultipleOperation {
    public static int currentLength = 4;

    public VecNPane a;
    public VolatilePane b, result;
    public VecNControlPane control;
    public JSlider dimension;

    public VecNScreen(){
        setLayout(new FlowLayout());
        setSize(540, 380);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 + 270, screenSize.height / 2);
        dimension = new JSlider(SwingConstants.VERTICAL, 2, 10, 4);
        dimension.setPaintTicks(true);
        dimension.setPaintLabels(true);
        dimension.setMajorTickSpacing(1);
        dimension.addChangeListener(this::onDimensionChanged);
        add(dimension);
        add(a = new VecNPane("Vector A", currentLength, this, true));
        add(control = new VecNControlPane(this));
        add(b = new VolatilePane("Vector B", "Number B", this, true));
        add(result = new VolatilePane("Result", "Result", this, false));
    }

    public void onDimensionChanged(ChangeEvent e){
        // remove everything and add again so the layout doesn't change
        // I know this is a stupid method, but (kinda) it works at least
        remove(a);
        remove(b);
        remove(control);
        remove(result);

        // try to preserve previous level's data
        VecN fieldA = a.vector;
        VecN fieldB = b.vecN.vector;
        double numB = b.number.num;

        currentLength = dimension.getValue();
        a = new VecNPane("Vector A", currentLength, this, true);
        a.setVectorPreserveLength(fieldA);
        add(a);
        add(control);
        a.repaint();
        b.vecN = new VecNPane("Vector B", currentLength, this, true);
        if (b.current == b.vecN) {
            b.remove(b.current);
            b.add(b.vecN);
            b.current = b.vecN;
            b.repaint();
        }
        add(b);
        result.vecN = new VecNPane("Result", currentLength, this, false);
        if (result.current == result.vecN){
            result.remove(result.current);
            result.add(result.vecN);
            result.current = result.vecN;
            result.repaint();
        }
        add(result);
        // God knows why I need to go to the next operator and then go back......
        // if I don't do so, Vector A will shift to the left side of the slider......
        // BUT WHY??? control.nextOperator does nothing to Vector A!!! IT DOES NOTHING TO V1!!!
        // Can anyone tell me why this works?
        // Anyway, since it works, let's just not change it.
        control.nextOperator();
        control.previousOperator();

        if (b.current == b.vecN){
            b.vecN.setVectorPreserveLength(fieldB);
        } else if (b.current == b.number){
            b.number.setNum(numB);
        }

        repaint();
    }

    public void refreshResult(){
        VecN v1 = a.vector;
        VecN v2 = b.vecN.vector;
        double num2 = b.number.num;

        //"+", "-", "X", "·", "X", "Norm", "Len", "Proj", "Perp"
        switch (control.index){
            case 0: {
                result.vecN.setVector(VecN.add(v1, v2));
                return;
            }
            case 1: {
                result.vecN.setVector(VecN.sub(v1, v2));
                return;
            }
            case 2: {
                result.vecN.setVector(VecN.scale(v1, num2));
                return;
            }
            case 3: {
                result.number.setNum(VecN.dot(v1, v2));
                return;
            }
            case 5: {
                result.vecN.setVector(VecN.norm(v1));
                return;
            }
            case 6: {
                result.number.setNum(VecN.len(v1));
                return;
            }
            case 7: {
                result.vecN.setVector(VecN.Proj(v1, v2));
                return;
            }
            case 8: {
                result.vecN.setVector(VecN.Perp(v1, v2));
                return;
            }
            default:
        }
    }

    @Override
    public void onPageOpened(JFrame frame) {
        frame.setSize(540, 380);
        frame.setTitle("Vector Calculator (Vector N)");
    }

    @Override
    public void setOperation(int operation) {
        control.setOperation(operation);
    }

    public static void addMenuItem(JMenu menu){
        for (int i = 0; i < VecNControlPane.operation.length; i++) {
            if (i == 4) continue;
            JMenuItem item = new JMenuItem(VecNControlPane.operation[i]);
            item.addActionListener(Main::operationListener);
            item.setActionCommand("vecN" + i);
            menu.add(item);
        }
    }
}

