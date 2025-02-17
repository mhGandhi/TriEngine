package com.triengine.projectors;

import com.triengine.Axis;
import com.triengine.vectors.Vec;
import com.triengine.vectors.Vector;
import com.triengine.projectors.viewstates.ViewState;

public class SimpleProjector extends Projector{

    public static final double OFFSET_FOR_PROJECTION = (0.707 / 2);
    public static final Vector VIEW_DIRECTION = Vector.v(1, OFFSET_FOR_PROJECTION, OFFSET_FOR_PROJECTION).normalize();


    public SimpleProjector() {
        super(new SimpleViewState());
    }
    public SimpleViewState getSvs(){
        return (SimpleViewState) this.viewState;
    }

    /**
     *
     * @param pSysPos
     * @return xPos on Screen, yPos on Screen, closeness to cam (depth)
     */
    @Override
    public int[] project(Vec pSysPos) {
        Vector centerOfSystem = Vector.v(getSvs().offSetX,getSvs().offSetY+(getSvs().screenWidth/2d),getSvs().offSetZ-(getSvs().screenHeight/2d));
        Vector centerOfRotation = Vector.add(centerOfSystem);

        pSysPos = Vector.add(pSysPos.scale(getSvs().scale), centerOfSystem);

        pSysPos = rotatePv(pSysPos, centerOfRotation, getSvs().angleHorizontal, Axis.Z);
        //pSysPos = rotatePv(pSysPos, centerOfSystem, svs.angleX, Axis.X);
        pSysPos = rotatePv(pSysPos, centerOfRotation, getSvs().angleVertical, Axis.Y);

        int rX = 0;
        int rY = 0;
        rX += (int)(pSysPos.getY());
        rY -= (int)(pSysPos.getZ());
        //3d magic
        rX -= (int)(pSysPos.getX() * OFFSET_FOR_PROJECTION);
        rY += (int)(pSysPos.getX() * OFFSET_FOR_PROJECTION);

        int rZ = 0;

        rZ = (int)pSysPos.dot(VIEW_DIRECTION);

        return new int[] {rX,rY,rZ};
    }

    private static Vector rotatePv(Vec point, Vec pivot, double pAngle, Axis pAxis) {
        double angleRadians = Math.toRadians(pAngle);

        // Translate point to origin relative to pivot
        double px = point.getX() - pivot.getX();
        double py = point.getY() - pivot.getY();
        double pz = point.getZ() - pivot.getZ();

        double x = px, y = py, z = pz;

        switch (pAxis) {
            case X -> {
                y = py * Math.cos(angleRadians) - pz * Math.sin(angleRadians);
                z = py * Math.sin(angleRadians) + pz * Math.cos(angleRadians);
            }
            case Y -> {
                x = px * Math.cos(angleRadians) + pz * Math.sin(angleRadians);
                z = -px * Math.sin(angleRadians) + pz * Math.cos(angleRadians);
            }
            case Z -> {
                x = px * Math.cos(angleRadians) - py * Math.sin(angleRadians);
                y = px * Math.sin(angleRadians) + py * Math.cos(angleRadians);
            }
        }

        // Translate point back to original position relative to pivot
        return new Vector(x + pivot.getX(), y + pivot.getY(), z + pivot.getZ());
    }


    public static class SimpleViewState extends ViewState {
        //todo make everything private and use getset; setAngle(newangle,axis){bound it}; scale(change){converge}
        //public double angleX = 0;
        public double angleVertical = 0;
        public double angleHorizontal = 0;

        public double offSetX = 0;
        public double offSetY = 0;
        public double offSetZ = 0;

        public double scale = 1;
    }
}
