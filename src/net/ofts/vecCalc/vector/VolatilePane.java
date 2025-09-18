package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.numberPane.BlankPane;
import net.ofts.vecCalc.numberPane.NumPane;
import net.ofts.vecCalc.numberPane.Vec3Pane;
import net.ofts.vecCalc.numberPane.VecNPane;

import javax.swing.*;

@Deprecated
public class VolatilePane extends BlankPane {
    public Vec3Pane vector;
    public VecNPane vecN;
    public NumPane number;
    public BlankPane blank;
    public JPanel current;
    public boolean isVecN;

    public VolatilePane(String title1, String title2, Vec3Screen root, boolean editable){
        super();
        vector = new Vec3Pane(title1, root, editable);
        number = new NumPane(title2, root, editable);
        blank = new BlankPane();
        add(vector);
        current = vector;
        isVecN = false;
    }

    public VolatilePane(String title1, String title2, VecNScreen root, boolean editable){
        super();
        vecN = new VecNPane(title1, VecNScreen.currentLength, root, editable);
        number = new NumPane(title2, root, editable);
        blank = new BlankPane();
        add(vecN);
        current = vecN;
        isVecN = true;
    }

    public void setOperand(byte operand){
        remove(current);

        switch (operand){
            case 1: {
                JPanel temp = isVecN ? vecN : vector;
                if (current == temp) {
                    add(current);
                    return;
                }
                current = temp;
                if (isVecN) {
                    vecN.resetVector();
                } else {
                    vector.resetVector();
                }
                break;
            }
            case 2: {
                if (current == number) {
                    add(number);
                    return;
                }
                current = number;
                number.resetNum();
                break;
            }
            default: current = blank;
        }

        add(current);
        repaint();
    }
}
