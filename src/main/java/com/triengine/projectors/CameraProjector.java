package com.triengine.projectors;

import com.triengine.Vec;
import com.triengine.projectors.viewstates.ViewState;

public class CameraProjector extends Projector {

    public CameraProjector() {
        super(new ViewState());
    }

    @Override
    public int[] project(Vec pSysPos) {
        return null;
    }

}
