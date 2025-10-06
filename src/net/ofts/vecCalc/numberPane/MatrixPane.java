package net.ofts.vecCalc.numberPane;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.ValueDisplayer;
import net.ofts.vecCalc.calc.Calculator;
import net.ofts.vecCalc.matrix.Matrix;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MatrixPane extends BlankPane {
    public Matrix matrix;
    public JTextField[][] matrixField;
    public JButton[][] resultField;
    public ICalculatorScreen parent;
    public String name;
    public boolean editable;
    @Deprecated
    public boolean inverseRowsAndColumns = false;
    public int matWidth, matHeight;

    public MatrixPane(String name, Matrix matrix, ICalculatorScreen parent){
        this(name, matrix.height, matrix.width, parent, false);
        this.matWidth = matrix.width;
        this.matHeight = matrix.height;
        setMatrix(matrix);
    }

    public MatrixPane(String name, int height, int width, ICalculatorScreen parent, boolean editable){
        this.parent = parent;
        this.name = name;
        this.editable = editable;
        matrix = new Matrix(height, width);
        setLayout(new GridLayout(height, width));
        setBorder(new TitledBorder(name));
        matrixField = new JTextField[height][width];
        resultField = new JButton[height][width];
        this.matWidth = width;
        this.matHeight = height;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (editable) {
                    JTextField temp = new JTextField("0");
                    matrixField[i][j] = temp;
                    temp.addFocusListener(focusManager);
                    temp.getDocument().addDocumentListener(new TextFieldMonitor(i, j));
                    temp.addActionListener(this::onEnterPressed);
                    add(temp);
                }else{
                    JButton temp = new JButton("0");
                    resultField[i][j] = temp;
                    temp.addMouseListener(new MouseListener(temp));
                    temp.setFocusable(false);
                    add(temp);
                }
            }
        }
    }

    public void setMatrix(Matrix matrix){
        this.matrix = matrix;
        updateMatrixPane();
    }

    public void setMatrixPreserveSize(Matrix matrix){
        this.matrix.setMatrixPreserveSize(matrix);
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
        parent.refreshResult(true);
    }

    public void updateMatrixPane(){
        for (int i = 0; i < matHeight; i++) {
            for (int j = 0; j < matWidth; j++) {
                String text = formatter.format(/*inverseRowsAndColumns? matrix.entries[i][j] : */matrix.entries[j][i]);
                if (editable) matrixField[i][j].setText(text);
                else resultField[i][j].setText(text);
            }
        }
    }

    @Override
    public void resetPane(){
        matrix = new Matrix(matHeight, matWidth);
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

        //if(inverseRowsAndColumns) matrix.entries[i][j] = val;
        //else
        matrix.entries[j][i] = val;
    }

    public AbstractNumberPane cloneWithValue(INumber number){
        assert number instanceof Matrix;
        Matrix matrix = (Matrix) number;
        MatrixPane pane = new MatrixPane(this.name, matrix.height, matrix.width, this.parent, this.editable);
        pane.setMatrix(matrix);
        return pane;
    }

    @Override
    public INumber getNumber() {
        return matrix;
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

    public static class MouseListener extends MouseAdapter {
        public JButton button;

        public MouseListener(JButton button){
            this.button = button;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            ValueDisplayer.displayValue(button.getText(), button.getLocationOnScreen());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ValueDisplayer.closeDisplay();
        }
    }
}
