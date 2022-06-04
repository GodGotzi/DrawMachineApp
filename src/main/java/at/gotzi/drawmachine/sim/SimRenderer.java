package at.gotzi.drawmachine.sim;

import at.gotzi.drawmachine.view.ResizeHandler;

import javax.swing.*;
import java.awt.*;

public class SimRenderer extends JPanel implements Renderer {
    private final JPanel panel;

    public SimRenderer() {
        this.panel = new JPanel();
        this.panel.setBackground(Color.WHITE);

        add(panel);
        setBackground(Color.LIGHT_GRAY);
        addComponentListener(new ResizeHandler(this, this::resizeAction));
    }

    private void resizeAction(int width, int height) {
        if (width > height) {
            int newSize = height-10;
            panel.setBounds((width - newSize) / 2, 5, newSize, newSize);
        } else {
            int newSize = width-10;
            panel.setBounds(5, (height - newSize) / 2, newSize, newSize);
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void nextStep() {

    }
}
