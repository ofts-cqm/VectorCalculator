package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.INumber;

public class Vec3 extends INumber {
    public double x1, x2, x3;

    public Vec3(double x1, double x2, double x3){
        super("vec3");
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
    }

    public void setByIndex(int index, double val){
        if (index == 1) x1 = val;
        else if(index == 2) x2 = val;
        else x3 = val;
    }

    public static Vec3 add(Vec3 a, Vec3 b){
        return new Vec3(a.x1 + b.x1, a.x2 + b.x2, a.x3 + b.x3);
    }

    public static Vec3 sub(Vec3 a, Vec3 b){
        return new Vec3(a.x1 - b.x1, a.x2 - b.x2, a.x3 - b.x3);
    }

    public static Vec3 scale(Vec3 a, double scale){
        return new Vec3(a.x1 * scale, a.x2 * scale, a.x3 * scale);
    }

    public static double dot(Vec3 a, Vec3 b){
        return a.x1 * b.x1 + a.x2 * b.x2 + a.x3 * b.x3;
    }

    public static Vec3 cross(Vec3 a, Vec3 b){
        return new Vec3(
                a.x2 * b.x3 - a.x3 * b.x2,
                a.x3 * b.x1 - a.x1 * b.x3,
                a.x1 * b.x2 - a.x2 * b.x1
        );
    }

    public static double len(Vec3 v){
        return Math.sqrt(lenSqr(v));
    }

    public static double lenSqr(Vec3 v){
        return v.x1 * v.x1 + v.x2 * v.x2 + v.x3 * v.x3;
    }

    public static Vec3 norm(Vec3 v){
        double length = len(v);
        if (length == 0) return v;
        return new Vec3(v.x1 / length, v.x2 / length, v.x3 / length);
    }

    public static Vec3 Proj(Vec3 a, Vec3 b){
        double len = lenSqr(b);
        if (len == 0) return b;
        return scale(b, dot(a, b) / len);
    }

    public static Vec3 Perp(Vec3 a, Vec3 b){
        return sub(a, Proj(a, b));
    }

    @Override
    public INumber clone() {
        return new Vec3(x1, x2, x3);
    }
}
