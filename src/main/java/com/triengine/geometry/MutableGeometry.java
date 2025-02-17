package com.triengine.geometry;

import com.triengine.Tri;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MutableGeometry extends Geometry{
    public List<Tri> triangles;//todo private

    public MutableGeometry(){
        triangles = new ArrayList<>();
    }
    @Override
    public Collection<Tri> getTriangles() {
        return triangles;
    }
}
