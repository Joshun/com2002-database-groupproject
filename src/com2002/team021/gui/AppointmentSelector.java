package com2002.team021.gui;

import com2002.team021.Appointment;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AppointmentSelector.java
 * Assumes appointments are with the same Practitioner and Date and are sorted
 */
public class AppointmentSelector extends JFrame {
    public AppointmentSelector(Appointment[] appointments, Boolean userIsSecretary) {
//        setSize(500, 500);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        String appointmentDate = dateFormat.format(appointments[0].getStart());
        String practitioner = appointments[0].getPractitioner().toString();
        setTitle("Choose " + practitioner + " appointment (" + appointmentDate + ")");
        Container contentPane = getContentPane();
        if (userIsSecretary) {
            contentPane.setLayout(new GridLayout(3, 1));
        }
        else {
            contentPane.setLayout(new GridLayout(2, 1));
        }

        JLabel descriptionLabel = new JLabel("Listing " + practitioner + " appointments for " + appointmentDate + ":");
        JButton addAppointmentButton = new JButton("***Add appointment***");

        JPanel appointmentListPane = new JPanel(new GridLayout(appointments.length, 1));

        SimpleDateFormat startTimeFormat = new SimpleDateFormat("hh:mm");
        SimpleDateFormat endTimeFormat = new SimpleDateFormat("hh:mm");

        for (Appointment a: appointments) {
            String buttonText = startTimeFormat.format(a.getStart()) + " - " + endTimeFormat.format(a.getEnd()) + " : " + a.getPatient();
            JButton appointmentButton = new JButton(buttonText);
            appointmentListPane.add(appointmentButton);
        }
        contentPane.add(descriptionLabel);
        contentPane.add(appointmentListPane);
        if (userIsSecretary) {
            contentPane.add(addAppointmentButton);
        }

        pack();
//        setPreferredSize(new Dimension(1000, 100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

//    public static void main(String[] args) throws java.sql.SQLException {
//        Appointment[] appointments = { new Appointment(), new Appointment() };
//        AppointmentSelector appSel = new AppointmentSelector(appointments, true);
//        appSel.setVisible(true);
//    }
}
