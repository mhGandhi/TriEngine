package com.triengine;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public class Tri {
    public final Vec a;
    public final Vec b;
    public final Vec c;
    public final Color col;

    public Tri(Vec a, Vec b, Vec c){
        this(a,b,c,Color.BLACK);
    }
    public Tri(Vec pA, Vec pB, Vec pC, Color pCol){
        a = pA;
        b= pB;
        c = pC;

        this.col = pCol;
    }


    public static Collection<Tri> rectangleNormalToAxis(Vec p1, Vec p2, Axis pAxis){
        Vec pi1 = Vec.o();
        Vec pi2 = Vec.o();
        switch (pAxis){
            case X -> {
                pi1.y(p1.y).z(p2.z).x(p1.x);
                pi2.y(p2.y).z(p1.z).x(p1.x);
            }
            case Y -> {
                pi1.x(p1.x).z(p2.z).x(p1.x);
                pi2.x(p2.x).z(p1.z).x(p1.x);
            }
            case Z -> {
                pi1.y(p1.y).x(p2.x).x(p1.x);
                pi2.y(p2.y).x(p1.x).x(p1.x);
            }
        }

        Tri t1 = new Tri(p1,pi1,pi2);
        Tri t2 = new Tri(p2,pi1,pi2);

        return List.of(t1,t2);
    }
}