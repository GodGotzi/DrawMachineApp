package net.gotzi.drawmachine.sim.monitor;

import net.gotzi.drawmachine.DrawMachineSim;
import net.gotzi.drawmachine.handler.MouseHandler;
import net.gotzi.drawmachine.error.UnsupportedValue;
import net.gotzi.drawmachine.sim.Simulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class SimMonitorView implements SimMonitor {

    private final Simulation simulation;

    private JPanel view;
    private JSlider simSpeedSlider;
    private JLabel simSpeedValueLabel;
    private JProgressBar progressBar;
    private JButton stopButton;
    private JButton runButton;
    private JLabel speedLabel;
    private JLabel stepLabel;
    private JSpinner simStepSpinner;
    private JLabel stepProgress;
    private JButton resetCanvasButton;
    private JButton resetViewButton;
    private JCheckBox fastMode;
    private final AtomicInteger atomicSimSpeed;
    private final AtomicInteger atomicSimSteps;

    public SimMonitorView(Simulation simulation) {
        this.simulation = simulation;
        this.atomicSimSpeed = new AtomicInteger();
        this.atomicSimSteps = new AtomicInteger();

        runButton.setText("Run");
        stopButton.setText("Stop");
        resetViewButton.setText("Reset View");
        resetCanvasButton.setText("Reset Canvas");
        speedLabel.setText("Simulation Speed");
        stepLabel.setText("Simulation Steps");
        stepProgress.setText("0/10000");
        simSpeedValueLabel.setText(String.format("%.2f x", 10 * Math.pow(10, -1)));

        simSpeedValueLabel.setHorizontalAlignment(JLabel.CENTER);
        simSpeedValueLabel.setVerticalAlignment(JLabel.CENTER);
        simSpeedValueLabel.setPreferredSize(new Dimension(150, 0));
        stepProgress.setHorizontalAlignment(JLabel.CENTER);
        stepProgress.setVerticalAlignment(JLabel.CENTER);
        stepProgress.setPreferredSize(new Dimension(150, 0));

        simSpeedSlider.setMinimum(10);
        simSpeedSlider.setValue(10);
        simSpeedSlider.setMaximum(1000);
        atomicSimSpeed.set(10);

        simStepSpinner.setValue(10000);
        atomicSimSteps.set(10000);

        progressBar.setMinimum(0);
        progressBar.setValue(0);
        progressBar.setMaximum(100);


        addListeners();
    }

    private void addListeners() {
        simSpeedSlider.addChangeListener(this::updateSimSpeed);
        simSpeedSlider.addMouseListener(new MouseHandler(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)));

        runButton.addActionListener(this::run);
        runButton.addMouseListener(new MouseHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        stopButton.addActionListener(this::stop);
        stopButton.addMouseListener(new MouseHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        resetViewButton.addActionListener(this::resetView);
        resetViewButton.addMouseListener(new MouseHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        resetCanvasButton.addActionListener(this::resetCanvas);
        resetCanvasButton.addMouseListener(new MouseHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        fastMode.addActionListener(this::switchFastMode);
        fastMode.addMouseListener(new MouseHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        simStepSpinner.addChangeListener(this::updateSimSteps);
    }

    private void switchFastMode(ActionEvent event) {
        //event.get
    }

    private void updateSimSteps(ChangeEvent ignored) {
        NumberFormat nf = DecimalFormat.getInstance(new Locale("en", "US"));
        int maxAllowed = Integer.parseInt(DrawMachineSim.getInstance().getConfig().get("max_simulation_steps"));
        int value;

        value = Integer.parseInt(simStepSpinner.getValue().toString());
        if (maxAllowed < value) {
            simStepSpinner.setValue(maxAllowed);
            new UnsupportedValue(DrawMachineSim.getInstance().getWindow(), "Value is too high Max: " + nf.format(maxAllowed));
        }

        stepProgress.setText(simulation.getCurrentSteps() + "/" + value);
        atomicSimSteps.set(value);
    }

    private void updateSimSpeed(ChangeEvent changeEvent) {
        atomicSimSpeed.set(this.simSpeedSlider.getValue());
        if (this.simSpeedSlider.getValue() > 999)
            simSpeedValueLabel.setText("∞ x");
        else
            simSpeedValueLabel.setText(String.format("%.2f ", this.simSpeedSlider.getValue() * Math.pow(10, -1)) + "x");
    }

    private synchronized JProgressBar getProgressBar() {
        return this.progressBar;
    }

    private void run(ActionEvent actionEvent) {
        if (this.simulation.isRunning()) return;
        this.simulation.run();
    }

    private void stop(ActionEvent actionEvent) {
        if (!this.simulation.isRunning()) return;
        this.simulation.stop();
    }

    private void resetView(ActionEvent actionEvent) {
        this.simulation.resetView();
    }

    private void resetCanvas(ActionEvent actionEvent) {
        if (this.simulation.isRunning()) return;
        this.simulation.resetCanvas();
    }

    public JPanel getPanel() {
        return view;
    }

    @Override
    public AtomicInteger getSimulationSpeed() {
        return atomicSimSpeed;
    }

    @Override
    public AtomicInteger getSimulationSteps() {
        return atomicSimSteps;
    }

    @Override
    public boolean isFastMode() {
        //fastMode.addChangeListener(e -> System.out.println(e.getSource()));
        return fastMode.isSelected();
    }
    @Override
    public void updateProgress(int progress) {
        this.progressBar.setValue(progress);
    }

    @Override
    public void updateSteps(int steps) {
        int progress = (int) ((float)steps/(float) atomicSimSteps.get() * 100);
        stepProgress.setText(steps + "/" + atomicSimSteps.get());
        updateProgress(progress);
    }
}