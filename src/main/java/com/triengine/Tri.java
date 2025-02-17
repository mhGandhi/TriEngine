package com.triengine;

import com.triengine.projectors.Projector;
import com.triengine.vectors.Vec;
import com.triengine.vectors.SetVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Tri {
    public final Vec a;
    public final Vec b;
    public final Vec c;
    public Color col;

    public Tri(Vec a, Vec b, Vec c){
        this(a,b,c,Color.BLACK);
    }
    public Tri(Vec pA, Vec pB, Vec pC, Color pCol){
        a = pA;
        b= pB;
        c = pC;

        this.col = pCol;
    }

    public SetVector avgCoordinate(){
        return Vec.add(a,b,c).scale((double)1/3);
    }

    // Method to calculate the sign of the cross product (helps determine the relative orientation)
    private static int sign(int[] p, int[] v1, int[] v2) {
        return (p[0] - v2[0]) * (v1[1] - v2[1]) - (v1[0] - v2[0]) * (p[1] - v2[1]);
    }

    public boolean contains(int[] pPoint, Projector projector) {

        int[][] triPoints = {
                projector.project(a),
                projector.project(b),
                projector.project(c)
        };
        return Tri.contains(pPoint,triPoints);
    }
    // Method to check if the point pPoint is inside the triangle formed by pTrianglePoints
    public static boolean contains(int[] pPoint, int[][] pTrianglePoints) {
        // Get the triangle vertices
        int[] a = pTrianglePoints[0];
        int[] b = pTrianglePoints[1];
        int[] c = pTrianglePoints[2];

        // Calculate the cross products
        int d1 = sign(pPoint, a, b);
        int d2 = sign(pPoint, b, c);
        int d3 = sign(pPoint, c, a);

        // Check if the point is inside the triangle (all signs should be the same)
        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        // The point is inside if all signs are consistent (either all positive or all negative)
        return !(hasNeg && hasPos);
    }

    public static Collection<Tri> t(SetVector a, SetVector b, SetVector c, Color color) {
        return Tri.t(a,b,c,25d, color);
    }

    public static Collection<Tri> t(SetVector a, SetVector b, SetVector c){
        return Tri.t(a,b,c,Color.GRAY);
    }

    public static Collection<Tri> t(SetVector a, SetVector b, SetVector c, double pMaxSideLen){
        return Tri.t(a,b,c,pMaxSideLen,Color.GRAY,0);
    }
    public static Collection<Tri> t(SetVector a, SetVector b, SetVector c, double pMaxSideLen, Color pColor){
        return Tri.t(a,b,c,pMaxSideLen,pColor,0);
    }

    public static Collection<Tri> t(SetVector a, SetVector b, SetVector c, double pMaxSideLen, Color pColor, int pRecDepth){
        pRecDepth++;
        if(pRecDepth>=9){
            return List.of(new Tri(a,b,c,pColor));
        }

        ArrayList<Tri> ret = new ArrayList<>();

        double ab = b.subtract(a).getLength();
        double bc = c.subtract(c).getLength();
        double ca = a.subtract(c).getLength();


        if(ab>=bc&&ab>=ca&&ab>pMaxSideLen){
            SetVector mab = Vec.add(a,b).scale(0.5d);

            ret.addAll(Tri.t(c,mab,a,pMaxSideLen,pColor,pRecDepth));
            ret.addAll(Tri.t(c,mab,b,pMaxSideLen,pColor,pRecDepth));

            return ret;
        }
        if(bc>ca&&bc>=ab&&bc>pMaxSideLen){
            SetVector mbc = Vec.add(b,c).scale(0.5d);

            ret.addAll(Tri.t(a,mbc,b,pMaxSideLen,pColor,pRecDepth));
            ret.addAll(Tri.t(a,mbc,c,pMaxSideLen,pColor,pRecDepth));

            return ret;
        }
        if(ca>pMaxSideLen){
            SetVector mca = Vec.add(c,a).scale(0.5d);

            ret.addAll(Tri.t(b,mca,c,pMaxSideLen,pColor,pRecDepth));
            ret.addAll(Tri.t(b,mca,a,pMaxSideLen,pColor,pRecDepth));

            return ret;
        }

        return List.of(new Tri(a,b,c,pColor));
    }
}