package com.triengine;

public class ViewState {
    public final Vec viewPlaneNormal;
    public final Vec viewPlaneOff;

    public ViewState(Vec viewPlaneNormal, Vec viewPlaneOff) {
        this.viewPlaneNormal = viewPlaneNormal;
        this.viewPlaneOff = viewPlaneOff;
    }
}
