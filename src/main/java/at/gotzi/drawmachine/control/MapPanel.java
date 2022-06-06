package at.gotzi.drawmachine.control;

import at.gotzi.drawmachine.sim.view.SimMonitor;
import at.gotzi.drawmachine.sim.alogrithm.SimRenderer;
import at.gotzi.drawmachine.sim.alogrithm.Canvas;
import at.gotzi.drawmachine.sim.view.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

public class MapPanel extends JPanel {
    private final Dimension dimension;
    private final IMapLayout mapLayout;
    private final SimRenderer simRenderer;

    private Simulation simulation;
    private Canvas paper;

    public MapPanel(Dimension dimension, Simulation simulation, int maxScrollSize, int minScrollSize, int startScroll) {
        this.dimension = dimension;
        this.paper = new Canvas(dimension.width, dimension.height, Color.BLACK.getRGB());
        this.simRenderer = new SimRenderer(paper, this::update);
        this.mapLayout = new MapLayout(this, paper, maxScrollSize, minScrollSize, startScroll);
        this.simulation = simulation;
        build();
    }

    private void build() {
        setBackground(Color.DARK_GRAY);
        setLayout((LayoutManager) mapLayout);
        addMouseListener((MouseListener) mapLayout);
        addMouseMotionListener((MouseMotionListener) mapLayout);
        addMouseWheelListener((MouseWheelListener) mapLayout);
    }

    private void update(Integer step) {
        mapLayout.repaint();
        this.simulation.updateSteps(step);
    }

    public SimRenderer getSimRenderer() {
        return simRenderer;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public IMapLayout getMapLayout() {
        return mapLayout;
    }
}