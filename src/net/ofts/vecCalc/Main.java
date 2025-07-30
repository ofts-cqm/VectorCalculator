package net.ofts.vecCalc;

import net.ofts.vecCalc.matrix.FunctionSolvingScreen;
import net.ofts.vecCalc.matrix.MatrixCalcScreen;
import net.ofts.vecCalc.vector.Vec3Screen;
import net.ofts.vecCalc.vector.VecNScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class Main {
    public static Hashtable<String, ICalculatorScreen> calculatorCodeMap;
    public static ICalculatorScreen current;
    public static JMenuBar menuBar;
    public static JMenuItem currentMenu;
    public static JFrame frame;

    public static void main(String[] args){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        setUpMenuBar();
        frame.setJMenuBar(Main.menuBar);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)(screenSize.width * 0.6), screenSize.height / 2);
        frame.setVisible(true);

        calculatorCodeMap = new Hashtable<>();
        calculatorCodeMap.put("vec3", current = new Vec3Screen());
        calculatorCodeMap.put("vecN", new VecNScreen());
        calculatorCodeMap.put("func", new FunctionSolvingScreen());
        calculatorCodeMap.put("matx", new MatrixCalcScreen());

        frame.add(current);
        current.onPageOpened(frame);
    }

    public static void setUpMenuBar(){
        menuBar = new JMenuBar();
        JMenu vector = new JMenu("Vector");
        MenuListener listener = new MenuListener();

        JMenuItem vec3 = new JMenuItem("Vector 3");
        vec3.addActionListener(listener);
        vec3.setActionCommand("vec3");
        vector.add(vec3);

        JMenuItem vecN = new JMenuItem("Vector N");
        vecN.addActionListener(listener);
        vecN.setActionCommand("vecN");
        vector.add(vecN);

        JMenu matrix = new JMenu("Matrix");

        JMenuItem func = new JMenuItem("Function Solving");
        func.addActionListener(listener);
        func.setActionCommand("func");
        matrix.add(func);

        JMenuItem matx = new JMenuItem("Matrix Calculation");
        matx.addActionListener(listener);
        matx.setActionCommand("matx");
        matrix.add(matx);

        menuBar.add(vector);
        menuBar.add(matrix);
        currentMenu = vec3;
        currentMenu.setEnabled(false);
    }

    public static class MenuListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(current);
            current = calculatorCodeMap.get(e.getActionCommand());
            frame.add(current);
            current.onPageOpened(frame);
            current.repaint();
            currentMenu.setEnabled(true);
            currentMenu = (JMenuItem) e.getSource();
            currentMenu.setEnabled(false);
            frame.revalidate();
            frame.repaint();
        }
    }
}
