package com2002.team021.gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Calendar.java
 */
public class Calendar extends JFrame {
    private enum Practitioner  { DENTIST, HYGIENIST }
    private final String[] WEEK_DAYS = { "Mon", "Tue", "Wed", "Thu", "Fri" };

    private boolean noModify = false;
    private JButton[] dentistButtons = new JButton[WEEK_DAYS.length];
    private JButton[] hygienistButtons = new JButton[WEEK_DAYS.length];


    private String makeAppointmentButtonLabel(Practitioner p) {
        int numAppointments = 0;
        String label;
        switch (p) {
            case DENTIST:
                label = "Dentist " + "(" + numAppointments + ")";
                break;
            case HYGIENIST:
                label = "Hygienist " + "(" + numAppointments + ")";
                break;
            default:
                label = "Undefined";
        }
        return label;
    }

    public Calendar() {
        setTitle("Choose day");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(1, WEEK_DAYS.length));
        for (int i=0; i<WEEK_DAYS.length; i++) {
            JPanel dayContainer = new JPanel(new GridLayout(2, 1));
            JPanel buttonContainer = new JPanel(new GridLayout(1, 2));

            JLabel label = new JLabel(WEEK_DAYS[i]);
            JButton dentistAppointments = new JButton(makeAppointmentButtonLabel(Practitioner.DENTIST));
            JButton hygienistAppointments = new JButton(makeAppointmentButtonLabel(Practitioner.HYGIENIST));
            dentistButtons[i] = dentistAppointments;
            hygienistButtons[i] = hygienistAppointments;

            buttonContainer.add(dentistAppointments);
            buttonContainer.add(hygienistAppointments);
            dayContainer.add(label);
            dayContainer.add(buttonContainer);
            contentPane.add(dayContainer);
        }
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calendar (Practitioner p) {
        this();
        noModify = true;
        switch (p) {
            case HYGIENIST:
                for (JButton b: dentistButtons) {
                    b.setEnabled(false);
                }
                break;
            case DENTIST:
                for (JButton b: hygienistButtons) {
                    b.setEnabled(false);
                }
                break;
        }
    }

    public static void main(String[] args) {
        Calendar cal = new Calendar(Practitioner.DENTIST);
        cal.setVisible(true);
    }
}
