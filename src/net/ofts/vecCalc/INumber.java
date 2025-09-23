package net.ofts.vecCalc;

public abstract class INumber implements Cloneable{
    public final String type;

    public INumber(String type){
        this.type = type;
    }

    public abstract INumber clone();
}
