package view;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final Panel panel;

    public Window() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("ScanLine => L, SeedFill => scroll button, basic polygon for ScanLine and SeedFill => left mouse, ctrl + right mouse => orezavany polygon, shift + right mouse => orezavaci polygon, clipPolygon => H, delete => C");


        panel = new Panel();

        add(panel, BorderLayout.CENTER);
        setVisible(true);
        pack();

        setLocationRelativeTo(null);

        panel.setFocusable(true);
        panel.grabFocus();
    }

    public Panel getPanel() {
        return panel;
    }

}
