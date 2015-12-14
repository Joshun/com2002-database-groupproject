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
        name = new JTextField();
        username = new JTextField();
        JPasswordField password = new JPasswordField();
        final JComponent[] fields = new JComponent[] {
                new JLabel("Name"),
                name,
                new JLabel(" "),
                new JLabel("Username"),
                username,
                new JLabel(" "),
                new JLabel("Password"),
                password
        };
        JOptionPane a = new JOptionPane();
        a.showMessageDialog(loginScreen, fields, title, JOptionPane.PLAIN_MESSAGE);



        System.out.println("You entered " +
                name.getText() + ", " +
                username.getText() + ", " +
                password.getText());

        if (name.getText().trim().length() == 0) {
            a.showMessageDialog(loginScreen, "Must Fill in text field", "Empty Field", JOptionPane.ERROR_MESSAGE);
            loginDialog(title);
        }
        else if (username.getText().trim().length() == 0) {
            a.showMessageDialog(loginScreen, "Must Fill in text field", "Empty Field", JOptionPane.ERROR_MESSAGE);
            loginDialog(title);
        }
        else if (password.getText().trim().length() == 0) {
            a.showMessageDialog(loginScreen, "Must Fill in text field", "Empty Field", JOptionPane.ERROR_MESSAGE);
            loginDialog(title);
        }
        else {
            if (title == "Secretary Log In") {
                loginScreen.dispose();
                new ChoiceScreenTwo("Secretary Choice");
            }
            else if ((title == "Hygienist Log In") || (title == "Dentist Log In")){
                loginScreen.dispose();
                new ChoiceScreenTwo("Practitioner Choice");
            }

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
