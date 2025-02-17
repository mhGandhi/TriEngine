package com.triengine.geometry;

import com.triengine.Tri;

import java.util.Collection;
import java.util.List;

public class ImmutableGeometry extends Geometry{
    protected final Collection<Tri> triangles;

    public ImmutableGeometry(Collection<Tri> triangles) {
        this.triangles = triangles;
    }

    @Override
    public Collection<Tri> getTriangles() {
        return triangles;
    }
}
