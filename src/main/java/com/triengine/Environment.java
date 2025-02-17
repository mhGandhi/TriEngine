package com.triengine;

import com.triengine.projectors.CameraProjector;
import com.triengine.projectors.Projector;
import com.triengine.projectors.SimpleProjector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Environment extends JPanel {
    final Projector projector;
    List<Tri> triangles;

    public Environment(ActionListener pAc, Projector p){
        this.projector = p;
        triangles = new ArrayList<>();
        {
            setBackground(Color.white);
            addMouseMotionListener(pAc);
            addComponentListener(pAc);
        }

        triangles.add(new Tri(Vec.o().x(100),Vec.o(),Vec.o().z(100), Color.RED) );
        triangles.add(new Tri(Vec.o().x(100),Vec.o(),Vec.o().y(100), Color.GREEN) );
        triangles.add(new Tri(Vec.o().y(100),Vec.o(),Vec.o().z(100), Color.BLUE) );

        triangles.addAll(Tri.rectangleNormalToAxis(
                Vec.o().x(50).y(50).z(150),
                Vec.o().x(50).y(-50).z(125),
                Axis.X
        ));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        CGraphics cg = new CGraphics(g2d, projector);

        Vec e1 = Vec.o().x(100);
        cg.setColor(Color.red);
        cg.drawStraight(e1);
        Vec e2 = Vec.o().y(100);
        cg.setColor(Color.green);
        cg.drawStraight(e2);
        Vec e3 = Vec.o().z(100);
        cg.setColor(Color.blue);
        cg.drawStraight(e3);

        //cg.setColor(new Color(128,128,128, 128));
        //cg.fillPlane(e1,e2,Vec.o().z(50));
        //cg.setColor(new Color(0,0,0, 150));
        //cg.drawPlane(e1,e2,Vec.o().z(50));

        cg.setColor(Color.black);

        java.util.List<Vec> vecs = List.of(
                Vec.o().x(-100).y(0).z(50)
        );
        for(Vec v : vecs){
            cg.drawVector(v);
        }

        drawTriangles(cg);
    }

    private void drawTriangles(CGraphics cg) {
        {//sort
            try {
                triangles.sort(Comparator.comparingInt(triangle -> projector.project(triangle.avgCoordinate())[3]));
            }catch(Exception _){

            }


        }

        {//draw
            for(Tri t : triangles){
                drawTriangle(cg,t);
            }
        }
    }

    void drawTriangle(CGraphics cg, Tri t){
        cg.setColor(t.col);

        Vec[] verts = {t.a,t.b,t.c};
        cg.drawPolygon(verts);
        cg.drawPoint(t.avgCoordinate());
        /*
        cg.setColor(Color.red);
        cg.drawPoint(t.a);
        cg.setColor(Color.green);
        cg.drawPoint(t.b);
        cg.setColor(Color.blue);
        cg.drawPoint(t.c);
        */
    }
    void fillTriangle(CGraphics cg, Tri t){
        cg.setColor(t.col);

        Vec[] verts = {t.a,t.b,t.c};
        cg.fillPolygon(verts);
    }
}
