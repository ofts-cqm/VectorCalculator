package net.ofts.vecCalc.calc;

public class StructureTreeLogger {
    private static int indentation = 0;

    public static void push() { indentation++; }

    public static void pop() { indentation--; }

    public static void write(String structure){
        for (int i = 0; i < indentation - 1; i++) {
            System.out.print("│  ");
        }
        System.out.print("└  ");
        System.out.println(structure);
    }
}
