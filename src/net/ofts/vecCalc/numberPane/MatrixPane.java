package net.ofts.vecCalc.numberPane;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.calc.Calculator;
import net.ofts.vecCalc.matrix.Matrix;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MatrixPane extends BlankPane {
    public Matrix matrix;
    public JTextField[][] matrixField;
    public ICalculatorScreen parent;
    public boolean inverseRowsAndColumns = false;

    public MatrixPane(String name, Matrix matrix, ICalculatorScreen parent){
        this(name, matrix.height, matrix.width, parent, false);
        setMatrix(matrix);
    }

    public MatrixPane(String name, int height, int width, ICalculatorScreen parent, boolean editable){
        this.parent = parent;
        matrix = new Matrix(height, width);
        setLayout(new GridLayout(height, width));
        setBorder(new TitledBorder(name));
        matrixField = new JTextField[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                JTextField temp = new JTextField("0");
                matrixField[i][j] = temp;
                if (editable) {
                    temp.addFocusListener(focusManager);
                    temp.getDocument().addDocumentListener(new TextFieldMonitor(i, j));
                    temp.addActionListener(this::onEnterPressed);
                }else{
                    temp.setEditable(false);
                    temp.setFocusable(false);
                }
                add(temp);
            }
        }
    }

    public void setMatrix(Matrix matrix){
        this.matrix = matrix;
        updateMatrixPane();
    }

    // there used to be a horizontally oriented matrix. This should not be used now
    @Deprecated
    public MatrixPane setInverseRowsAndColumns(){
        //assert matrix.width == matrix.height;
        //inverseRowsAndColumns = true;
        return this;
    }

    public void onEnterPressed(ActionEvent e){
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        parent.refreshResult();
    }

    public void updateMatrixPane(){
        for (int i = 0; i < matrixField.length; i++) {
            for (int j = 0; j < matrixField[0].length; j++) {
                matrixField[i][j].setText(formatter.format(inverseRowsAndColumns? matrix.entries[i][j] : matrix.entries[j][i]));
            }
        }
    }

    @Override
    public void resetPane(){
        matrix = new Matrix(matrixField.length, matrixField[0].length);
        updateMatrixPane();
    }

    public void handleFieldChange(JTextField field, int i, int j){
        double val;
        try{
            val = Double.parseDouble(field.getText());
            if (field.getBackground().equals(Color.red)) field.setBackground(Color.white);
        }catch (Exception ex){
            Calculator.logLevel = Calculator.LogLevel.WARNING;
            val = Calculator.evaluate(field.getText(), false);
            if (Calculator.getLog().isBlank()){
                if (field.getBackground().equals(Color.red)) field.setBackground(Color.white);
            }else{
                field.setBackground(Color.red);
                repaint();
                return;
            }
        }

        if(inverseRowsAndColumns) matrix.entries[i][j] = val;
        else matrix.entries[j][i] = val;
    }

    public class TextFieldMonitor implements DocumentListener {
        public int i, j;
        public TextFieldMonitor(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public void insertUpdate(DocumentEvent e) {
            handleFieldChange(matrixField[i][j], i, j);
        }

        public void removeUpdate(DocumentEvent e) {
            handleFieldChange(matrixField[i][j], i, j);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }
}
