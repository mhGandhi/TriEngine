package com.triengine.projectors;

import com.triengine.Axis;
import com.triengine.Vec;
import com.triengine.projectors.viewstates.ViewState;

public class SimpleProjector extends Projector{
    public final SimpleViewState svs;

    public SimpleProjector() {
        this(new SimpleViewState());
    }
    public SimpleProjector(SimpleViewState svs) {
        this.svs = svs;
    }

    @Override
    public int[] project(Vec pSysPos) {
        Vec centerOfView = Vec.o().x(svs.offSetX).y(svs.offSetY).z(svs.offSetZ);
        pSysPos = Vec.add(pSysPos, centerOfView);

        pSysPos = rotatePv(pSysPos, centerOfView, svs.angleX, Axis.X);
        pSysPos = rotatePv(pSysPos, centerOfView, svs.angleY, Axis.Y);
        pSysPos = rotatePv(pSysPos, centerOfView, svs.angleZ, Axis.Z);

        int rX = 0;
        int rY = 0;
        int rZ = 0;

        rX += (int)(pSysPos.y);
        rY -= (int)(pSysPos.z);

        rX -= (int)(pSysPos.x * (0.707/2));
        rY += (int)(pSysPos.x * (0.707/2));


        Vec frontDir = Vec.o().x(1).y((0.707/2)).z((0.707/2)).normalize();
        rZ = (int)pSysPos.dot(frontDir);

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
        public double angleX = 0;
        public double angleY = 0;
        public double angleZ = 0;

        public double offSetX = 600;
        public double offSetY = 500;
        public double offSetZ = 0;
    }
}
