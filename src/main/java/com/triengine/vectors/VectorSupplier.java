package com.triengine.vectors;

/**
 * provides one vector
 */
public class VectorSupplier implements java.util.function.Supplier<Vec> {
    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public Vec get() {
        return Vec.o();
    }
}
