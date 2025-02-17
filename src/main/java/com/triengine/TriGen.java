package com.triengine;

import com.triengine.vectors.Vec;
import com.triengine.vectors.Vector;

import java.util.ArrayList;
import java.util.Collection;

public class TriGen {
    public static Collection<Tri> rectangle(Vector p1, Vector p2, Axis pAxis){
        Vector pi1 = Vector.o();
        Vector pi2 = Vector.o();
        switch (pAxis){
            case X -> {
                pi1.y(p1.y).z(p2.z).x(p1.x);
                pi2.y(p2.y).z(p1.z).x(p1.x);
            }
            case Y -> {
                pi1.x(p1.x).z(p2.z).y(p1.y);
                pi2.x(p2.x).z(p1.z).y(p1.y);
            }
            case Z -> {
                pi1.y(p1.y).x(p2.x).z(p1.z);
                pi2.y(p2.y).x(p1.x).z(p1.z);
            }
        }

        Collection<Tri> t1 = Tri.t(p1,pi1,pi2);
        Collection<Tri> t2 = Tri.t(p2,pi1,pi2);

        ArrayList<Tri> ret = new ArrayList<>(t1);
        ret.addAll(t2);
        return ret;
    }
    public static Collection<Tri> strip(Vector p1, Vector p2, Axis pAxis){
        Vector pi1 = Vector.o();
        Vector pi2 = Vector.o();
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

    public static Collection<Tri> strip(Vector cornerA, Vector cornerB, Vector cornerC, Vector cornerD){
        Vector p1 = cornerA;
        Vector p2 = cornerC;
        Vector pi1 = cornerB;
        Vector pi2 = cornerD;

        Collection<Tri> triangles = new ArrayList<>();


        Vector frontLeft = Vector.o();    Vector frontRight = Vector.o();
        Vector backLeft = Vector.o();     Vector backRight = Vector.o();
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

        Vector direction = backLeft.subtract(frontLeft).normalize().scale(segmentLen);

        Vector nextA = Vec.add(frontLeft,direction);
        Vector nextB = frontRight;
        Vector nextC = frontLeft;

        double currentLen = 0;
        while(currentLen<totalLen){
            Vector a=nextA,b=nextB,c=nextC;
            Vector[] t = {a,b,c};
            currentLen+=segmentLen/2;
            if(currentLen>=totalLen-segmentLen/2){
                t[0] = backLeft;
            }
            if(currentLen>=totalLen){
                t[0] = backRight;
                t[1] = backLeft;
            }
            triangles.addAll(Tri.t(t[0],t[1],t[2]));

            nextA = Vector.add(b,direction);
            nextB = a;
            nextC = b;
        }

        return triangles;
    }
}
