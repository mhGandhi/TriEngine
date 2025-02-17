package com.triengine.projectors;

import com.triengine.Vec;
import com.triengine.projectors.viewstates.ViewState;

public class OrthogonalProjector extends Projector{

    public enum Plane{
        XY,
        YZ,
        XZ
    }
    private final Plane perspective;

    public OrthogonalProjector() {
        this(Plane.XY);
    }
    public OrthogonalProjector(Plane perspective) {
        this.perspective = perspective;
    }

    @Override
    public int[] project(Vec pSysPos) {
        switch (perspective){
            case XY -> {
                return new int[] {(int)pSysPos.x, (int)pSysPos.y,   (int)pSysPos.z};
            }
            case YZ -> {
                return new int[] {(int)pSysPos.y, (int)pSysPos.z,   (int)pSysPos.x};
            }
            case XZ -> {
                return new int[] {(int)pSysPos.x, (int)pSysPos.z,   (int)pSysPos.y};
            }
        }
        System.err.println("Invalid Plane, reverting to XY");
        return new int[] {(int)pSysPos.x, (int)pSysPos.y, (int)pSysPos.z};
    }
}
