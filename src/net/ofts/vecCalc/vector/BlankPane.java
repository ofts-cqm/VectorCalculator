package net.ofts.vecCalc.vector;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

public class BlankPane extends JPanel {
    public static TextFieldFocusMng focusManager = new TextFieldFocusMng();
    public static NumberFormat formatter = NumberFormat.getNumberInstance();

    public BlankPane(){
        this.setPreferredSize(new Dimension(100, 300));
        this.setSize(100, 300);
    }

    public void handleChange(JTextField field){

    }

    public static class TextFieldFocusMng implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            if (e.getSource() instanceof JTextField field){
                field.selectAll();
            }
        }

        @Override
        public void focusLost(FocusEvent e) {

        }
    }

    public class TextFieldMonitor implements DocumentListener {
        public JTextField textField;

        public TextFieldMonitor(JTextField parent){
            textField = parent;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            handleChange(textField);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            handleChange(textField);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }

    static {
        formatter.setMaximumFractionDigits(6);
    }
}
