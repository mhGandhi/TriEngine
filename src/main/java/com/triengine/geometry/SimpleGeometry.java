package com.triengine.geometry;

import com.triengine.Tri;
import com.triengine.projectors.SimpleProjector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleGeometry extends Geometry{
    public List<Tri> triangles;//todo private

    public SimpleGeometry(){
        triangles = new ArrayList<>();
    }
    @Override
    public Collection<Tri> getTriangles() {
        return triangles;
    }
}
