package com2002.team021.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginScreen.java
 */

public class LoginScreenTwo {

    static JFrame loginScreen;
    static JButton secretary, hygienist, dentist, exit, okay, cancel;
    static JTextField name, username;
    static Container contentPane;

    public static void loginDialog(String title) {

        if (title == "Secretary Log In") {
                loginScreen.dispose();
                new ChoiceScreenTwo("Secretary Choice");
        }
        else if ((title == "Hygienist Log In") || (title == "Dentist Log In")){
                loginScreen.dispose();
                new ChoiceScreenTwo("Practitioner Choice");
        }

    }

    public static void main(String[] args) {

        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}


        loginScreen = new JFrame("Login Screen");
        loginScreen.setSize(900,500);
        loginScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = loginScreen.getContentPane();
        contentPane.setLayout(null);

        //Create buttons
        secretary = new JButton("Secretary");
        hygienist = new JButton("Hygienist");
        dentist = new JButton("Dentist");
        exit = new JButton("Exit");

        //Add Listeners
        secretary.addActionListener(new btnSec_Action());
        hygienist.addActionListener(new btnHyg_Action());
        dentist.addActionListener(new btnDent_Action());
        exit.addActionListener(new btnExit_Action());

        //Add buttons

        contentPane.add(secretary);
        contentPane.add(hygienist);
        contentPane.add(dentist);
        contentPane.add(exit);

        //Set Bounds
        secretary.setBounds(300, 100, 300, 25);
        hygienist.setBounds(300, 175, 300, 25);
        dentist.setBounds(300, 255, 300, 25);
        exit.setBounds(300, 330, 300, 25);

        //Make fram visible
        loginScreen.setResizable(false);
        loginScreen.setVisible(true);
    }

    static class btnSec_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //loginScreen.dispose();
            loginDialog("Secretary Log In");
        }
    }

    static class btnHyg_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //loginScreen.dispose();
            loginDialog("Hygienist Log In");
        }
    }

    static class btnDent_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //loginScreen.dispose();
            loginDialog("Dentist Log In");
        }
    }

    static class btnExit_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
