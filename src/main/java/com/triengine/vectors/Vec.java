package com.triengine.vectors;

import com.triengine.Plane;

public abstract class Vec {
    public abstract double getX();
    public abstract double getY();
    public abstract double getZ();

    public static SetVector o(){
        return new SetVector();
    }

    public static SetVector v(double x, double y, double z){
        return new SetVector(x,y,z);
    }

    public Vec pro(Plane pPlane){
        switch (pPlane){
            case XY -> {
                return SetVector.v(getX(),getY(),0);
            }
            case XZ -> {
                return SetVector.v(getX(),0,getZ());
            }
            case YZ -> {
                return SetVector.v(0,getY(),getZ());
            }
        }

        //todo central return for this
        System.err.println("invalid plane; reverting to nullvec");
        return SetVector.o();
    }

    public static SetVector add(Vec... vecs){
        SetVector ret = SetVector.o();

        for(Vec v : vecs){
            ret.setX(ret.getX()+v.getX());
            ret.setY(ret.getY()+v.getY());
            ret.setZ(ret.getZ()+v.getZ());
        }

        return ret;
    }

    public SetVector add(Vec vec){
        return Vec.add(vec, this);
    }

    public SetVector v(){
        return Vec.v(getX(),getY(),getZ());
    }

    public SetVector inv() {
        return scale(-1);
    }

    public SetVector scale(double pScale) {
        return SetVector.v(this.getX()*pScale,this.getY()*pScale,this.getZ()*pScale);
    }

    public double getLength(){
        return Math.sqrt(this.getX()*this.getX() + this.getY()*this.getY() + this.getZ()*this.getZ());
    }

    public SetVector subtract(Vec pV) {
        return new SetVector(this.getX()-pV.getX(),this.getY()-pV.getY(),this.getZ()-pV.getZ());
    }

    public SetVector normalize() {
        double l = getLength();
        if(l == 0){
            return SetVector.o();
        }
        return this.scale(1/l);
    }

    public double dot(Vec pV) {
        return this.getX()*pV.getX()+this.getY()*pV.getY()+this.getZ()*pV.getZ();
    }

    public SetVector cross(Vec pV) {
        Vec v = this;
        Vec w = pV;
        return new SetVector(
                v.getY()*w.getZ()-v.getZ()*w.getY(),
                v.getZ()*w.getX()-v.getX()*w.getZ(),
                v.getX()*w.getY()-v.getY()*w.getX()
        );
    }

    @Override
    public String toString() {
        return "["+getX()+"|"+getY()+"|"+getZ()+"]";
    }
}
