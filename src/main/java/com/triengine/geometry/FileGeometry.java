package com.triengine.geometry;

import com.triengine.Tri;
import com.triengine.Vector;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class FileGeometry extends Geometry{
    private Collection<Tri> triangles;

    public FileGeometry(String pFileName){
        this.triangles = fromFiles(pFileName);
    }

    @Override
    public Collection<Tri> getTriangles() {
        return triangles;
    }



    Collection<Tri> fromFiles(String pFileName){
        List<Tri> ret = new ArrayList<>();
        System.out.println(System.getProperty("user.dir"));
        String path = System.getProperty("user.dir")+"/geometry/";

        Map<String, Vector> points = new HashMap<>();
        // Read points.txt
        try {
            List<String> lines = Files.readAllLines(Paths.get(path+pFileName+".points"));
            for (String line : lines) {
                if(line.startsWith("#"))continue;
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String name = parts[0];
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    points.put(name, new Vector(x, y, z));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading points.txt: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(path+pFileName+".triangles"));
            for (String line : lines) {
                if(line.startsWith("#"))continue;
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    Vector a = points.get(parts[0]);
                    Vector b = points.get(parts[1]);
                    Vector c = points.get(parts[2]);

                    String hexColor = parts[3].split(" ")[0]; // Parse hex color
                    Color color = Color.MAGENTA;
                    try{
                        color = Color.decode(hexColor);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    if (a != null && b != null && c != null) {
                        ret.addAll(Tri.t(a, b, c, color));
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
