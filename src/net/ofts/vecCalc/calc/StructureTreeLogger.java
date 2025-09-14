package net.ofts.vecCalc.calc;

import java.util.ArrayList;

public class StructureTreeLogger {
    private static int indentation = 0;
    private static final ArrayList<Boolean> indentationRule = new ArrayList<>();

    public static void push() {
        indentation++;
        indentationRule.add(true);
    }

    public static void pop() {
        indentation--;
        indentationRule.remove(indentationRule.size() - 1);
    }

    public static void write(String structure, boolean isLast){
        for (int i = 0; i < indentation - 1; i++) {
            System.out.print(indentationRule.get(i) ? "│  " : "   ");
        }
        if (isLast){
            indentationRule.set(indentation - 1, false);
            System.out.print("└  ");
        }else {
            System.out.print("├  ");
        }
        System.out.println(structure);
    }
}
