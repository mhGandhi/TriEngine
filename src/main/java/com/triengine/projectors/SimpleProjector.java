package com.triengine.projectors;

import com.triengine.Axis;
import com.triengine.Vec;
import com.triengine.projectors.viewstates.ViewState;

public class SimpleProjector extends Projector{

    public static final double OFFSET_FOR_PROJECTION = (0.707 / 2);
    public static final Vec VIEW_DIRECTION = Vec.v(1, OFFSET_FOR_PROJECTION, OFFSET_FOR_PROJECTION).normalize();


    public SimpleProjector() {
        this(new SimpleViewState());
    }
    public SimpleProjector(SimpleViewState svs) {
        super(svs);
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
        Vec centerOfSystem = Vec.v(getSvs().offSetX,getSvs().offSetY+(getSvs().screenWidth/2d),getSvs().offSetZ-(getSvs().screenHeight/2d));
        Vec centerOfRotation = Vec.add(centerOfSystem);

        pSysPos = Vec.add(pSysPos.scale(getSvs().scale), centerOfSystem);

        pSysPos = rotatePv(pSysPos, centerOfRotation, getSvs().angleHorizontal, Axis.Z);
        //pSysPos = rotatePv(pSysPos, centerOfSystem, svs.angleX, Axis.X);
        pSysPos = rotatePv(pSysPos, centerOfRotation, getSvs().angleVertical, Axis.Y);

        int rX = 0;
        int rY = 0;
        rX += (int)(pSysPos.y);
        rY -= (int)(pSysPos.z);
        //3d magic
        rX -= (int)(pSysPos.x * OFFSET_FOR_PROJECTION);
        rY += (int)(pSysPos.x * OFFSET_FOR_PROJECTION);

        int rZ = 0;

        rZ = (int)pSysPos.dot(VIEW_DIRECTION);

        return new int[] {rX,rY,rZ};
    }

    private static Vec rotate(Vec point, double pAngle, Axis pAxis){
        double angleRadians = Math.toRadians(pAngle);
        switch (pAxis){
            case X -> {
                double y = point.y * Math.cos(angleRadians) - point.z * Math.sin(angleRadians);
                double z = point.y * Math.sin(angleRadians) + point.z * Math.cos(angleRadians);
                return new Vec(point.x, y, z);
            }
            case Y -> {
                double x = point.x * Math.cos(angleRadians) + point.z * Math.sin(angleRadians);
                double z = -point.x * Math.sin(angleRadians) + point.z * Math.cos(angleRadians);
                return new Vec(x, point.y, z);
            }
            case Z -> {
                double x = point.x * Math.cos(angleRadians) - point.y * Math.sin(angleRadians);
                double y = point.x * Math.sin(angleRadians) + point.y * Math.cos(angleRadians);
                return new Vec(x, y, point.z);
            }
        }

        System.err.println("Invalid Axis, reverting to nullvector");
        return Vec.o();
    }

    private static Vec rotatePv(Vec point, Vec pivot, double pAngle, Axis pAxis) {
        double angleRadians = Math.toRadians(pAngle);

        // Translate point to origin relative to pivot
        double px = point.x - pivot.x;
        double py = point.y - pivot.y;
        double pz = point.z - pivot.z;

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
        return new Vec(x + pivot.x, y + pivot.y, z + pivot.z);
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
