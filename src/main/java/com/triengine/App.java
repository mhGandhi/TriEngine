package com.triengine;

import com.triengine.projectors.Projector;
import com.triengine.projectors.SimpleProjector;
import com.triengine.projectors.viewstates.ViewState;

import javax.swing.*;
import java.awt.*;

public class App {
    Environment env;

    public App(){
        SimpleProjector projector = new SimpleProjector();
        ActionListener actionListener = new ActionListener(this, projector.svs);


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



    public void repaint() {
        env.repaint();
        System.out.println("REPAINT");
    }
}
