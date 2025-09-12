package net.ofts.vecCalc.calc;

import net.ofts.vecCalc.ICalculatorScreen;

import javax.swing.*;
import java.awt.*;

public class CalculatorScreen extends ICalculatorScreen {
    public KeyboardPane keyboard = new KeyboardPane(this::receiveInput, this::clearInput, this::refreshResult);
    public JTextField textField = new JTextField();

    public CalculatorScreen(){
        setLayout(new BorderLayout(50, 50));
        add(Box.createRigidArea(new Dimension(500, 0)), BorderLayout.NORTH);
        add(textField, BorderLayout.CENTER);
        add(keyboard, BorderLayout.SOUTH);
        textField.addActionListener(e -> refreshResult());
        textField.setPreferredSize(new Dimension(500, 100));
    }

    public void receiveInput(String input){
        textField.setText(textField.getText() + input);
    }

    public void clearInput(){
        textField.setText("");
    }

    @Override
    public void refreshResult() {
        double evaluated = Calculator.evaluate(textField.getText(), true);
        if (Calculator.getLog().isEmpty()){
            textField.setText(evaluated + "");
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
        frame.setSize(540, 380);
        frame.setTitle("Calculator (Scientific)");
    }
}
