package com2002.team021.gui;

import com2002.team021.Practitioner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * ChoiceScreen.java         1.1 01/12/
 * 
 */


public class ChoiceScreen extends JFrame {

    private Container contentPane;
    private JButton viewCal, editCal, ptnMan;
//    private JFrame choiceScreen;
    private Practitioner practitioner;

    public ChoiceScreen() { this(null); }

    public ChoiceScreen(Practitioner practitioner) {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                new LoginScreen();
            }
        });


        this.practitioner = practitioner;

        //Look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }

//        choiceScreen = new JFrame();
        setSize(500,300);
        String windowTitle = "Welcome, ";
        if (practitioner != null) {
            windowTitle = windowTitle + practitioner.getRole() + " " + practitioner.getName();
        }
        else {
            windowTitle = windowTitle + "Secretary";
        }
        setTitle(windowTitle);

        contentPane = getContentPane();
        contentPane.setLayout(null);

        //Create buttons
        if (practitioner == null) {
            editCal = new JButton("View / Edit Calendar");
            ptnMan = new JButton("Patient Management");

            editCal.addActionListener(new btnEditCal_Action());
            ptnMan.addActionListener(new btnPtnMan_Action());

            contentPane.add(editCal);
            contentPane.add(ptnMan);

            editCal.setBounds(100, 112, 300, 25);
            ptnMan.setBounds(100, 163, 300, 25);
        }
        else if (practitioner.getRole().equals("Dentist") || practitioner.getRole().equals("Hygienist")) {
            viewCal = new JButton("View Calendar");

            viewCal.addActionListener(new btnEditCal_Action());

            contentPane.add(viewCal);

            viewCal.setBounds(100, 112, 300, 25);
        }

        setResizable(false);
        setVisible(true);
    }


    private class btnEditCal_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            if (practitioner != null) {
                new CalendarView(practitioner);
            }
            else {
                new CalendarView();
            }
        }
    }


    private class btnPtnMan_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            new PatientManager();
        }
    }

    public static void main(String[] args) {
        new ChoiceScreen();
        //new ChoiceScreen("Practitioner Choice");
    }
}
