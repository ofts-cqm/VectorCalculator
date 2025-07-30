package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.AbstractNumberPane;
import net.ofts.vecCalc.ICalculatorScreen;

import java.awt.*;

public class MatrixWithSizeBarPane extends AbstractNumberPane {
    public MatrixPane matrix;
    public MatrixSizerPane sizer;

    public MatrixWithSizeBarPane(String name, int height, int width, ICalculatorScreen parent, boolean editable){
        this.matrix = new MatrixPane(name, height, width, parent, editable);
        this.sizer = new MatrixSizerPane(matrix);
        setLayout(new BorderLayout());
        add(matrix, BorderLayout.CENTER);
        add(sizer, BorderLayout.NORTH);
    }

    @Override
    public void resetPane() {
        matrix.resetPane();
    }
}
