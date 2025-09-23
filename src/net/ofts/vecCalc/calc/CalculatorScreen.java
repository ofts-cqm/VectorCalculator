package net.ofts.vecCalc.calc;

import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.history.HistoryItem;
import net.ofts.vecCalc.numberPane.NumPane;
import net.ofts.vecCalc.numberPane.TextPane;

import javax.swing.*;
import java.awt.*;

public class CalculatorScreen extends ICalculatorScreen {
    public JTextField textField = new JTextField();
    public JTextField result = new JTextField("Press 'Enter' or '=' to evaluate");
    public KeyboardPane keyboard = new KeyboardPane(this::receiveInput, this::clearInput,  () -> refreshResult(true), () -> textField.getText());

    public CalculatorScreen(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());
        add(textField);
        add(Box.createVerticalGlue());
        add(result);
        add(Box.createVerticalGlue());
        add(keyboard);
        textField.addActionListener(e -> refreshResult(true));
        textField.setPreferredSize(new Dimension(500, 75));
        result.setPreferredSize(new Dimension(500, 25));
        result.setEditable(false);
    }

    public void receiveInput(String input){
        textField.setText(textField.getText() + input);
    }

    public void clearInput(){
        textField.setText("");
    }

    @Override
    public void refreshResult(boolean recordResult) {
        String preText = textField.getText();
        double evaluated = Calculator.evaluate(textField.getText(), true);
        if (Calculator.getLog().isEmpty()){
            result.setText("= " + evaluated);
            if (recordResult) HistoryItem.recordHistory("calc1", new TextPane(preText + " = " + evaluated), new NumPane("", this, false).setNum(evaluated));
            return;
        }

        JFrame errorFrame = new JFrame("Error");
        JList<String> errorList = new JList<>(Calculator.getLog().split("\n"));
        errorList.setForeground(Color.red);
        errorFrame.setContentPane(new JScrollPane(errorList));
        errorFrame.setSize(200, 150);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        errorFrame.setLocation((screenSize.width - errorFrame.getWidth()) / 2, (screenSize.height - errorFrame.getHeight()) / 2);
        errorFrame.setAlwaysOnTop(true);
        errorFrame.setVisible(true);
    }

    @Override
    public void onPageOpened(JFrame frame) {
        Calculator.logLevel = Calculator.LogLevel.ERROR;
        frame.setSize(540, 380);
        frame.setTitle("Calculator (Scientific)");
    }

    @Override
    public GenericPane getPaneByIndex(int index) {
        return new HolderPane(this);
    }

    @Override
    public String getOperationName(String opcode) {
        return "Calculate";
    }
}
