package com.triengine.vectors;

public class UnstableVector extends Vec {
    Vec baseVector;
    double maxOff;

    public UnstableVector(Vec pBaseVec,double pMaxOff){
        baseVector = pBaseVec;
        maxOff = pMaxOff;
    }

    private double getOff(){
        return Math.random()*maxOff;
    }

    @Override
    public double getX() {
        return baseVector.getX()+getOff();
    }

    @Override
    public double getY() {
        return baseVector.getY()+getOff();
    }

    @Override
    public double getZ() {
        return baseVector.getZ()+getOff();
    }
}
