package com.triengine;

import com.triengine.projectors.Projector;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Environment extends JPanel {
    final Projector projector;
    List<Tri> staticTriangles;

    Vec[] c = {Vec.o().x(500).y(-100).z(-10),
            Vec.o().x(500).y(100).z(-10),
            Vec.o().x(-500).y(100).z(-10),
            Vec.o().x(-500).y(-100).z(-10)};
    public Environment(ActionListener pAc, Projector p){
        this.projector = p;
        staticTriangles = new ArrayList<>();
        {
            setBackground(Color.white);
            addMouseMotionListener(pAc);
            addComponentListener(pAc);
            addMouseListener(pAc);
        }



        staticTriangles.addAll(fromFiles());
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
                //Vec.o().x(1).y((0.707/2)).z((0.707/2)).normalize().scale(100)
        );
        for(Vec v : vecs){
            cg.drawVector(v);
        }

        drawTriangles(cg, makeTriangles());

        cg.drawPoint(c[0]);
        cg.drawPoint(c[1]);
        cg.drawPoint(c[2]);
        cg.drawPoint(c[3]);
    }

    private Collection<Tri> makeTriangles(){
        Collection<Tri> triangles = new ArrayList<>();
        triangles.addAll(staticTriangles);
        /*
        triangles.addAll(Tri.rectangle(
                Vec.o().x(75).y(50).z(75),
                Vec.o().x(75).y(-50).z(50),
                Axis.X
        ));
        triangles.add(new Tri(Vec.o().x(100),Vec.o(),Vec.o().z(100), Color.RED) );
        triangles.add(new Tri(Vec.o().x(100),Vec.o(),Vec.o().y(100), Color.GREEN) );
        triangles.add(new Tri(Vec.o().y(100),Vec.o(),Vec.o().z(100), Color.BLUE) );
         */
        triangles.addAll(TriGen.strip(
                c[0],c[1],c[2],c[3]
        ));

        return triangles;
    }

    private void drawTriangles(CGraphics cg, Collection<Tri> pTriangles) {
        List<Tri> triangles = new ArrayList<>(pTriangles);

        //System.out.println("drawing "+triangles.size()+" triangles");

        {//sort
            try {
                triangles.sort(Comparator.comparingInt(triangle -> projector.project(triangle.avgCoordinate())[2]));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

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
        Vec center = t.avgCoordinate();
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
        cg.setColor(t.col);

        Vec[] verts = {t.a,t.b,t.c};
        cg.fillPolygon(verts);
        cg.drawPolygon(verts);
    }

    Collection<Tri> fromFiles(){
        Collection<Tri> ret = List.of();
        System.out.println(System.getProperty("user.dir"));
        String path = System.getProperty("user.dir")+"/geometry/";

        Map<String,Vec> points = new HashMap<>();
        // Read points.txt
        try {
            List<String> lines = Files.readAllLines(Paths.get(path + "points.txt"));
            for (String line : lines) {
                if(line.startsWith("#"))continue;
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String name = parts[0];
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    points.put(name, new Vec(x, y, z));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading points.txt: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(path + "triangles.txt"));
            for (String line : lines) {
                if(line.startsWith("#"))continue;
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    Vec a = points.get(parts[0]);
                    Vec b = points.get(parts[1]);
                    Vec c = points.get(parts[2]);

                    String hexColor = parts[3].split(" ")[0]; // Parse hex color
                    Color color = Color.MAGENTA;
                    try{
                        color = Color.decode(hexColor);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    if (a != null && b != null && c != null) {
                        staticTriangles.addAll(Tri.t(a, b, c, color));
                    } else {
                        System.err.println("Error: Undefined point in triangle " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading triangles.txt: " + e.getMessage());
            return List.of();
        }

        return ret;
    }
}
