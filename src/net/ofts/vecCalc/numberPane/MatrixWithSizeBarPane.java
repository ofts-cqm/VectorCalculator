package net.ofts.vecCalc.numberPane;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.Number;

import java.awt.*;

public class MatrixWithSizeBarPane extends AbstractNumberPane {
    public MatrixPane matrix;
    public MatrixSizerPane sizer;

    public String name;
    public ICalculatorScreen parent;
    public boolean editable;

    public MatrixWithSizeBarPane(String name, int height, int width, ICalculatorScreen parent, boolean editable){
        this.matrix = new MatrixPane(name, height, width, parent, editable);
        this.name = name;
        this.parent = parent;
        this.editable = editable;
        this.sizer = new MatrixSizerPane(this::onDimensionChanged);
        setLayout(new BorderLayout());
        add(matrix, BorderLayout.CENTER);
        add(sizer, BorderLayout.NORTH);
    }

    public void onDimensionChanged(int height, int width){
        remove(matrix);
        matrix = new MatrixPane(name, height, width, parent, editable);
        add(matrix, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public void resetPane() {
        matrix.resetPane();
    }

    @Override
    public AbstractNumberPane cloneWithValue(INumber number) {
        assert number instanceof Matrix;
        Matrix matrix = (Matrix) number;
        MatrixWithSizeBarPane mat = new MatrixWithSizeBarPane(this.name, matrix.height, matrix.width, this.parent, this.editable);
        mat.matrix.setMatrix(matrix);
        return mat;
    }

    @Override
    public INumber getNumber() {
        return matrix.matrix;
    }
}
