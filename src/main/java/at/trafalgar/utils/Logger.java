package at.trafalgar.utils;

import at.trafalgar.Main;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Logger {
    public static void log(String message, boolean bold) {
        Thread.runAsync(() -> {
            SwingUtilities.invokeLater(() -> {
                JTextPane pane = Main.logArea;
                StyledDocument document = pane.getStyledDocument();
                SimpleAttributeSet attributeSet = new SimpleAttributeSet();
                if (bold) {
                    StyleConstants.setBold(attributeSet, true);
                }
                try {
                    document.insertString(document.getLength(), message + "\n", attributeSet);
                    pane.setCaretPosition(document.getLength());
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public static void log(String message) {
        Thread.runAsync(() -> {
            log(message, false);
        });
    }
}
