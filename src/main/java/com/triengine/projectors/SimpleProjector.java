package com.triengine.projectors;

import com.triengine.Vec;
import com.triengine.projectors.viewstates.ViewState;

public class SimpleProjector extends Projector{
    @Override
    public int[] project(Vec pSysPos) {
        int rX = 0;
        int rY = 0;

        rX += (int)(pSysPos.y);
        rY -= (int)(pSysPos.z);

        rX -= (int)(pSysPos.x * 0.5);
        rY += (int)(pSysPos.x * 0.5);

        return new int[] {rX,rY};
    }

    public class SimpleViewState extends ViewState {

    }
}
