package com.triengine;

public class ViewState {
    public Vec viewPlaneNormal;  // Direction camera is facing
    public Vec viewPlaneMiddle;  // Camera position in 3D space
    public Vec viewUp;           // "Up" direction for the camera
    public float fieldOfView;    // Field of view (if perspective)
    public float nearClip;       // Near clipping plane
    public float farClip;        // Far clipping plane

    public int screenWidth = 0;
    public int screenHeight = 0;

    public ViewState(){
        this(
                Vec.o().x(-1).y(-1).z(-1),
                Vec.o().x(200).y(200).z(200),
                Vec.o().z(1),
                90f,
                0f,
                100_000f
        );
    }

    public ViewState(Vec viewPlaneNormal, Vec viewPlaneMiddle, Vec viewUp, float fieldOfView, float nearClip, float farClip) {
        this.viewPlaneNormal = viewPlaneNormal;
        this.viewPlaneMiddle = viewPlaneMiddle;
        this.viewUp = viewUp;
        this.fieldOfView = fieldOfView;
        this.nearClip = nearClip;
        this.farClip = farClip;
    }
}
