package com.triengine;

import java.awt.geom.AffineTransform;
import java.net.URI;
import java.util.Objects;

public class Vec {
    public double x;
    public double y;
    public double z;

    public Vec(double pX, double pY, double pZ) {
        this.x = pX;
        this.y = pY;
        this.z = pZ;
    }

    public Vec(){
        this(0d,0d,0d);
    }

    public Vec x(double pX){
        this.x = pX;
        return this;
    }
    public Vec y(double pY){
        this.y = pY;
        return this;
    }
    public Vec z(double pZ){
        this.z = pZ;
        return this;
    }

    public static Vec o(){
        return new Vec();
    }

    public static Vec v(double x, double y, double z){
        return new Vec(x,y,z);
    }

    public Vec pro(Plane pPlane){
        switch (pPlane){
            case XY -> {
                return Vec.v(this.x,this.y,0);
            }
            case XZ -> {
                return Vec.v(this.x,0,this.z);
            }
            case YZ -> {
                return Vec.v(0,this.y,this.z);
            }
        }

        //todo central return for this
        System.err.println("invalid plane; reverting to nullvec");
        return Vec.o();
    }

    public static Vec add(Vec... vecs){
        Vec ret = Vec.o();

        for(Vec v : vecs){
            ret.x += v.x;
            ret.y += v.y;
            ret.z += v.z;
        }

        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vec vec)) return false;
        return Double.compare(x, vec.x) == 0 && Double.compare(y, vec.y) == 0 && Double.compare(z, vec.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "["+x+"|"+y+"|"+z+"]";
    }

    public Vec inv() {
        return Vec.v(this.x*-1,this.y*-1,this.z*-1);
    }

    public Vec scale(double pScale) {
        return Vec.v(this.x*pScale,this.y*pScale,this.z*pScale);
    }

    public double[] array() {
        double[] ret = {x,y,z};
        return ret;
    }

    public double getLength(){
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }

    public Vec subtract(Vec pV) {
        return new Vec(this.x-pV.x,this.y-pV.y,this.z-pV.z);
    }

    public Vec normalize() {
        double l = getLength();
        if(l == 0){
            return Vec.o();
        }
        return this.scale(1/l);
    }

    public double dot(Vec pV) {
        return this.x*pV.x+this.y*pV.y+this.z*pV.z;
    }

    public Vec cross(Vec pV) {
        Vec v = this;
        Vec w = pV;
        return new Vec(
                v.y*w.z-v.z*w.y,
                v.z*w.x-v.x*w.z,
                v.x*w.y-v.y*w.x
        );
    }
}