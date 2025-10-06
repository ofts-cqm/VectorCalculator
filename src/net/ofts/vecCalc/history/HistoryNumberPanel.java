package net.ofts.vecCalc.history;

import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.ValueDisplayer;
import net.ofts.vecCalc.calc.TextNumber;
import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.Number;
import net.ofts.vecCalc.vector.Vec3;
import net.ofts.vecCalc.vector.VecN;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

public class HistoryNumberPanel extends JPanel {
    public INumber number;
    public Dimension magnifierSize;
    public String magnifierString;
    public boolean isValidPanel = true;

    public static final NumberFormat formatter = NumberFormat.getNumberInstance();

    public HistoryNumberPanel(INumber number){
        this.setMaximumSize(new Dimension(50, 75));
        this.setPreferredSize(new Dimension(50, 75));
        if (number instanceof Matrix mat) initMatrix(mat);
        else if (number instanceof Vec3 vec) initVector(new double[]{vec.x1, vec.x2, vec.x3});
        else if (number instanceof VecN vec) initVector(vec.elements);
        else if (number instanceof Number num) initNumber(num.num);
        else if (number instanceof TextNumber text) initText(text.text);
        else isValidPanel = false;
        addMouseListener(new MouseHandler());
    }

    public static int getTextWidth(JLabel label){
        return label.getFontMetrics(label.getFont()).stringWidth(label.getText());
    }

    public void initText(String text){
        this.setMaximumSize(new Dimension(150, 75));
        this.setPreferredSize(new Dimension(150, 75));
        this.setBorder(new TitledBorder("Expression"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("<html><body>" + text + "</body></html>");
        add(label, BorderLayout.CENTER);

        magnifierString = label.getText();
        magnifierSize = new Dimension(getTextWidth(new JLabel(text)) + 30, 30);
    }

    public void initNumber(double val){
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("Num"));

        magnifierString = formatter.format(val);
        JLabel label = new JLabel(magnifierString);
        add(label, BorderLayout.CENTER);
        magnifierSize = new Dimension(getTextWidth(label) + 30, 30);
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

        StringBuilder builder = new StringBuilder("< ");
        for (double val : values){
            builder.append(formatter.format(val)).append(' ');
        }
        builder.append('>');
        magnifierString = builder.toString();
        magnifierSize = new Dimension(getTextWidth(new JLabel(magnifierString)) + 30, 30);
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

        StringBuilder overall = new StringBuilder("<html><body>");
        int currentWidth = 0;
        for (int i = 0; i < matrix.height; i++){
            StringBuilder inner = new StringBuilder("[ ");
            for (int j = 0; j < matrix.width; j++) {
                inner.append(formatter.format(matrix.entries[j][i])).append(' ');
            }
            inner.append(']');
            currentWidth = Math.max(currentWidth, getTextWidth(new JLabel(inner.toString())));
            overall.append(inner).append("<br>");
        }
        overall.append("</body></html>");
        magnifierString = overall.toString();
        magnifierSize = new Dimension(currentWidth + 30, matrix.height * 20);
    }

    public class MouseHandler extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            if(isValidPanel)
                ValueDisplayer.displayValue(magnifierString, magnifierSize, getLocationOnScreen());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(isValidPanel)
                ValueDisplayer.closeDisplay();
        }
    }

    static {
        formatter.setMaximumFractionDigits(1);
    }
}
