package com.triengine.projectors;

import com.triengine.Plane;
import com.triengine.Vec;
import com.triengine.projectors.viewstates.ViewState;

public class OrthogonalProjector extends Projector{
    private final Plane perspective;

    public OrthogonalProjector(Plane perspective) {
        super(new ViewState());
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
