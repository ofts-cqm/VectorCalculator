package net.ofts.vecCalc.calc;

import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.numberPane.AbstractNumberPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class KeyboardPane extends AbstractNumberPane {
    public Consumer<String> inputStream;
    public Runnable clear, evaluate;
    public Supplier<String> getInput;
    public boolean hyperbolaForm = false;

    public static final String[][] layout = new String[][]{
            {"|x|",  "√x",   "∛x",   "(", ")", "^", "+", "clear"},
            {"sin",  "cos",  "tan",  "1", "2", "3", "-", "Rad"},
            {"csc",  "sec",  "cot",  "4", "5", "6", "*", "Alt"},
            {"asin", "acos", "atan", "7", "8", "9", "/", "ans"},
            {"log",  "ln",   "e",    "π", "0", ".", "EXP", "="}
    };

    public static final JButton[][] buttons = new JButton[layout.length][layout[0].length];

    public KeyboardPane(Consumer<String> inputStream, Runnable clear, Runnable evaluate, Supplier<String> getInput){
        this.inputStream = inputStream;
        this.clear = clear;
        this.getInput = getInput;
        this.evaluate = evaluate;
        setLayout(new GridLayout(5, 8));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton(layout[i][j]);
                button.setActionCommand(layout[i][j]);
                button.addActionListener(this::onButtonPressed);
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(button);
                buttons[i][j] = button;
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
            case "Alt" -> {
                hyperbolaForm = !hyperbolaForm;
                if (hyperbolaForm){
                    for (int i = 1; i < 4; i++) {
                        for (int j = 0; j < 3; j++) {
                            buttons[i][j].setText(buttons[i][j].getText() + "h");
                            buttons[i][j].setActionCommand(buttons[i][j].getText());
                        }
                    }
                }else{
                    for (int i = 1; i < 4; i++) {
                        for (int j = 0; j < 3; j++) {
                            buttons[i][j].setText(buttons[i][j].getText().substring(0, buttons[i][j].getText().length() - 1));
                            buttons[i][j].setActionCommand(buttons[i][j].getText());
                        }
                    }
                }
                yield "";
            }
            case "EXP" -> "E";
            default -> e.getActionCommand();
        };
        if (!value.isEmpty()) inputStream.accept(value);
    }

    @Override
    public void resetPane() {
        clear.run();
    }

    @Override
    public AbstractNumberPane cloneWithValue(INumber number) {
        assert number instanceof TextNumber;
        String input = ((TextNumber) number).text;
        clear.run();
        inputStream.accept(input);
        return new KeyboardPane(inputStream, clear, evaluate, getInput);
    }

    @Override
    public INumber getNumber() {
        return new TextNumber(getInput.get());
    }
}
