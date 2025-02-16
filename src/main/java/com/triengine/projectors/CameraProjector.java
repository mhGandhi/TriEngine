package com.triengine.projectors;

import com.triengine.Vec;
import com.triengine.ViewState;

public class CameraProjector extends Projector {
    private final ViewState vs;

    public CameraProjector(ViewState pViewState) {
        this.vs = pViewState;
    }

    @Override
    public int[] project(Vec pSysPos) {
        // 1. Calculate the camera-space position by transforming the world position (pSysPos)
        // Subtract the camera position (viewPlaneMiddle) from the world position (pSysPos)
        Vec cameraSpacePos = pSysPos.subtract(vs.viewPlaneMiddle);

        // 2. Compute the camera's right vector (cross product of viewNormal and viewUp)
        // This gives the "right" direction relative to the camera.
        Vec right = vs.viewPlaneNormal.cross(vs.viewUp);

        // 3. Normalize the view vectors (viewPlaneNormal, viewUp, right)
        vs.viewPlaneNormal.normalize();
        vs.viewUp.normalize();
        right.normalize();

        // 4. Apply the perspective projection formula: (x/z, y/z)
        // Perspective projection: divide the x and y camera-space coordinates by z (depth)
        float x = (float) cameraSpacePos.dot(right);  // projection onto the right vector (x-axis)
        float y = (float) cameraSpacePos.dot(vs.viewUp);  // projection onto the up vector (y-axis)
        float z = (float) cameraSpacePos.dot(vs.viewPlaneNormal);  // projection onto the view normal (z-axis)

        // 5. Apply the field of view (FOV) and near/far clipping planes
        if (z < vs.nearClip || z > vs.farClip) {
            return null;  // If the point is outside the clipping range, return null (not visible)
        }

        // Normalize by the z component (perspective division)
        float scale = (float) Math.tan(Math.toRadians(vs.fieldOfView) / 2);
        x = x / z * scale;  // Scale based on depth (z)
        y = y / z * scale;  // Scale based on depth (z)

        // 6. Map the normalized device coordinates to screen space (viewport transformation)
        float aspectRatio = (float) vs.screenWidth / vs.screenHeight;  // Assuming screen width and height
        x = x * aspectRatio;  // Apply the aspect ratio to avoid distortion

        // 7. Convert the coordinates into screen space
        // Assuming the screen space is from (0, 0) to (screenWidth, screenHeight)
        int screenX = (int) ((x + 1) * 0.5 * vs.screenWidth);  // Map to screen width
        int screenY = (int) ((-y + 1) * 0.5 * vs.screenHeight);  // Map to screen height

        // Return the screen coordinates as an array
        return new int[] {screenX, screenY};
    }

}
