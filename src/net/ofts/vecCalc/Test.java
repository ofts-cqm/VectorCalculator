package net.ofts.vecCalc;

import net.ofts.vecCalc.history.HistoryItem;
import net.ofts.vecCalc.vector.Vec3;

public class Test {
    public static void main(String[] args) {
        Main.main(null);
        new HistoryItem("vec31", new Vec3[]{ new Vec3(1, 2, 3), new Vec3(3, 2, 1), new Vec3()}).openHistory();
    }
    //45(1+1)
    // 4pi + 5e
    // 4abs-2
    // sin5pi
}
