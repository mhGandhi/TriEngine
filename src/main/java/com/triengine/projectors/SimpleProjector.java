package com.triengine.projectors;

import com.triengine.Vec;

public class SimpleProjector extends Projector{
    @Override
    public int[] project(Vec pSysPos) {
        int rX = 500;
        int rY = 250;

        rX += (int)(pSysPos.y);
        rY -= (int)(pSysPos.z);

        rX -= (int)(pSysPos.x * 0.5);
        rY += (int)(pSysPos.x * 0.5);

        return new int[] {rX,rY};
    }
}
