package at.gotzi.drawmachine.sim;

import at.gotzi.drawmachine.view.Resizeable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SimView extends JSplitPane implements Simulation {

    private SimRenderer renderer;
    private SimMonitor simMonitor;

    private boolean running = false;

    public SimView() {
        try {
            buildImageHolder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBackground(Color.GRAY);
        setOrientation(JSplitPane.VERTICAL_SPLIT);
        setDividerSize(1);
    }

    private void buildImageHolder() throws IOException {


        SimDrawer simDrawer = new SimDrawer();
        this.renderer = new SimRenderer();
        this.simMonitor = new SimConfigView(this);

        setTopComponent(renderer.getPanel());
        setBottomComponent(((SimConfigView) simMonitor).getView());
        setEnabled(false);
    }

    public SimMonitor getSimulationSettings() {
        return simMonitor;
    }

    @Override
    public void run(SimMonitor simMonitor) {
        this.running = true;
    }

    @Override
    public int getCurrentSteps() {
        return 0;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}