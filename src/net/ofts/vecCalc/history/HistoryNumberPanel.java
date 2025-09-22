package net.ofts.vecCalc.history;

import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.Number;
import net.ofts.vecCalc.vector.Vec3;
import net.ofts.vecCalc.vector.VecN;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.NumberFormat;

public class HistoryNumberPanel extends JPanel {
    public INumber number;
    public static final NumberFormat formatter = NumberFormat.getNumberInstance();

    public HistoryNumberPanel(INumber number){
        this.setMaximumSize(new Dimension(50, 75));
        this.setPreferredSize(new Dimension(50, 75));
        if (number instanceof Matrix mat) initMatrix(mat);
        else if (number instanceof Vec3 vec) initVector(new double[]{vec.x1, vec.x2, vec.x3});
        else if (number instanceof VecN vec) initVector(vec.elements);
        else if (number instanceof Number num) initNumber(num.num);
    }

    public void initNumber(double val){
        this.setLayout(new BorderLayout());
        add(new JLabel(formatter.format(val)), BorderLayout.CENTER);
    }

    public void initVector(double[] values){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new TitledBorder("Vect"));

        add(new JLabel(formatter.format(values[0])));
        add(new JLabel(formatter.format(values[1])));
        if(values.length == 3)
            add(new JLabel(formatter.format(values[2])));
        else if(values.length > 3){
            add(new JLabel("..."));
        }
    }

    public void initMatrix(Matrix matrix){
        this.setLayout(new GridLayout(Math.max(matrix.height, 3), Math.max(matrix.width, 3)));
        this.setBorder(new TitledBorder("Matx"));

        for (int i = 0; i < 2; i++){
            add(new JLabel(formatter.format(matrix.entries[0][i])));
            add(new JLabel(formatter.format(matrix.entries[1][i])));
            if (matrix.width == 3) add(new JLabel(formatter.format(matrix.entries[2][i])));
            else if (matrix.width > 3) add(new JLabel("..."));
        }

        if (matrix.height == 3){
            add(new JLabel(formatter.format(matrix.entries[0][2])));
            add(new JLabel(formatter.format(matrix.entries[1][2])));
            if (matrix.width == 3) add(new JLabel(formatter.format(matrix.entries[2][2])));
            else if (matrix.width > 3) add(new JLabel("..."));
        } else if(matrix.width > 3){
            add(new JLabel("..."));
            add(new JLabel("..."));
            add(new JLabel("..."));
        }
    }

    static {
        formatter.setMaximumFractionDigits(1);
    }
}
