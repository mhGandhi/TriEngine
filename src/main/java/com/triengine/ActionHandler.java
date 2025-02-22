package com.triengine;

import com.triengine.projectors.SimpleProjector;
import com.triengine.projectors.viewstates.ViewState;

import java.awt.event.*;

public class ActionHandler implements MouseMotionListener, ComponentListener, MouseListener, MouseWheelListener, ActionListener {
    private final ViewState viewState;
    private final App app;

    public ActionHandler(App pApp, ViewState viewState) {
        this.app = pApp;
        this.viewState = viewState;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
    int currentMB = 0;
    @Override
    public void mousePressed(MouseEvent e) {
        currentMB = e.getButton();
    }

    int[] lastPos = {0,0};
    @Override
    public void mouseDragged(MouseEvent e) {
        if(viewState instanceof SimpleProjector.SimpleViewState svs){//todo put into viewstate somehow maybe
            if(currentMB==2){
                svs.angleHorizontal += e.getX()- lastPos[0];
                svs.angleVertical += e.getY()- lastPos[1];

                if(svs.angleVertical >359)svs.angleVertical -=360;
                if(svs.angleVertical <0)svs.angleVertical +=360;
                if(svs.angleHorizontal >359)svs.angleHorizontal -=360;
                if(svs.angleHorizontal <0)svs.angleHorizontal +=360;//todo put into viewstate
            }
            else if(currentMB==1){
                svs.offSetY += e.getX()- lastPos[0];
                svs.offSetX += e.getY()- lastPos[1];
            }

            //System.out.println();
            //System.out.println(svs.angleHorizontal);
            //System.out.println(svs.offSetY);
        }

        if(currentMB==3){//todo temp for testt
            app.elevateC(e.getY()- lastPos[1]);
        }

        lastPos[0] = e.getX();
        lastPos[1] = e.getY();
        viewState.mousePos[0] = e.getX();
        viewState.mousePos[1] = e.getY();

        app.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastPos[0] = e.getX();
        lastPos[1] = e.getY();
        viewState.mousePos[0] = e.getX();
        viewState.mousePos[1] = e.getY();

        app.repaint();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        viewState.screenHeight = e.getComponent().getHeight();
        viewState.screenWidth = e.getComponent().getWidth();
        app.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //System.out.println();
        //System.out.println("type: "+e.getScrollType());
        //System.out.println("scroll amount: "+e.getScrollAmount());
        //System.out.println("units to scroll: "+e.getUnitsToScroll());
        //System.out.println("wheel rotation: "+e.getWheelRotation());
        //System.out.println("precise rotation: "+e.getPreciseWheelRotation());

        if(viewState instanceof SimpleProjector.SimpleViewState svs){
            if(e.getWheelRotation()>0){//zoom out
                svs.scale = svs.scale*0.9;
            }else{
                svs.scale = svs.scale*1.1;
            }
            //System.out.println(svs.scale);
        }
        app.repaint();
    }


    @Override
    public void componentMoved(ComponentEvent e) {

    }
    @Override
    public void componentShown(ComponentEvent e) {

    }
    @Override
    public void componentHidden(ComponentEvent e) {

    }
    @Override
    public void mouseClicked(MouseEvent e) {

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
