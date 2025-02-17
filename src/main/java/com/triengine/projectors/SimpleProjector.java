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
        pSysPos = rotate(pSysPos, svs.angleX, Axis.X);
        pSysPos = rotate(pSysPos, svs.angleY, Axis.Y);
        pSysPos = rotate(pSysPos, svs.angleZ, Axis.Z);

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

    public static class SimpleViewState extends ViewState {
        public double angleX = 0;
        public double angleY = 0;
        public double angleZ = 0;
    }
}
