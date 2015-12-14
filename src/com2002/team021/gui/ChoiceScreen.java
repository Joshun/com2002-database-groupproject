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

    static JFrame choiceScreen;
    static JButton viewCal, logTreat, editCal, ptnMan;
    static Container contentPane;
    static String practitionerRole = "";
    static Practitioner practitioner;


    public ChoiceScreen(Practitioner practitioner) {
        this.practitioner = practitioner;
        //Look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }

        choiceScreen = new JFrame();
        choiceScreen.setSize(500,300);
        choiceScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = choiceScreen.getContentPane();
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
            practitioner = null;

        }
        else if (practitioner.getRole().equals("Dentist") || practitioner.getRole().equals("Hygienist")) {
            viewCal = new JButton("View Calendar");
            logTreat = new JButton("Log Treatment");

            viewCal.addActionListener(new btnEditCal_Action());
            //logTreat.addActionListener(new btnLogTreat_Action());

            contentPane.add(viewCal);

            viewCal.setBounds(100, 112, 300, 25);
            logTreat.setBounds(100, 163, 300, 25);
        }

        choiceScreen.setResizable(false);
        choiceScreen.setVisible(true);

    }

    public ChoiceScreen() {
        this(null);
    }

    static class btnEditCal_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            choiceScreen.dispose();
            if (practitioner != null) {
                System.out.println("Practitioner " + practitioner);
                new CalendarView(practitioner);
            }
            else {
                new CalendarView();
            }
        }
    }

    static class btnPtnMan_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            choiceScreen.dispose();
            new PatientManager();

        }
    }

    public static void main(String[] args) {
        new ChoiceScreen();
        //new ChoiceScreen("Practitioner Choice");
    }


}
