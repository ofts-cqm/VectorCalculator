package net.ofts.vecCalc.span;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.VecN;
import net.ofts.vecCalc.vector.VecNPane;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class VectorListPane extends JScrollPane {
    public ICalculatorScreen parent;
    public VecNPane[] vectors;
    public JPanel vectorPanel;
    public int length;
    public int dimension;
    public String name;

    public VectorListPane(String name, int dimension, int length, ICalculatorScreen parent, boolean editable){
        super();
        this.name = name;
        this.dimension = dimension;
        this.length = length;
        this.parent = parent;
        setSize(length * 100, 350);
        setPreferredSize(new Dimension(length * 100, 350));
        setBorder(new TitledBorder(name));
        vectorPanel = new JPanel();
        vectorPanel.setLayout(new FlowLayout());
        setViewportView(vectorPanel);

        vectors = new VecNPane[length];
        for (int i = 0; i < length; i++) {
            vectors[i] = new VecNPane("", dimension, parent, editable);
            vectors[i].setSize(50, 300);
            vectors[i].setPreferredSize(new Dimension(50, 300));
            vectorPanel.add(vectors[i]);
        }
        if (length == 0){
            vectorPanel.add(new JLabel("<html>This Set is Empty</html>", SwingConstants.CENTER));
        }
    }

    public void resetPane(){
        for (VecNPane vector : vectors) {
            vector.resetPane();
        }
    }

    public Matrix toMatrix(){
        VecN[] vecN = new VecN[length];
        for (int i = 0; i < length; i++) {
            vecN[i] = vectors[i].vector;
        }
        return new Matrix(vecN);
    }
}
