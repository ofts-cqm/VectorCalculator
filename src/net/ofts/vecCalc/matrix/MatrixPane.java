package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.vector.BlankPane;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MatrixPane extends BlankPane {
    public Matrix matrix;
    public JTextField[][] matrixField;

    public MatrixPane(String name, int size){
        matrix = new Matrix(size);
        setLayout(new GridLayout(size, size));
        setBorder(new TitledBorder(name));
        matrixField = new JTextField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JTextField temp = new JTextField("0");
                matrixField[i][j] = temp;
                temp.addFocusListener(focusManager);
                temp.getDocument().addDocumentListener(new TextFieldMonitor(i, j));
                add(temp);
            }
        }
    }

    public void updateMatrixPane(){
        for (int i = 0; i < matrixField.length; i++) {
            for (int j = 0; j < matrixField.length; j++) {
                matrixField[i][j].setText(formatter.format(matrix.values[i][j]));
            }
        }
    }

    public void handleFieldChange(JTextField field, int i, int j){
        double val;
        try{
            val = Double.parseDouble(field.getText());
            if (field.getBackground().equals(Color.red)) field.setBackground(Color.white);
        }catch (Exception ex){
            field.setBackground(Color.red);
            repaint();
            return;
        }

        matrix.values[i][j] = val;
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
