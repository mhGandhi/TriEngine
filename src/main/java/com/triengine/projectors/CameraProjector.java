package com.triengine.projectors;

import com.triengine.Vec;
import com.triengine.projectors.viewstates.ViewState;

public class CameraProjector extends Projector {
    private final ViewState vs;

    public CameraProjector(ViewState pViewState) {
        this.vs = pViewState;
    }

    @Override
    public int[] project(Vec pSysPos) {
        return null;
    }

}
