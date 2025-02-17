package com.triengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TriGen {
    public static Collection<Tri> rectangle(Vec p1, Vec p2, Axis pAxis){
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

        Collection<Tri> t1 = Tri.t(p1,pi1,pi2);
        Collection<Tri> t2 = Tri.t(p2,pi1,pi2);

        ArrayList<Tri> ret = new ArrayList<>(t1);
        ret.addAll(t2);
        return ret;
    }
    public static Collection<Tri> strip(Vec p1, Vec p2, Axis pAxis){
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
        return strip(p1,pi1,pi2,pi2);
    }

    public static Collection<Tri> strip(Vec cornerA, Vec cornerB, Vec cornerC, Vec cornerD){
        Vec p1 = cornerA;
        Vec p2 = cornerC;
        Vec pi1 = cornerB;
        Vec pi2 = cornerD;

        Collection<Tri> triangles = new ArrayList<>();


        Vec frontLeft = Vec.o();    Vec frontRight = Vec.o();
        Vec backLeft = Vec.o();     Vec backRight = Vec.o();
        final int segmentCount;
        final double totalLen;
        {//assign corners
            double l1 = p1.subtract(pi1).getLength();
            double l2 = p1.subtract(pi2).getLength();

            if(l1<=l2){
                frontLeft = p1;
                frontRight = pi1;

                backLeft = pi2;
                backRight = p2;

                segmentCount = (int)Math.ceil(l2/l1);
                totalLen = l2;
                //startLeft = l2/segmentLen%2==1;
            }else{
                frontLeft = pi1;
                frontRight = p2;

                backLeft = p1;
                backRight = pi2;

                segmentCount = (int)Math.ceil(l1/l2);
                totalLen = l1;
                //startLeft = l1/segmentLen%2==1;
            }
        }
        //test
        //triangles.add(new Tri(frontLeft,frontRight,backLeft,Color.CYAN));
        //triangles.add(new Tri(backRight,frontRight,backLeft,Color.GREEN));

        final double segmentLen = totalLen/segmentCount;
        //System.out.println(totalLen+" seg "+segmentLen);

        Vec direction = backLeft.subtract(frontLeft).normalize().scale(segmentLen);

        Vec nextA = Vec.add(frontLeft,direction);
        Vec nextB = frontRight;
        Vec nextC = frontLeft;

        double currentLen = 0;
        while(currentLen<totalLen){
            Vec a=nextA,b=nextB,c=nextC;
            Vec[] t = {a,b,c};
            currentLen+=segmentLen/2;
            if(currentLen>=totalLen){
                t[0] = backRight;
                t[1] = backLeft;
            }
            triangles.addAll(Tri.t(t[0],t[1],t[2]));

            nextA = Vec.add(b,direction);
            nextB = a;
            nextC = b;
        }

        return triangles;
    }
}
