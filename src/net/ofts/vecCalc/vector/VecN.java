package net.ofts.vecCalc.vector;

/**
 * When designed, I set the vector's size as constant
 * However, when required to resize the vector, it becomes so painful. The only way is to create a new vec instead of resize it
 * This is a major engineering fatal!
 */
public class VecN {
    public double[] elements;

    public VecN(){}

    public VecN(int length){
        elements = new double[length];
    }

    public VecN(double[] entries){
        elements = entries;
    }

    public VecN(VecN copyFrom){
        elements = copyFrom.elements.clone();
    }

    public void mutableAdd(VecN b){
        for (int i = 0; i < elements.length; i++) {
            elements[i] += b.elements[i];
        }
    }

    public static VecN add(VecN a, VecN b){
        VecN result = new VecN(a);
        for (int i = 0; i < a.elements.length; i++) {
            result.elements[i] += b.elements[i];
        }
        return result;
    }

    public static VecN sub(VecN a, VecN b){
        VecN result = new VecN(a);
        for (int i = 0; i < a.elements.length; i++) {
            result.elements[i] -= b.elements[i];
        }
        return result;
    }

    public VecN mutableScale(double b){
        for (int i = 0; i < elements.length; i++) {
            elements[i] *= b;
        }
        return this;
    }

    public static VecN scale(VecN a, double b){
        VecN result = new VecN(a);
        for (int i = 0; i < a.elements.length; i++) {
            result.elements[i] *= b;
        }
        return result;
    }

    public static double dot(VecN a, VecN b){
        double result = 0;
        for (int i = 0; i < a.elements.length; i++){
            result += a.elements[i] * b.elements[i];
        }
        return result;
    }

    public static double lenSqr(VecN a){
        double result = 0;
        for (double i : a.elements){
            result += i * i;
        }
        return result;
    }

    public static double len(VecN a){
        return Math.sqrt(lenSqr(a));
    }

    public static VecN norm(VecN a){
        double len = len(a);
        if (len == 0) return a;
        VecN result = new VecN(a);
        for (int i = 0; i < a.elements.length; i++) {
            result.elements[i] /= len;
        }
        return result;
    }

    public static VecN Proj(VecN a, VecN b){
        double len = lenSqr(b);
        if (len == 0) return b;
        return scale(b, dot(a, b) / len);
    }

    public static VecN Perp(VecN a, VecN b){
        return sub(a, Proj(a, b));
    }
}
