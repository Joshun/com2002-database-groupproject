package com2002.team021.gui;

import com2002.team021.Appointment;
import com2002.team021.Patient;
import com2002.team021.Practitioner;
import com2002.team021.Query;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Created by joshua on 10/12/15.
 */
public class RescheduleAppointment extends JFrame {

    private TimeEntry startEntry;
    private TimeEntry endEntry;
    private JButton rescheduleButton;


    private class TimeEntry {
        private SpinnerNumberModel hourEntryModel;
        private SpinnerNumberModel minuteEntryModel;
        private JSpinner hourEntry;
        private JSpinner minuteEntry;
        private JPanel timeContainer;

        public TimeEntry() {
            hourEntryModel = new SpinnerNumberModel(9, 9, 16, 1);
            minuteEntryModel = new SpinnerNumberModel(0, 0, 59, 1);
            hourEntry = new JSpinner(hourEntryModel);
            minuteEntry = new JSpinner(minuteEntryModel);

            timeContainer = new JPanel(new GridLayout(2, 2));
            timeContainer.add(new JLabel("HH"));
            timeContainer.add(new JLabel("MM"));
            timeContainer.add(hourEntry);
            timeContainer.add(minuteEntry);
        }

        public JPanel getTimeContainer() {
            return timeContainer;
        }

        public SpinnerNumberModel getHourEntryModel() {
            return hourEntryModel;
        }

        public SpinnerNumberModel getMinuteEntryModel() {
            return minuteEntryModel;
        }



    }

    public RescheduleAppointment(Appointment appointmentToModify) {
        setTitle("Reschedule Appointment");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(5, 2));

        Practitioner practitioner = appointmentToModify.getPractitioner();
        Patient patient = appointmentToModify.getPatient();

        contentPane.add(new JLabel("Patient: "));
        contentPane.add(new JLabel(patient.getName()));
        contentPane.add(new JLabel("Practitioner: "));
        contentPane.add(new JLabel(practitioner.getName()));

        startEntry = new TimeEntry();
        endEntry = new TimeEntry();
        contentPane.add(new JLabel("Start time"));
        contentPane.add(startEntry.getTimeContainer());
        contentPane.add(new JLabel("End time"));
        contentPane.add(endEntry.getTimeContainer());

        contentPane.add(new JLabel());
        rescheduleButton = new JButton("Reschedule");
        contentPane.add(rescheduleButton);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {

        try {
            Query query = new Query();
            ArrayList<Appointment> appointments = query.getAppointments();
            Appointment anAppointment = appointments.get(0);
            new RescheduleAppointment(anAppointment);


        }
        catch (java.sql.SQLException e) {
            System.out.println("Error connecting to database");
        }
    }
}
