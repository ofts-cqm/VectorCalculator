package net.ofts.vecCalc.span;

import net.ofts.vecCalc.AbstractNumberPane;
import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.matrix.MatrixSizerPane;
import net.ofts.vecCalc.vector.VecN;

import java.awt.*;

public class SpanSetPane extends AbstractNumberPane {
    public VectorListPane list;
    public MatrixSizerPane sizer;
    public String name;
    public ICalculatorScreen parent;
    public boolean editable;

    public SpanSetPane(String name, int length, int dimension, ICalculatorScreen parent, boolean editable){
        this.list = new VectorListPane(name, dimension, length, parent, editable);
        this.name = name;
        this.parent = parent;
        this.editable = editable;
        this.sizer = new MatrixSizerPane(this::onDimensionChanged);
        setLayout(new BorderLayout());
        add(list, BorderLayout.CENTER);
    }

    public SpanSetPane(String name, ICalculatorScreen parent, VecN[] values, boolean editable){
        this(name, values.length, values.length > 0 ? values[0].elements.length : 0, parent, editable);
        for (int i = 0; i < values.length; i++) {
            list.vectors[i].setVector(values[i]);
        }
    }

    @Override
    public void resetPane() {
        list.resetPane();
    }

    public void onDimensionChanged(int height, int width){
        remove(list);
        list = new VectorListPane(name, height, width, parent, editable);
        add(list, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
