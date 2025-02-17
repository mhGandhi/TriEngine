package com.triengine;

public abstract class Vec {
    public abstract double getX();
    public abstract double getY();
    public abstract double getZ();

    public static Vector o(){
        return new Vector();
    }

    public static Vector v(double x, double y, double z){
        return new Vector(x,y,z);
    }

    public Vec pro(Plane pPlane){
        switch (pPlane){
            case XY -> {
                return Vector.v(getX(),getY(),0);
            }
            case XZ -> {
                return Vector.v(getX(),0,getZ());
            }
            case YZ -> {
                return Vector.v(0,getY(),getZ());
            }
        }

        //todo central return for this
        System.err.println("invalid plane; reverting to nullvec");
        return Vector.o();
    }

    public static Vector add(Vec... vecs){
        Vector ret = Vector.o();

        for(Vec v : vecs){
            ret.setX(ret.getX()+v.getX());
            ret.setY(ret.getY()+v.getY());
            ret.setZ(ret.getZ()+v.getZ());
        }

        return ret;
    }

    public Vector inv() {
        return scale(-1);
    }

    public Vector scale(double pScale) {
        return Vector.v(this.getX()*pScale,this.getY()*pScale,this.getZ()*pScale);
    }

    public double getLength(){
        return Math.sqrt(this.getX()*this.getX() + this.getY()*this.getY() + this.getZ()*this.getZ());
    }

    public Vector subtract(Vec pV) {
        return new Vector(this.getX()-pV.getX(),this.getY()-pV.getY(),this.getZ()-pV.getZ());
    }

    public Vector normalize() {
        double l = getLength();
        if(l == 0){
            return Vector.o();
        }
        return this.scale(1/l);
    }

    public double dot(Vec pV) {
        return this.getX()*pV.getX()+this.getY()*pV.getY()+this.getZ()*pV.getZ();
    }

    public Vector cross(Vec pV) {
        Vec v = this;
        Vec w = pV;
        return new Vector(
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
