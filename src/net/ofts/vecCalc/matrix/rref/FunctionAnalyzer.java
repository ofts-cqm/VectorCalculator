package net.ofts.vecCalc.matrix.rref;

import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.VecN;
import net.ofts.vecCalc.numberPane.VecNPane;

import javax.swing.*;

import java.awt.*;

import static net.ofts.vecCalc.matrix.rref.AugmentedMatrix.isZero;

/**
 * This class IS NOT A ICalculatorScreen.
 * it extends ICalculatorScreen because during the process of analysis,
 * we need to create some panes to represent the intermediate numbers.
 * However, all panes requires a parent. Therefore, we need a place-holder parents
 * for all panes created to store intermediate numbers
 */
public class FunctionAnalyzer extends ICalculatorScreen {
    public Matrix solvedMatrix;
    public VecN solvedAnswer;
    public int height;
    public int width;

    public static final char[] variableList = new char[]{'s', 't', 'u', 'v', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * This is used AFTER the function is solved (AKA, RREF).
     * This does not solve function. Instead, it analyzes the solved RREF form of a function set
     * For example, it converts matrix to a list of vector in a form where
     * the answer of the function set x = v1 + s * v2 + t * v3 ......
     *
     * @param mat the SOLVED matrix in rref form
     * @param answer the vector solution of this function
     */
    public FunctionAnalyzer(Matrix mat, VecN answer){
        this.solvedAnswer = answer;
        this.solvedMatrix = mat;
        this.height = Math.min(mat.height, mat.width);
        this.width = mat.width;
    }

    /**
     * This analyzes the function set. It returns a matrix A where A=[v1, v2, ... vn].
     * v1, v2 ... vn are the same as the described in the constructor's documentation.
     * @return A where A=[v1, v2, ... vn]
     */
    public Matrix analyzeSolution(){
        int number_variables = 0;
        int last_exist = -1;
        int[] variable = new int[width];

        // for each variable, we want to know if it exists as a pivot point.
        // "variable" is used to store all function's variable's type.
        // "-1" simply means it's a pivot points
        // here, we search from up to down
        // to make sure we cover all variables, still use width as boundary
        lp: for (int i = 0; i < width; i++) {
            // scan from left to right
            if (i < height) for (int j = last_exist + 1; j < width; j++) {
                // the first number we meet is the pivot entry
                if (!isZero(solvedMatrix.entries[j][i])) {
                    variable[j] = -1; // record it as pivot entry
                    last_exist = j; // all pivot entries are on the right side of this, so start from here
                    continue lp; // search next row
                }
            }
            // if we cannot find a pivot entry, then this row and all below are full zero
            // we can record number of variables now.
            number_variables = width - i;
            break;
        }

        // create the analyzed matrix, since we already know how many free variables we have
        Matrix answerMatrix = new Matrix(width, number_variables + 1);
        //answerMatrix.entries[0] = VecN.scale(solvedAnswer, -1).elements;

        // here, we assign index to all free variables
        int assignedVariable = 0;
        for (int i = 0; i < width; i++) {
            if (variable[i] == 0) { // if a variable does not have pivot entry, it is free
                variable[i] = ++assignedVariable;// assign new index to this entry
                answerMatrix.entries[assignedVariable][i] = 1;// set this variable's entry to 1
            }
        }

        // now, we iterate through the matrix to fill the answer matrix.
        // we search from up to down
        for (int i = 0; i < solvedMatrix.height/*number_variables*/; i++) {
            int pivot = -1;

            // first try to find a pivot entry
            for (int j = 0; j < width; j++) {
                if (!isZero(solvedMatrix.entries[j][i])){
                    pivot = j;
                    break;
                }
            }

            // cannot find? congratulation! we reached the end!
            if (pivot == -1) break;

            // pivot entry are not important in this case because we are defining them
            // start from its right
            for (int j = pivot + 1; j < width; j++) {
                // if we find a non-zero entry
                if (!isZero(solvedMatrix.entries[j][i])){
                    // we use variable[j] to find this entry's related free-variable
                    // and set this free-variable's value as the negative of the solved matrix's
                    // negative because we implicitly moved the answer from right-hand-side
                    // of the equal sign to the left-hand-side, so negative
                    answerMatrix.entries[variable[j]][pivot] = -solvedMatrix.entries[j][i];
                }
            }
            // don't forget to assign the trivial solution
            answerMatrix.entries[0][pivot] = -solvedAnswer.elements[i];
        }

        return answerMatrix;
    }

    /**
     * This analysis the function's solution just like {@code this::analyzeSolution}.
     * More over, it displays the analyzed result on a new pop-up JFrame
     */
    public void analyzeAndDisplay(){
        Matrix answerMatrix = analyzeSolution();
        int assignedVariable = answerMatrix.width - 1;

        // setup JFrame
        JFrame frame = new JFrame("Function Solutions");
        frame.setSize(Math.max((assignedVariable + 1) * 150, 400), width * 35 + 75);
        frame.setAlwaysOnTop(true);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
        frame.setVisible(true);

        // setup vector panels
        JPanel vectorPane = new JPanel(new FlowLayout());
        vectorPane.setSize((assignedVariable + 1) * 150, width * 35 + 75);

        // adding vectors to JFrame
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

        // finish up
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

    @Override
    public GenericPane getPaneByIndex(int index) {
        return null;
    }

    @Override
    public String getOperationName(String opcode) {
        return "";
    }
}
