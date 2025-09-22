package net.ofts.vecCalc;

import net.ofts.vecCalc.calc.Calculator;
import net.ofts.vecCalc.calc.CalculatorScreen;
import net.ofts.vecCalc.matrix.FunctionSolvingScreen;
import net.ofts.vecCalc.matrix.MatrixCalcScreen;
import net.ofts.vecCalc.span.SpanSetScreen;
import net.ofts.vecCalc.vector.Vec3Screen;
import net.ofts.vecCalc.vector.VecNScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class Main {
    public static Hashtable<String, ICalculatorScreen> calculatorCodeMap;
    public static Hashtable<String, JMenuItem> menuItemMap = new Hashtable<>();
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
        SpanSetScreen tmp = new SpanSetScreen();
        calculatorCodeMap.put("span", tmp);
        calculatorCodeMap.put("span0", tmp);
        calculatorCodeMap.put("span1", tmp);
        calculatorCodeMap.put("calc", new CalculatorScreen());

        frame.add(current);
        current.onPageOpened(frame);
        Calculator.setUp();
    }

    public static void setUpMenuBar(){
        menuBar = new JMenuBar();
        JMenu vector = new JMenu("Vector");
        MenuListener listener = new MenuListener();

        JMenu vec3 = new JMenu("Vector 3");
        vec3.addActionListener(listener);
        vec3.setActionCommand("vec3");
        Vec3Screen.addMenuItem(vec3);
        vector.add(vec3);

        JMenu vecN = new JMenu("Vector N");
        vecN.addActionListener(listener);
        vecN.setActionCommand("vecN");
        VecNScreen.addMenuItem(vecN);
        vector.add(vecN);

        JMenu matrix = new JMenu("Matrix");

        JMenuItem func = new JMenuItem("Function Solving");
        func.addActionListener(listener);
        func.setActionCommand("func");
        menuItemMap.put("func", func);
        matrix.add(func);

        JMenu matx = new JMenu("Matrix Calculation");
        matx.addActionListener(listener);
        matx.setActionCommand("matx");
        MatrixCalcScreen.addMenuItem(matx);
        matrix.add(matx);

        JMenu span = new JMenu("Span");

        JMenuItem base = new JMenuItem("Find Base");
        base.addActionListener(Main::operationListener);
        base.setActionCommand("span0");
        menuItemMap.put("span0", base);
        span.add(base);

        JMenuItem isIn = new JMenuItem("Is Vector In Subspace");
        isIn.addActionListener(Main::operationListener);
        isIn.setActionCommand("span1");
        menuItemMap.put("span1", isIn);
        span.add(isIn);

        JMenuItem calc = new JMenuItem("Calculator");
        calc.addActionListener(Main::operationListener);
        calc.setActionCommand("calc1");
        menuItemMap.put("calc1", calc);

        menuBar.add(vector);
        menuBar.add(matrix);
        menuBar.add(span);
        menuBar.add(calc);
        currentMenu = vec3.getItem(0);
        currentMenu.setEnabled(false);
    }

    public static void operationListener(ActionEvent e){
        openCalculatorPage(e.getActionCommand(), (JMenuItem) e.getSource());
    }

    public static void openCalculatorPage(String operationCode, JMenuItem source){
        int operator = operationCode.charAt(4) - 48;
        ICalculatorScreen calc = calculatorCodeMap.get(operationCode.substring(0, 4));
        if (calc != current){
            setCalculator(calc, source);
        }else{
            currentMenu.setEnabled(true);
            currentMenu = source;
            currentMenu.setEnabled(false);
        }
        if (calc instanceof IMultipleOperation mul) mul.setOperation(operator);
    }

    public static void updateSelectedMenuItem(JMenuItem item){
        currentMenu.setEnabled(true);
        currentMenu = item;
        currentMenu.setEnabled(false);
    }

    public static void setCalculator(ICalculatorScreen screen, JMenuItem source){
        frame.remove(current);
        current = screen;
        frame.add(current);
        current.onPageOpened(frame);
        current.repaint();
        currentMenu.setEnabled(true);
        currentMenu = source;
        currentMenu.setEnabled(false);
        frame.revalidate();
        frame.repaint();
    }

    public static class MenuListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            setCalculator(calculatorCodeMap.get(e.getActionCommand()), (JMenuItem) e.getSource());
        }
    }
}
