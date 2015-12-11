package com2002.team021.gui;

import com2002.team021.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.channels.CancelledKeyException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Created by joshua on 10/12/15.
 */
public class RescheduleAppointment extends JFrame {

    private AppointmentDayView dayView;
    private Appointment appointmentToModify;
    private TimeEntry startEntry;
    private TimeEntry endEntry;
    private JButton rescheduleButton;
    private Date startTimestamp;
    private Date endTimestamp;


    private class TimeEntry {
        private SpinnerNumberModel hourEntryModel;
        private SpinnerNumberModel minuteEntryModel;
        private JSpinner hourEntry;
        private JSpinner minuteEntry;
        private JPanel timeContainer;

//        private class SpinnerChangeListener implements ChangeListener {
//            @Override
//            public void stateChanged(ChangeEvent changeEvent) {
//                timeChanged();
//            }
//        }

        public TimeEntry() {
            hourEntryModel = new SpinnerNumberModel(9, 9, 16, 1);
            minuteEntryModel = new SpinnerNumberModel(0, 0, 59, 1);
            hourEntry = new JSpinner(hourEntryModel);
            minuteEntry = new JSpinner(minuteEntryModel);
//            hourEntry.addChangeListener(new SpinnerChangeListener());
//            minuteEntry.addChangeListener(new SpinnerChangeListener());

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

    private class RescheduleButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            computeTimestamps();
            Patient patient = appointmentToModify.getPatient();
            Practitioner practitioner = appointmentToModify.getPractitioner();

            ArrayList<Treatment> emptyTreatment = new ArrayList<>();

            Appointment newAppointment = new Appointment(startTimestamp, startTimestamp, endTimestamp, patient, practitioner, emptyTreatment);
            System.out.println("Old appointment: " + appointmentToModify);
            System.out.println("Updated to: " + newAppointment);
            dayView.updateAppointment(appointmentToModify, newAppointment);
            setVisible(false);
            dayView.setEnabled(true);
        }
    }


    private void computeTimestamps() {
        int startHour = startEntry.getHourEntryModel().getNumber().intValue();
        int startMinute = startEntry.getMinuteEntryModel().getNumber().intValue();
        int endHour = endEntry.getHourEntryModel().getNumber().intValue();
        int endMinute = endEntry.getMinuteEntryModel().getNumber().intValue();
        Calendar startTimeCal = Calendar.getInstance();
        startTimeCal.set(Calendar.HOUR_OF_DAY, startHour);
        startTimeCal.set(Calendar.MINUTE, startMinute);
        Calendar endTimeCal = Calendar.getInstance();
        endTimeCal.set(Calendar.HOUR_OF_DAY, endHour);
        endTimeCal.set(Calendar.MINUTE, endMinute);
        startTimestamp = startTimeCal.getTime();
        endTimestamp = endTimeCal.getTime();
    }

    public RescheduleAppointment(Appointment appointmentToModify, AppointmentDayView dayView) {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dayView.setEnabled(true);
            }
        });

        this.dayView = dayView;
        this.appointmentToModify = appointmentToModify;
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
        rescheduleButton.addActionListener(new RescheduleButtonHandler());
        contentPane.add(rescheduleButton);

        // Get current time values of appointment
        startTimestamp = appointmentToModify.getStartTime();
        endTimestamp = appointmentToModify.getEndTime();
        System.out.println("Start: " + startTimestamp + "End: " + endTimestamp);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startTimestamp);
        int startHour = startCal.get(Calendar.HOUR_OF_DAY);
        int startMinute = startCal.get(Calendar.MINUTE);
        startEntry.getHourEntryModel().setValue(startHour);
        startEntry.getMinuteEntryModel().setValue(startMinute);
        System.out.println("end " + endTimestamp);

        Calendar endCal = Calendar.getInstance();
        System.out.println(endTimestamp);
        endCal.setTime(endTimestamp);
        System.out.println(endCal);
        int endHour = endCal.get(Calendar.HOUR_OF_DAY);
        int endMinute = endCal.get(Calendar.MINUTE);
        System.out.println(endHour + ":" + endMinute);
        endEntry.getHourEntryModel().setValue(endHour);
        endEntry.getMinuteEntryModel().setValue(endMinute);

        pack();
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {

        try {
            Query query = new Query();
            ArrayList<Appointment> appointments = query.getAppointments();
            Appointment anAppointment = appointments.get(0);
            new RescheduleAppointment(anAppointment, new AppointmentDayView(new Date()));


        }
        catch (java.sql.SQLException e) {
            System.out.println("Error connecting to database");
        }
    }
}
