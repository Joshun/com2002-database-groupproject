package com2002.team021.gui;

import javax.swing.*;

/**
 * Created by joshua on 15/12/15.
 */
public class ErrorHandler {
    private JFrame parentFrame;

    public ErrorHandler(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showDialog(String message, Exception e) {
        showDialog(message + " (" + e.toString() + ")");
    }
}
