package com.triengine;

import com.triengine.projectors.SimpleProjector;
import com.triengine.projectors.viewstates.ViewState;

import java.awt.event.*;

public class ActionListener implements MouseMotionListener, ComponentListener, MouseListener {
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
            if(currentMB==2){
                svs.angleZ += e.getX()- lastPos[0];
                svs.angleY += e.getY()- lastPos[1];

                if(svs.angleY>359)svs.angleY-=360;
                if(svs.angleY<0)svs.angleY+=360;
                if(svs.angleZ>359)svs.angleZ-=360;
                if(svs.angleZ<0)svs.angleZ+=360;//todo put into viewstate
            }
            else if(currentMB==1){
                svs.offSetY += e.getX()- lastPos[0];
                svs.offSetX += e.getY()- lastPos[1];
            }

            //System.out.println(svs.angleZ);
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

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    int currentMB = 0;
    @Override
    public void mousePressed(MouseEvent e) {
        currentMB = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
