package net.ofts.vecCalc.matrix;

import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.vector.VecNPane;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.util.function.BiConsumer;

public class MatrixSizerPane extends JPanel {
    public GenericPane associatedVector;
    public JSlider height = new JSlider(SwingConstants.HORIZONTAL, 1, 10, 3);
    public JSlider width = new JSlider(SwingConstants.HORIZONTAL, 1, 10, 3);

    public BiConsumer<Integer, Integer> onDimensionChanged;

    public MatrixSizerPane(MatrixPane pane, BiConsumer<Integer, Integer> onDimensionChanged){
        setBorder(new TitledBorder("Config"));
        this.onDimensionChanged = onDimensionChanged;
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
        onDimensionChanged.accept(height.getValue(), width.getValue());
        if(e.getSource().equals(width)) {
            if (associatedVector != null){
                VecNPane oldPane = associatedVector.getPanel(VecNPane.class);
                associatedVector.setPanel(new VecNPane(oldPane.name, width.getValue(), oldPane.parent, oldPane.editable));
            }
        }
    }
}
