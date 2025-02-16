package com.triengine.projectors;

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
        pSysPos = rotateAroundX(pSysPos, Math.toRadians(svs.angleX));
        pSysPos = rotateAroundY(pSysPos, Math.toRadians(svs.angleY));
        pSysPos = rotateAroundZ(pSysPos, Math.toRadians(svs.angleZ));

        int rX = 0;
        int rY = 0;

        rX += (int)(pSysPos.y);
        rY -= (int)(pSysPos.z);

        rX -= (int)(pSysPos.x * 0.5);
        rY += (int)(pSysPos.x * 0.5);

        return new int[] {rX,rY};
    }

    private static Vec rotateAroundX(Vec point, double angleRadians) {
        double y = point.y * Math.cos(angleRadians) - point.z * Math.sin(angleRadians);
        double z = point.y * Math.sin(angleRadians) + point.z * Math.cos(angleRadians);

        // Return the new point
        return new Vec(point.x, y, z);
    }
    private static Vec rotateAroundY(Vec point, double angleRadians) {
        // Apply rotation matrix for Y-axis
        double x = point.x * Math.cos(angleRadians) + point.z * Math.sin(angleRadians);
        double z = -point.x * Math.sin(angleRadians) + point.z * Math.cos(angleRadians);

        // Return the new point
        return new Vec(x, point.y, z);
    }
    private static Vec rotateAroundZ(Vec point, double angleRadians) {
        // Apply rotation matrix for Z-axis
        double x = point.x * Math.cos(angleRadians) - point.y * Math.sin(angleRadians);
        double y = point.x * Math.sin(angleRadians) + point.y * Math.cos(angleRadians);

        // Return the new point
        return new Vec(x, y, point.z);
    }

    public static class SimpleViewState extends ViewState {
        public double angleX = 0;
        public double angleY = 0;
        public double angleZ = 0;
    }
}
