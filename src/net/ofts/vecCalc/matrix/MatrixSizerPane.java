package net.ofts.vecCalc.matrix;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class MatrixSizerPane extends JPanel {
    public MatrixPane matrix;
    public JSlider height = new JSlider(SwingConstants.HORIZONTAL, 2, 10, 3);
    public JSlider width = new JSlider(SwingConstants.HORIZONTAL, 2, 10, 3);

    public MatrixSizerPane(MatrixPane pane){
        setBorder(new TitledBorder("Config"));
        this.matrix = pane;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("m:"));
        height.setPaintTicks(true);
        height.setPaintLabels(true);
        height.setMajorTickSpacing(1);
        height.setFocusable(false);
        height.addChangeListener(this::onDimensionChanged);
        add(height);

        add(new JLabel("n:"));
        width.setPaintTicks(true);
        width.setPaintLabels(true);
        width.setMajorTickSpacing(1);
        width.setFocusable(false);
        width.addChangeListener(this::onDimensionChanged);
        add(width);
    }

    public void onDimensionChanged(ChangeEvent e){

    }
}
