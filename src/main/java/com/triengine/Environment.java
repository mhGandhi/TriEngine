package com.triengine;

import com.triengine.geometry.FileGeometry;
import com.triengine.geometry.Geometry;
import com.triengine.projectors.Projector;
import com.triengine.projectors.SimpleProjector;
import com.triengine.vectors.Vec;
import com.triengine.vectors.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Environment extends JPanel {
    Projector projector;

    Geometry pyramid;
    com.triengine.vectors.Vector[] c = {
            com.triengine.vectors.Vector.v(500,-100,-10),
            com.triengine.vectors.Vector.v(500,100,-10),
            com.triengine.vectors.Vector.v(-500,100,-10),
            com.triengine.vectors.Vector.v(-500,-100,-10)
    };

    public Environment(ActionHandler pAc, Projector p){
        this.projector = p;
        {
            setBackground(Color.white);
            addMouseMotionListener(pAc);
            addComponentListener(pAc);
            addMouseListener(pAc);
            addMouseWheelListener(pAc);
        }

        pyramid = new FileGeometry("pyramid");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        CGraphics cg = new CGraphics(g2d, projector);

        {//draw bg geometry
            com.triengine.vectors.Vector e1 = com.triengine.vectors.Vector.o().x(1000);
            cg.setColor(Color.red);
            cg.drawStraight(e1);
            com.triengine.vectors.Vector e2 = com.triengine.vectors.Vector.o().y(1000);
            cg.setColor(Color.green);
            cg.drawStraight(e2);
            com.triengine.vectors.Vector e3 = com.triengine.vectors.Vector.o().z(1000);
            cg.setColor(Color.blue);
            cg.drawStraight(e3);
        }

        drawTriangles(cg, makeTriangles());

        if(projector instanceof SimpleProjector sp){
            cg.setColor(Color.MAGENTA);

        }

        //todo draw temp round plane indicators
    }

    private List<Tri> makeTriangles(){
        List<Tri> triangles = new ArrayList<>();

        triangles.addAll(pyramid.getTriangles());
        triangles.addAll(TriGen.strip(
                c[0],c[1],c[2],c[3]
        ));
        triangles.addAll(TriGen.rectangle(
                com.triengine.vectors.Vector.v(150,50,150),
                com.triengine.vectors.Vector.v(50,150,150),
                Axis.Z
        ));



        {//sort
            try {
                triangles.sort(Comparator.comparingInt(triangle -> projector.project(triangle.avgCoordinate())[2]));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        {//painting shenanigans todo
            for (int i = triangles.size() - 1; i > 0; i--) {
                Tri t = triangles.get(i);
                if (t.contains(projector.viewState.mousePos, projector)) {
                    t.col = Color.red;
                    break;
                }
            }
        }

        return triangles;
    }

    private void drawTriangles(CGraphics cg, List<Tri> pTriangles) {
        List<Tri> triangles = new ArrayList<>(pTriangles);

        //System.out.println("drawing "+triangles.size()+" triangles");

        {//draw
            int i = 0;
            for(Tri t : triangles){
                fillTriangle(cg,t);
                cg.setColor(new Color(0,0,0,100));
                drawTriangle(cg,t);
                cg.setColor(Color.WHITE);
                //debugTriangle(cg,t,triangles.size()-i);
                i++;
            }
        }
    }

    void drawTriangle(CGraphics cg, Tri t){
        Vec[] verts = {t.a,t.b,t.c};
        cg.drawPolygon(verts);
    }

    void debugTriangle(CGraphics cg, Tri t, int i){
        Vec[] verts = {t.a,t.b,t.c};
        cg.drawPolygon(verts);
        Vector center = t.avgCoordinate();
        cg.drawPoint(center);
        cg.drawString(center,i<10?i+"":"#");
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
        Vec[] verts = {t.a,t.b,t.c};

        cg.setColor(t.col);

        cg.fillPolygon(verts);
        cg.drawPolygon(verts);
    }
}
