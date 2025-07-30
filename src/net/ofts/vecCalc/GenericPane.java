package net.ofts.vecCalc;

import net.ofts.vecCalc.matrix.MatrixPane;
import net.ofts.vecCalc.matrix.MatrixWithSizeBarPane;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class GenericPane extends JPanel {
    private final Hashtable<Class<? extends AbstractNumberPane>, AbstractNumberPane> panels = new Hashtable<>();
    private AbstractNumberPane current;
    private Dimension overrideSize = null;
    private boolean doOverrideSize = false;

    public GenericPane(AbstractNumberPane... panel){
        setLayout(new BorderLayout());
        for (AbstractNumberPane pane : panel){
            panels.put(pane.getClass(), pane);
            if(pane.getClass().equals(MatrixWithSizeBarPane.class)){
                panels.put(MatrixPane.class, pane);
            }
        }
        add(panel[0], BorderLayout.CENTER);
        current = panel[0];
    }


    public void setOverrideSize(Dimension size){
        overrideSize = size;
        current.setPreferredSize(overrideSize);
        doOverrideSize = true;
    }

    public <T extends AbstractNumberPane> T getPanel(Class<T> type){
        return (T)panels.get(type);
    }

    public <T extends AbstractNumberPane> void setPanel(Class<T> type, T panel){
        panels.replace(type, panel);
    }

    public <T extends AbstractNumberPane> void displayPanel(Class<T> type){
        remove(current);
        current = panels.get(type);
        current.resetPane();
        if (doOverrideSize){
            current.setPreferredSize(overrideSize);
        }
        add(current);
        revalidate();
        repaint();
    }
}
