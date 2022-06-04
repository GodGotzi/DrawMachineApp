package at.gotzi.drawmachine.view.file;

import at.gotzi.drawmachine.DrawMachineCA;
import at.gotzi.drawmachine.view.Resizeable;

import javax.swing.*;
import java.awt.*;

public class EditorPanel extends JPanel implements Resizeable {

    public EditorPanel() {
        setBackground(Color.BLACK);
        setVisible(true);
    }

    public void off() {
        DrawMachineCA.LOGGER.info("disabled");
        setVisible(false);
    }


    public void on() {
        DrawMachineCA.LOGGER.info("enabled");
        setVisible(true);
    }

    @Override
    public void updateBounds(int width, int height) {
        setBounds(0, 0, width > 500 ? width/3 : getWidth(), height);
    }
}