package com.triengine;

import java.awt.*;

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
}