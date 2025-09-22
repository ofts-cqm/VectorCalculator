package net.ofts.vecCalc;

import net.ofts.vecCalc.numberPane.MatrixPane;
import net.ofts.vecCalc.numberPane.MatrixWithSizeBarPane;
import net.ofts.vecCalc.numberPane.AbstractNumberPane;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class GenericPane extends AbstractNumberPane {
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

    public AbstractNumberPane getCurrent(){
        return current;
    }

    public void setOverrideSize(Dimension size){
        overrideSize = size;
        current.setPreferredSize(overrideSize);
        doOverrideSize = true;
    }

    public <T extends AbstractNumberPane> T getPanel(Class<T> type){
        return (T)panels.get(type);
    }

    public <T extends AbstractNumberPane> void setPanel(T panel){
        panels.replace(panel.getClass(), panel);
        if(panel.getClass().equals(MatrixWithSizeBarPane.class)){
            panels.replace(MatrixPane.class, panel);
        }else if(panel.getClass().equals(MatrixPane.class)){
            panels.replace(MatrixWithSizeBarPane.class, panel);
        }

        if (current.getClass().equals(panel.getClass())){
            displayPanel(current.getClass());
        }
    }

    public <T extends AbstractNumberPane> void displayPanel(Class<T> type){
        if(current.equals(panels.get(type))) return;
        remove(current);
        current.resetPane();
        current = panels.get(type);
        if (doOverrideSize){
            current.setPreferredSize(overrideSize);
        }
        add(current);
        revalidate();
        repaint();
    }

    @Override
    public void resetPane() {
        getCurrent().resetPane();
    }

    @Override
    public AbstractNumberPane cloneWithValue(INumber number) {
        return getCurrent().cloneWithValue(number);
    }

    @Override
    public INumber getNumber() {
        return getCurrent().getNumber();
    }
}
