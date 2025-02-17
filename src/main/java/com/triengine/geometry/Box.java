package com.triengine.geometry;

import com.triengine.Tri;

import java.util.Collection;
import java.util.List;

public class Box extends ImmutableGeometry {

    public Box() {
        super(buildBox());
    }

    private static Collection<Tri> buildBox() {
        return List.of();
    }

    @Override
    public Collection<Tri> getTriangles() {
        return List.of();
    }
}
