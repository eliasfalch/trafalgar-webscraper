package at.trafalgar;

import at.trafalgar.gui.GUI;
import at.trafalgar.utils.Thread;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class Main {
    public static JTextPane logArea;
    public static JTextField patternField;

    public static final Font font = new Font("Calibri", Font.PLAIN, 14);

    public static void main(String[] args) {
        Thread.runAsync(() -> {
            try {
                UIManager.setLookAndFeel(new FlatOneDarkIJTheme());
                UIManager.getDefaults().keySet().stream()
                        .filter(key -> UIManager.get(key) instanceof FontUIResource)
                        .forEach(key -> UIManager.put(key, new FontUIResource(font)));
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
            SwingUtilities.invokeLater(GUI::createGUI);
        });
    }
}