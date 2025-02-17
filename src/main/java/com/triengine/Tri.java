package com.triengine;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static Collection<Tri> t(Vec a, Vec b, Vec c, Color color) {
        return Tri.t(a,b,c,25d, color);
    }

    public Vec avgCoordinate(){
        return Vec.add(a,b,c).scale((double)1/3);
    }

    public static Collection<Tri> t(Vec a, Vec b, Vec c){
        return Tri.t(a,b,c,Color.GRAY);
    }

    public static Collection<Tri> t(Vec a, Vec b, Vec c, double pMaxSideLen){
        return Tri.t(a,b,c,pMaxSideLen,Color.GRAY,0);
    }
    public static Collection<Tri> t(Vec a, Vec b, Vec c, double pMaxSideLen, Color pColor){
        return Tri.t(a,b,c,pMaxSideLen,pColor,0);
    }

    public static Collection<Tri> t(Vec a, Vec b, Vec c, double pMaxSideLen, Color pColor, int pRecDepth){
        pRecDepth++;
        if(pRecDepth>=9){
            return List.of(new Tri(a,b,c,pColor));
        }

        ArrayList<Tri> ret = new ArrayList<>();

        double ab = b.subtract(a).getLength();
        double bc = c.subtract(c).getLength();
        double ca = a.subtract(c).getLength();


        if(ab>=bc&&ab>=ca&&ab>pMaxSideLen){
            Vec mab = Vec.add(a,b).scale(0.5d);

            ret.addAll(Tri.t(c,mab,a,pMaxSideLen,pColor,pRecDepth));
            ret.addAll(Tri.t(c,mab,b,pMaxSideLen,pColor,pRecDepth));

            return ret;
        }
        if(bc>ca&&bc>=ab&&bc>pMaxSideLen){
            Vec mbc = Vec.add(b,c).scale(0.5d);

            ret.addAll(Tri.t(a,mbc,b,pMaxSideLen,pColor,pRecDepth));
            ret.addAll(Tri.t(a,mbc,c,pMaxSideLen,pColor,pRecDepth));

            return ret;
        }
        if(ca>pMaxSideLen){
            Vec mca = Vec.add(c,a).scale(0.5d);

            ret.addAll(Tri.t(b,mca,c,pMaxSideLen,pColor,pRecDepth));
            ret.addAll(Tri.t(b,mca,a,pMaxSideLen,pColor,pRecDepth));

            return ret;
        }

        return List.of(new Tri(a,b,c,pColor));
    }
}