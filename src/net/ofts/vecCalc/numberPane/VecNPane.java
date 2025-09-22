package net.ofts.vecCalc.numberPane;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.calc.Calculator;
import net.ofts.vecCalc.vector.VecN;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class VecNPane extends BlankPane {
    public ICalculatorScreen parent;
    public VecN vector;
    public JTextField[] fields;
    public boolean editable;
    public int size;
    public String name;

    public VecNPane(String name, int size, ICalculatorScreen parent, boolean editable){
        super();
        this.size = size;
        this.editable = editable;
        this.name = name;
        this.setBorder(new TitledBorder(name));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.parent = parent;

        vector = new VecN(size);
        fields = new JTextField[size];

        for (int i = 0; i < size; i++) {
            JTextField temp = new JTextField("0");
            temp.setMaximumSize(new Dimension(75, 20));

            if (editable) {
                temp.addFocusListener(focusManager);
                temp.getDocument().addDocumentListener(new VecNTextFieldMonitor(i));
                temp.addActionListener(this::onEnterPressed);
            }else{
                temp.setEditable(false);
                temp.setFocusable(false);
            }

            fields[i] = temp;
            add(Box.createVerticalGlue());
            add(temp);
        }
        add(Box.createVerticalGlue());
    }

    public void onEnterPressed(ActionEvent e){
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        parent.refreshResult(true);
    }

    public VecNPane setVector(VecN vector){
        this.vector = vector;
        for (int i = 0; i < size; i++) {
            fields[i].setText(formatter.format(vector.elements[i]));
        }
        repaint();
        return this;
    }

    public VecNPane setVectorPreserveLength(VecN vector){
        if (vector.elements.length == size){
            setVector(vector);
        } else {
            VecN vecn = new VecN(size);
            vecn.elements = Arrays.copyOf(vector.elements, size);
            setVector(vecn);
        }
        return this;
    }

    public void resetVector(){
        setVector(new VecN(size));
    }

    @Override
    public void resetPane(){
        resetVector();
    }

    public void handleVecNChange(JTextField field, int index){
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

        vector.elements[index] = val;
    }

    public class VecNTextFieldMonitor implements DocumentListener {
        public int index;
        public VecNTextFieldMonitor(int index) {
            this.index = index;
        }

        public void insertUpdate(DocumentEvent e) {
            handleVecNChange(fields[index], index);
        }

        public void removeUpdate(DocumentEvent e) {
            handleVecNChange(fields[index], index);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }

    public AbstractNumberPane cloneWithValue(INumber number){
        assert number instanceof VecN;
        VecN vec = (VecN) number;
        return new VecNPane(this.name, vec.elements.length, this.parent, this.editable).setVector(vec);
    }

    @Override
    public INumber getNumber() {
        return vector;
    }
}
