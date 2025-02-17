package com.triengine;

import java.awt.*;
import java.util.ArrayList;
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

    public Vec avgCoordinate(){
        return Vec.add(a,b,c).scale((double)1/3);
    }

    public static Collection<Tri> t(Vec a, Vec b, Vec c){
        return Tri.t(a,b,c,10d);
    }

    public static Collection<Tri> t(Vec a, Vec b, Vec c, double pMaxSideLen){
        return List.of(new Tri(a,b,c));
    }
}