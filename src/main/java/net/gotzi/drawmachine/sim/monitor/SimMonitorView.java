package net.gotzi.drawmachine.sim.monitor;

import net.gotzi.drawmachine.DrawMachineSim;
import net.gotzi.drawmachine.handler.MouseCursorHandler;
import net.gotzi.drawmachine.error.UnsupportedValue;
import net.gotzi.drawmachine.sim.Simulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class SimMonitorView implements SimMonitor {

    private final Simulation simulation;
    private final String maxAllowedStepsStr =
            DrawMachineSim.getInstance().getConfig().get("max_simulation_steps");

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
        stepProgress.setText(String.format("%0" + maxAllowedStepsStr.length() + "d/%" +
                maxAllowedStepsStr.length() + "d", 0, 10000));
        simSpeedValueLabel.setText(String.format("%.2f x", 10 * Math.pow(10, -1)));

        simSpeedValueLabel.setHorizontalAlignment(JLabel.CENTER);
        simSpeedValueLabel.setVerticalAlignment(JLabel.CENTER);

        stepProgress.setHorizontalAlignment(JLabel.CENTER);
        stepProgress.setVerticalAlignment(JLabel.CENTER);

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
        simSpeedSlider.addMouseListener(new MouseCursorHandler(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)));

        runButton.addActionListener(this::run);
        runButton.addMouseListener(new MouseCursorHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        stopButton.addActionListener(this::stop);
        stopButton.addMouseListener(new MouseCursorHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        resetViewButton.addActionListener(this::resetView);
        resetViewButton.addMouseListener(new MouseCursorHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        resetCanvasButton.addActionListener(this::resetCanvas);
        resetCanvasButton.addMouseListener(new MouseCursorHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        fastMode.addMouseListener(new MouseCursorHandler(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)));

        simStepSpinner.addChangeListener(this::updateSimSteps);
    }

    @Override
    public void updateProgress(int progress) {
        this.progressBar.setValue(progress);
    }

    @Override
    public void updateSteps(int steps) {
        int progress = (int) ((float)steps/(float) atomicSimSteps.get() * 100);
        stepProgress.setText(String.format("%0" + maxAllowedStepsStr.length() + "d/%" +
                maxAllowedStepsStr.length() + "d", steps, getSimulationSteps().get()));
        updateProgress(progress);
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
        return fastMode.isSelected();
    }

    private void updateSimSteps(ChangeEvent ignored) {
        NumberFormat nf = DecimalFormat.getInstance(new Locale("en", "US"));
        int maxAllowed = Integer.parseInt(this.maxAllowedStepsStr);
        int value;

        try {
            value = Integer.parseInt(simStepSpinner.getValue().toString());
            if (maxAllowed < value) {
                simStepSpinner.setValue(maxAllowed);
                new UnsupportedValue(DrawMachineSim.getInstance().getWindow(), "Value is too high Max: " + nf.format(maxAllowed));
                return;
            }
        } catch (Exception e) {
            return;
        }

        stepProgress.setText(String.format("%0" + maxAllowedStepsStr.length() + "d/%" +
                maxAllowedStepsStr.length() + "d", simulation.getCurrentSteps(), value));
        atomicSimSteps.set(value);
    }

    private void updateSimSpeed(ChangeEvent changeEvent) {
        atomicSimSpeed.set(this.simSpeedSlider.getValue());
        if (this.simSpeedSlider.getValue() > 999)
            simSpeedValueLabel.setText("∞ x");
        else
            simSpeedValueLabel.setText(String.format("%.2f ", this.simSpeedSlider.getValue() * Math.pow(10, -1)) + "x");
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

    public JPanel getView() {
        return view;
    }
}