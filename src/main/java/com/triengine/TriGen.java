package com.triengine;

import com.triengine.vectors.Vec;
import com.triengine.vectors.SetVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class TriGen {
    public static Collection<Tri> rectangle(SetVector p1, SetVector p2, Axis pAxis){
        SetVector pi1 = SetVector.o();
        SetVector pi2 = SetVector.o();
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
    public static Collection<Tri> rectangle(SetVector A, SetVector B, SetVector C, SetVector D){

        Collection<Tri> t1 = Tri.t(A,B,C);
        Collection<Tri> t2 = Tri.t(A,C,D);

        ArrayList<Tri> ret = new ArrayList<>(t1);
        ret.addAll(t2);
        return ret;
    }
    public static Collection<Tri> strip(SetVector p1, SetVector p2, Axis pAxis){
        SetVector pi1 = SetVector.o();
        SetVector pi2 = SetVector.o();
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

    public static Collection<Tri> strip(SetVector cornerA, SetVector cornerB, SetVector cornerC, SetVector cornerD){
        SetVector p1 = cornerA;
        SetVector p2 = cornerC;
        SetVector pi1 = cornerB;
        SetVector pi2 = cornerD;

        Collection<Tri> triangles = new ArrayList<>();


        SetVector frontLeft = SetVector.o();    SetVector frontRight = SetVector.o();
        SetVector backLeft = SetVector.o();     SetVector backRight = SetVector.o();
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

        SetVector direction = backLeft.subtract(frontLeft).normalize().scale(segmentLen);

        SetVector nextA = Vec.add(frontLeft,direction);
        SetVector nextB = frontRight;
        SetVector nextC = frontLeft;

        double currentLen = 0;
        while(currentLen<totalLen){
            SetVector a=nextA,b=nextB,c=nextC;
            SetVector[] t = {a,b,c};
            currentLen+=segmentLen/2;
            if(currentLen>=totalLen-segmentLen/2){
                t[0] = backLeft;
            }
            if(currentLen>=totalLen){
                t[0] = backRight;
                t[1] = backLeft;
            }
            triangles.addAll(Tri.t(t[0],t[1],t[2]));

            nextA = SetVector.add(b,direction);
            nextB = a;
            nextC = b;
        }

        return triangles;
    }
}
