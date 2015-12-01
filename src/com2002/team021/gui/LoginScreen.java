package com2002.team021.gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginScreen.java
 */

public class LoginScreen extends JFrame {
    public LoginScreen() {
        setTitle("Login Screen");
//        setSize(300, 300);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(new JButton("Secretary"));
        contentPane.add(new JButton("Hygienist"));
        contentPane.add(new JButton("Dentist"));
        contentPane.add(new JButton("Exit"));
        this.pack();
    }

    public static void main(String[] args) {
        JFrame loginScreen = new LoginScreen();
        loginScreen.setVisible(true);
    }
}
