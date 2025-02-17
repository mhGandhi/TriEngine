package com.triengine.projectors;

import com.triengine.Plane;
import com.triengine.Vec;
import com.triengine.projectors.viewstates.ViewState;

public class OrthogonalProjector extends Projector{
    private final Plane perspective;

    public OrthogonalProjector() {
        this(Plane.XY);
    }
    public OrthogonalProjector(Plane perspective) {
        super(new ViewState());
        this.perspective = perspective;
    }

    @Override
    public int[] project(Vec pSysPos) {
        double sOfX = (double) viewState.screenWidth /2;
        double sOfY = (double) viewState.screenHeight /2;

        switch (perspective){
            case XY -> {
                return new int[] {(int)(pSysPos.x+sOfX), (int)(pSysPos.y+sOfY),   (int)pSysPos.z};
            }
            case YZ -> {
                return new int[] {(int)(pSysPos.y+sOfX), (int)(pSysPos.z*-1+sOfY),   (int)pSysPos.x};
            }
            case XZ -> {
                return new int[] {(int)(pSysPos.x+sOfX), (int)(pSysPos.z*-1+sOfY),   (int)pSysPos.y};
            }
        }
        System.err.println("Invalid Plane, reverting to XY");
        return new int[] {(int)pSysPos.x, (int)pSysPos.y, (int)pSysPos.z};
    }
}
