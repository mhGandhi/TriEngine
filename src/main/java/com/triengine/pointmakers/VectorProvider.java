package com.triengine.pointmakers;

import com.triengine.Vector;

import java.util.function.Supplier;

/**
 * provides one vector
 */
public class VectorProvider implements Supplier<Vector> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public Vector get() {
        return Vector.o();
    }
}
