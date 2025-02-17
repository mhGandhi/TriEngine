package com.triengine;

import com.triengine.projectors.Projector;
import com.triengine.projectors.SimpleProjector;
import com.triengine.vectors.SetVector;

import javax.swing.*;
import java.awt.*;

public class App {
    Environment env;
    Projector projector;

    public App(){
        projector = new SimpleProjector();
        ActionHandler actionListener = new ActionHandler(this, projector.viewState);


        env = new Environment(actionListener, projector);
        {
            JFrame frame = new JFrame();
            frame.add(env);
            frame.setBackground(Color.green);

            frame.setSize(1000, 500);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }

    public void setProjector(Projector p){
        this.projector = p;
    }

    public void repaint() {
        env.repaint();
        //System.out.println("REPAINT");
    }

    public void elevateC(int elev) {
        for(SetVector v : env.c){
            v.z-=elev;
        }
        env.repaint();
    }
}
