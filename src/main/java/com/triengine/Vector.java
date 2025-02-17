package com.triengine;

import java.util.Objects;

public class Vector extends Vec {
    public double x;
    public double y;
    public double z;

    public Vector(){
        this(0d,0d,0d);
    }
    public Vector(double pX, double pY, double pZ) {
        this.x = pX;
        this.y = pY;
        this.z = pZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector vec)) return false;
        return Double.compare(x, vec.x) == 0 && Double.compare(y, vec.y) == 0 && Double.compare(z, vec.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    public void setX(double pX) {
        this.x = pX;
    }

    public void setY(double pY) {
        this.y = pY;
    }

    public void setZ(double pZ) {
        this.z = pZ;
    }

    public Vector x(double pX){
        this.x = pX;
        return this;
    }
    public Vector y(double pY){
        this.y = pY;
        return this;
    }
    public Vector z(double pZ){
        this.z = pZ;
        return this;
    }
}