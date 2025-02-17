package com.triengine.projectors;
import com.triengine.vectors.Vec;
import com.triengine.projectors.viewstates.ViewState;

public abstract class Projector {
    public final ViewState viewState;

    protected Projector(ViewState viewState) {
        this.viewState = viewState;
    }

    public abstract int[] project(Vec pSysPos);
}
