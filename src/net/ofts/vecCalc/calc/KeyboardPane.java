package net.ofts.vecCalc.calc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class KeyboardPane extends JPanel {
    public Consumer<String> inputStream;
    public Runnable clear, evaluate;

    public static final String[][] layout = new String[][]{
            {"|x|",  "√x",   "∛x",   "(", ")", "^", "+", "clear"},
            {"sin",  "cos",  "tan",  "1", "2", "3", "-", "Rad"},
            {"csc",  "sec",  "cot",  "4", "5", "6", "*", "mod"},
            {"asin", "acos", "atan", "7", "8", "9", "/", "ans"},
            {"log",  "ln",   "e",    "π", "0", ".", "EXP", "="}
    };

    public KeyboardPane(Consumer<String> inputStream, Runnable clear, Runnable evaluate){
        this.inputStream = inputStream;
        this.clear = clear;
        this.evaluate = evaluate;
        setLayout(new GridLayout(5, 8));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton(layout[i][j]);
                button.setActionCommand(layout[i][j]);
                button.addActionListener(this::onButtonPressed);
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(button);
            }
        }
    }

    public void onButtonPressed(ActionEvent e){
        String value = switch (e.getActionCommand()){
            case "|x|" -> "abs";
            case "√x" -> "sqrt";
            case "∛x" -> "cbrt";
            case "clear" -> {
                clear.run();
                yield  "";
            }
            case "Deg" -> {
                Calculator.isRadian = true;
                ((JButton)e.getSource()).setText("Rad");
                yield "";
            }
            case "Rad" -> {
                Calculator.isRadian = false;
                ((JButton)e.getSource()).setText("Deg");
                yield "";
            }
            case "=" -> {
                evaluate.run();
                yield "";
            }
            case "EXP" -> "E";
            default -> e.getActionCommand();
        };
        if (!value.isEmpty()) inputStream.accept(value);
    }
}
