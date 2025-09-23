package net.ofts.vecCalc.history;

import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.calc.TextNumber;
import net.ofts.vecCalc.matrix.Matrix;
import net.ofts.vecCalc.vector.Number;
import net.ofts.vecCalc.vector.Vec3;
import net.ofts.vecCalc.vector.VecN;

public class SuperNumber {
    public double[][] entries;
    public Double num, x1, x2, x3;
    public double[] elements;
    public String type, text;

    public SuperNumber(INumber num){
        if (num instanceof Matrix matx){
            entries = matx.entries;
            type = "matx";
        }else if(num instanceof Number na){
            this.num = na.num;
            type = "numb";
        }else if (num instanceof Vec3 v3){
            this.x1 = v3.x1;
            this.x2 = v3.x2;
            this.x3 = v3.x3;
            type = "vec3";
        }else if (num instanceof VecN vn){
            this.elements = vn.elements;
            type = "vecN";
        }else if (num instanceof TextNumber txt){
            this.text = txt.text;
            type = "text";
        }else if(num == null){
            type = "null";
        }
    }

    public INumber degrade(){
        return switch (type){
            case "matx" -> new Matrix(entries);
            case "numb" -> new Number(num);
            case "vec3" -> new Vec3(x1, x2, x3);
            case "vecN" -> new VecN(elements);
            case "text" -> new TextNumber(text);
            default -> null;
        };
    }
}
