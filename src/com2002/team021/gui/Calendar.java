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
        for (String day: WEEK_DAYS) {
            JPanel dayContainer = new JPanel(new GridLayout(2, 1));
            JPanel buttonContainer = new JPanel(new GridLayout(1, 2));

            JLabel label = new JLabel(day);
            JButton dentistAppointments = new JButton(makeAppointmentButtonLabel(Practitioner.DENTIST));
            JButton hygienistAppointments = new JButton(makeAppointmentButtonLabel(Practitioner.HYGIENIST));

            buttonContainer.add(dentistAppointments);
            buttonContainer.add(hygienistAppointments);
            dayContainer.add(label);
            dayContainer.add(buttonContainer);
            contentPane.add(dayContainer);
        }
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Calendar cal = new Calendar();
        cal.setVisible(true);
    }
}
