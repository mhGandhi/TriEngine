package com.triengine;

import com.triengine.projectors.SimpleProjector;
import com.triengine.projectors.viewstates.ViewState;

import java.awt.event.*;

public class ActionListener implements MouseMotionListener, ComponentListener {
    private final ViewState viewState;
    private final App app;

    public ActionListener(App pApp, ViewState viewState) {
        this.app = pApp;
        this.viewState = viewState;
    }

    int[] lastPos = {0,0};
    @Override
    public void mouseDragged(MouseEvent e) {
        if(viewState instanceof SimpleProjector.SimpleViewState svs){
            svs.angleZ += e.getX()- lastPos[0];
            svs.angleY += e.getY()- lastPos[1];

            System.out.println(svs.angleZ);
        }

        lastPos[0] = e.getX();
        lastPos[1] = e.getY();

        app.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastPos[0] = e.getX();
        lastPos[1] = e.getY();
    }

    /**
     * Invoked when the component's size changes.
     *
     * @param e the event to be processed
     */
    @Override
    public void componentResized(ComponentEvent e) {
        viewState.screenHeight = e.getComponent().getHeight();
        viewState.screenWidth = e.getComponent().getWidth();
        app.repaint();
    }

    /**
     * Invoked when the component's position changes.
     *
     * @param e the event to be processed
     */
    @Override
    public void componentMoved(ComponentEvent e) {

    }

    /**
     * Invoked when the component has been made visible.
     *
     * @param e the event to be processed
     */
    @Override
    public void componentShown(ComponentEvent e) {

    }

    /**
     * Invoked when the component has been made invisible.
     *
     * @param e the event to be processed
     */
    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
