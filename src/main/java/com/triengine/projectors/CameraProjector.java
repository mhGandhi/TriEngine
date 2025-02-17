package com.triengine.projectors;

import com.triengine.Vec;
import com.triengine.projectors.viewstates.ViewState;

public class CameraProjector extends Projector {

    public CameraProjector() {
        this(new ViewState());
    }
    public CameraProjector(ViewState pViewState) {
        super(pViewState);
    }

    @Override
    public int[] project(Vec pSysPos) {
        return null;
    }

}
