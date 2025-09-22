package net.ofts.vecCalc.span;

import net.ofts.vecCalc.*;
import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.matrix.rref.AugmentedMatrix;
import net.ofts.vecCalc.numberPane.*;
import net.ofts.vecCalc.vector.Vec3ControlPane;
import net.ofts.vecCalc.vector.VecN;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static net.ofts.vecCalc.matrix.rref.AugmentedMatrix.isZero;

public class SpanSetScreen extends ICalculatorScreen implements IMultipleOperation {
    public GenericPane operandA;
    public GenericPane operandB;
    public GenericPane result;
    public SpanSetControlPane control;

    public static JMenuItem[] items;

    public  SpanSetScreen(){
        operandA = new GenericPane(
                new BlankPane(),
                new VecNPane("Vector", 3, this, true)
        );
        operandA.setOverrideSize(null);
        operandB = new GenericPane(new SpanSetPane("Span Set", 3, 3, this, true));
        operandB.getPanel(SpanSetPane.class).sizer.associatedVector = operandA;
        result = new GenericPane(
                new SpanSetPane("Base", 3, 3, this, false),
                new BooleanPane("Is in", this, true),
                new NumPane("Is in", this, false)
        );
        result.setOverrideSize(null);
        control = new SpanSetControlPane(this);

        setLayout(new GridLayout(1, 4));
        add(operandA);
        add(control);
        add(operandB);
        add(result);
    }

    @Override
    public void refreshResult() {
        SpanSetPane span = operandB.getPanel(SpanSetPane.class);
        VecN vector = operandA.getPanel(VecNPane.class).vector;

        try{
            AbstractNumberPane ans= switch (control.index) {
                //find base
                case 0 -> new SpanSetPane("Span Set", this, findBase(span), false);
                //is in
                case 1 -> new BooleanPane("Is In", this, isIn(span, vector));
                default -> throw new IllegalStateException("Unexpected value: " + control.index);
            };

            result.setPanel(ans);
            result.displayPanel(ans.getClass());
            control.setNoError();
        }catch (AssertionError e){
            control.setError(e.getMessage());
        }
    }

    public static VecN[] findBase(SpanSetPane span){
        //Matrix original = span.list.toMatrix();
        Matrix mat = new AugmentedMatrix(span.list.toMatrix()).rref().getMainMatrix();
        ArrayList<Integer> pivots = new ArrayList<>();
        for (int i = 0; i < mat.height; i++) {
            for (int j = 0; j < mat.width; j++) {
                if (!isZero(mat.entries[j][i])) {
                    pivots.add(j);
                    break;
                }
            }
        }

        VecN[] entries = new VecN[pivots.size()];
        for (int i = 0; i < pivots.size(); i++) {
            entries[i] = span.list.vectors[pivots.get(i)].vector;
        }
        return entries;
    }

    public static boolean isIn(SpanSetPane span, VecN vector){
        AugmentedMatrix mat = new AugmentedMatrix(span.list.toMatrix(), vector).rref();
        Matrix main = mat.getMainMatrix();
        VecN answer = mat.getVector();
        lp: for (int i = 0; i < main.height; i++) {
            for (int j = 0; j < main.width; j++) {
                if (!isZero(main.entries[j][i])) continue lp;
            }
            return isZero(answer.elements[i]);
        }
        return true;
    }

    @Override
    public void onPageOpened(JFrame frame) {
        frame.setSize(680, 380);
        frame.setTitle("Span Set Util");
    }

    @Override
    public void setOperation(int operation) {
        control.setOperation(operation);
    }

    @Deprecated
    public static void addMenuItem(JMenu menu){
        items = new JMenuItem[SpanSetControlPane.operation.length];
        for (int i = 0; i < SpanSetControlPane.operation.length; i++) {
            JMenuItem item = new JMenuItem(SpanSetControlPane.operation[i]);
            item.addActionListener(Main::operationListener);
            item.setActionCommand("span" + i);
            menu.add(item);
            items[i] = item;
        }
    }

    @Override
    public GenericPane getPaneByIndex(int index) {
        if (index == 0) return operandA;
        else return operandB;
    }

    @Override
    public String getOperationName(String opcode) {
        return SpanSetControlPane.operation[opcode.charAt(4) - '0'];
    }
}
