package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.IMultipleOperation;
import net.ofts.vecCalc.Main;
import net.ofts.vecCalc.numberPane.BlankPane;
import net.ofts.vecCalc.numberPane.NumPane;
import net.ofts.vecCalc.numberPane.VecNPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class VecNScreen extends ICalculatorScreen implements IMultipleOperation {
    public static int currentLength = 2;
    public static JMenuItem[] items;
    public GenericPane operandA, operandB, result;
    public VecNControlPane control;
    public JSlider dimension;

    public VecNScreen(){
        setLayout(new FlowLayout());
        setSize(540, 380);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 + 270, screenSize.height / 2);
        dimension = new JSlider(SwingConstants.VERTICAL, 2, 10, 2);
        dimension.setPaintTicks(true);
        dimension.setPaintLabels(true);
        dimension.setMajorTickSpacing(1);
        dimension.addChangeListener(this::onDimensionChanged);
        add(dimension);
        operandA = new GenericPane(
                new VecNPane("Vector A", currentLength, this, true)
        );
        operandB = new GenericPane(
                new VecNPane("Vector A", currentLength, this, true),
                new NumPane("Number B", this, true),
                new BlankPane()
        );
        result = new GenericPane(
                new VecNPane("Result", currentLength, this, false),
                new NumPane("Result", this, false)
        );
        add(operandA);
        add(control = new VecNControlPane(this));
        add(operandB);
        add(result);
    }

    public void onDimensionChanged(ChangeEvent e){
        currentLength = dimension.getValue();

        // try to preserve previous level's data
        VecN fieldA = operandA.getPanel(VecNPane.class).vector;
        VecN fieldB = operandB.getPanel(VecNPane.class).vector;

        operandA.setPanel(new VecNPane("Vector A", currentLength, this, true).setVectorPreserveLength(fieldA));
        operandB.setPanel(new VecNPane("Vector B", currentLength, this, true).setVectorPreserveLength(fieldB));
        result.setPanel(new VecNPane("Result", currentLength, this, false));

        refreshResult();
    }

    public void refreshResult(){
        VecN v1 = operandA.getPanel(VecNPane.class).vector;
        VecN v2 = operandB.getPanel(VecNPane.class).vector;
        double num2 = operandB.getPanel(NumPane.class).num;

        //"+", "-", "X", "·", "X", "Norm", "Len", "Proj", "Perp"
        result.setPanel(switch (control.index){
            case 0 -> new VecNPane("Result", currentLength, this, false).setVector(VecN.add(v1, v2));
            case 1 -> new VecNPane("Result", currentLength, this, false).setVector(VecN.sub(v1, v2));
            case 2 -> new VecNPane("Result", currentLength, this, false).setVector(VecN.scale(v1, num2));
            case 3 -> new NumPane("Result", this, false).setNum(VecN.dot(v1, v2));
            case 4 -> new VecNPane("Result", currentLength, this, false).setVector(VecN.norm(v1));
            case 5 -> new NumPane("Result", this, false).setNum(VecN.len(v1));
            case 6 -> new VecNPane("Result", currentLength, this, false).setVector(VecN.Proj(v1, v2));
            case 7 -> new VecNPane("Result", currentLength, this, false).setVector(VecN.Perp(v1, v2));
            default -> null;
        });
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
        items = new JMenuItem[VecNControlPane.operation.length];
        for (int i = 0; i < VecNControlPane.operation.length; i++) {
            JMenuItem item = new JMenuItem(VecNControlPane.operation[i]);
            item.addActionListener(Main::operationListener);
            item.setActionCommand("vecN" + i);
            Main.menuItemMap.put("vecN" + i, item);
            menu.add(item);
            items[i] = item;
        }
    }

    @Override
    public GenericPane getPaneByIndex(int index) {
        if (index == 0) return operandA;
        else return operandB;
    }
}

