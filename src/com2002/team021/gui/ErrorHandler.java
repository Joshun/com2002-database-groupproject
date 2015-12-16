package com2002.team021.gui;

import javax.swing.*;

/**
 * Created by joshua on 15/12/15.
 */
public class ErrorHandler {
    private JFrame parentFrame;
    private boolean showDebug;

    public ErrorHandler(JFrame parentFrame, boolean showDebug) {
        this.parentFrame = parentFrame;
        this.showDebug = showDebug;
    }

    public ErrorHandler(JFrame parentFrame) {
        this(parentFrame, false);
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showDialog(String message, Exception e) {
        System.out.println("Error: " + message + " " + e.toString());
        if (showDebug) {
            showDialog(message + " (" + e.toString() + ")");
        }
        else {
            showDialog(message);
        }
    }
}
