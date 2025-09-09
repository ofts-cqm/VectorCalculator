package net.ofts.vecCalc.matrix.rref;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.VecN;
import net.ofts.vecCalc.vector.VecNPane;

import javax.swing.*;

import java.awt.*;

import static net.ofts.vecCalc.matrix.rref.AugmentedMatrix.isZero;

public class FunctionAnalyzer extends ICalculatorScreen {
    public Matrix solvedMatrix;
    public VecN solvedAnswer;
    public int height;
    public int width;

    public static final char[] variableList = new char[]{'s', 't', 'u', 'v', 'a', 'b', 'c', 'd', 'e', 'f'};

    public FunctionAnalyzer(Matrix mat, VecN answer){
        this.solvedAnswer = answer;
        this.solvedMatrix = mat;
        this.height = Math.min(mat.height, mat.width);
        this.width = mat.width;
    }

    public Matrix analyzeSolution(){
        int number_variables = 0;
        int last_exist = -1;
        int[] variable = new int[width];

        lp: for (int i = 0; i < width; i++) {
            if (i < height) for (int j = last_exist + 1; j < width; j++) {
                if (!isZero(solvedMatrix.entries[j][i])) {
                    variable[j] = -1;
                    last_exist = j;
                    continue lp;
                }
            }
            number_variables = width - i;
            break;
        }

        Matrix answerMatrix = new Matrix(width, number_variables + 1);
        //answerMatrix.entries[0] = VecN.scale(solvedAnswer, -1).elements;

        int assignedVariable = 0;
        for (int i = 0; i < width; i++) {
            if (variable[i] == 0) {
                variable[i] = ++assignedVariable;
                answerMatrix.entries[assignedVariable][i] = 1;
            }
        }

        for (int i = 0; i < solvedMatrix.height/*number_variables*/; i++) {
            int pivot = -1;

            for (int j = 0; j < width; j++) {
                if (!isZero(solvedMatrix.entries[j][i])){
                    pivot = j;
                    break;
                }
            }

            if (pivot == -1) break;

            for (int j = pivot + 1; j < width; j++) {
                if (!isZero(solvedMatrix.entries[j][i])){
                    answerMatrix.entries[variable[j]][pivot] = -solvedMatrix.entries[j][i];
                }
            }
            answerMatrix.entries[0][pivot] = -solvedAnswer.elements[i];
        }

        return answerMatrix;
    }

    public void analyzeAndDisplay(){
        Matrix answerMatrix = analyzeSolution();
        int assignedVariable = answerMatrix.width - 1;

        JFrame frame = new JFrame("Function Solutions");
        frame.setSize(Math.max((assignedVariable + 1) * 150, 400), width * 35 + 75);
        frame.setAlwaysOnTop(true);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
        frame.setVisible(true);

        JPanel vectorPane = new JPanel(new FlowLayout());
        vectorPane.setSize((assignedVariable + 1) * 150, width * 35 + 75);

        vectorPane.add(new JLabel("X = "));
        for (int i = 0; i < assignedVariable + 1; i++) {
            VecNPane pane = new VecNPane(i == 0 ? "Const" : (variableList[i - 1] + ""), height, this, false);
            pane.setVector(answerMatrix.getColumn(i));
            pane.setSize(100, width * 27 + 20);
            pane.setPreferredSize(new Dimension(100, width * 35 + 25));
            vectorPane.add(pane);
            if (i < assignedVariable){
                vectorPane.add(new JLabel(" + " + variableList[i] + " "));
            }
        }

        vectorPane.repaint();
        vectorPane.revalidate();
        frame.add(vectorPane, BorderLayout.CENTER);
        JLabel label = new JLabel("There are infinity number of solutions. The Solution Set is:", SwingConstants.CENTER);
        label.setForeground(Color.red);
        frame.add(label, BorderLayout.NORTH);
    }

    @Override
    public void refreshResult() {

    }

    @Override
    public void onPageOpened(JFrame frame) {

    }
}
