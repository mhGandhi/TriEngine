package com.triengine.projectors;

import com.triengine.Plane;
import com.triengine.vectors.Vec;
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
                return new int[] {(int)(pSysPos.getX()+sOfX), (int)(pSysPos.getY()+sOfY),   (int)pSysPos.getZ()};
            }
            case YZ -> {
                return new int[] {(int)(pSysPos.getY()+sOfX), (int)(pSysPos.getZ()*-1+sOfY),   (int)pSysPos.getX()};
            }
            case XZ -> {
                return new int[] {(int)(pSysPos.getX()+sOfX), (int)(pSysPos.getZ()*-1+sOfY),   (int)pSysPos.getY()};
            }
        }
        System.err.println("Invalid Plane, reverting to XY");
        return new int[] {(int)pSysPos.getX(), (int)pSysPos.getY(), (int)pSysPos.getZ()};
    }
}
