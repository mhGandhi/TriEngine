package com.triengine.geometry;

import com.triengine.Tri;
import com.triengine.TriGen;
import com.triengine.vectors.SetVector;
import com.triengine.vectors.Vec;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pillar extends ImmutableGeometry {

    //todo fix 8-10
    public Pillar(Vec pVector, Vec pSupport, double pR, double pRes){
        super(buildPillar(pVector, pSupport, pR, pRes));
    }

    private static Collection<Tri> buildPillar(Vec pVector, Vec pSupport, double pR, double pRes) {
        if(pRes<2){
            System.err.println("Resolution must be at least 2");
            pRes = 2;
        }
        if(pR<1){
            System.err.println("Radius must be at least 1");
            pR = 1;
        }
        List<Vec> vecs = generateCircleVectors(pSupport, pR, (double)360/pRes, pVector);
        //vecs = List.of(Vec.v(30,30,0),Vec.v(-30,30,0),Vec.v(-30,-30,0),Vec.v(30,-30,0));
        //System.out.println(vecs.size());
        //return Tri.t(vecs.get(0).v(),vecs.get(1).v(),pSupport.v());


        SetVector[][] points = new SetVector[2][vecs.size()];

        for(int i = 0; i < points[0].length; i++){
            points[0][i] = vecs.get(i).v();
            points[1][i] = Vec.add(vecs.get(i),pVector);
            //System.out.println(points[0][i]+" "+points[1][i]);
        }
        Collection<Tri> triangles = new ArrayList<>();

        triangles.addAll(TriGen.strip(points[0][0],points[0][points[0].length-1],points[1][points[1].length-1],points[1][0]));
        for(int i = 0; i < points[0].length-1; i++){
            triangles.addAll(TriGen.strip(points[0][i],points[0][i+1],points[1][i+1],points[1][i]));
        }
        triangles.addAll(Tri.t(points[0][0],points[0][points[0].length-1],pSupport.v()));
        triangles.addAll(Tri.t(points[1][0],points[1][points[1].length-1],Vec.add(pSupport,pVector)));
        for(int i = 0; i < points[0].length-1; i++){
            triangles.addAll(Tri.t(points[0][i],points[0][i+1],pSupport.v()));

            triangles.addAll(Tri.t(points[1][i],points[1][i+1],Vec.add(pSupport,pVector)));
        }
        //System.out.println(triangles.size());
        return triangles;
    }

    public static List<Vec> generateCircleVectors(Vec P, double r, double res, Vec N) {
        List<Vec> vectors = new ArrayList<>();

        // Normalize the normal vector
        SetVector normal = N.normalize();

        // Get two perpendicular vectors (U, V) forming a basis for the circle plane
        Vec arbitrary = (Math.abs(normal.x) < Math.abs(normal.y) && Math.abs(normal.x) < Math.abs(normal.z)) ? Vec.v(1, 0, 0) : Vec.v(0, 1, 0);
        Vec U = normal.cross(arbitrary).normalize();
        Vec V = normal.cross(U).normalize();

        //return List.of(U.scale(100),V.scale(100));

        // Generate circle points and corresponding vectors
        for (double angle = 0; angle < 360; angle += res) {
            double rad = Math.toRadians(angle);
            Vec circlePoint = P.add(U.scale(Math.cos(rad) * r)).add(V.scale(Math.sin(rad) * r));
            vectors.add(circlePoint);
        }

        return vectors;
    }
}
