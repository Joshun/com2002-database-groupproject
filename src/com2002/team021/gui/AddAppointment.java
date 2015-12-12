package com2002.team021.gui;

import com2002.team021.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by joshua on 07/12/15.
 */


public class AddAppointment extends JFrame {
    private Date day;
    private AppointmentDayView dayView;
    private Date startTimeStamp;
    private Date endTimeStamp;
    private JComboBox<Patient> patientJComboBox;
    private JComboBox<Practitioner> practitionerJComboBox;
    private JComboBox<String> appointmentJComboBox;
    private SpinnerNumberModel startHourEntryModel;
    private SpinnerNumberModel startMinuteEntryModel;
    private JLabel endTimeLabel;
    private JButton addAppointmentButton;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    private String[] appointmentTypes = { "Checkup", "Hygienist", "Treatment" };
    private int[] appointmentDurations = { 20, 20, 60 };

    public class AddAppointmentButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Patient patient = (Patient) patientJComboBox.getSelectedItem();
            Practitioner practitioner = (Practitioner) practitionerJComboBox.getSelectedItem();
            String appointmentType = (String) appointmentJComboBox.getSelectedItem();
            ArrayList<Treatment> emptyArrLis = new ArrayList<>();
            System.out.println("Date: " + day);
            System.out.println("Start: " + startTimeStamp);
            System.out.println("End: " + endTimeStamp);
            Appointment newAppointment = new Appointment(startTimeStamp.getTime(), endTimeStamp.getTime(), patient, practitioner, emptyArrLis);

                dayView.addAppointment(newAppointment);
                try {
                    Query query = new Query();
                    query.addAppointment(newAppointment);
                }
                catch (java.sql.SQLException ex) {
                    System.out.println("Failed to update appointment " + ex);
                }


            setVisible(false);
            dayView.setEnabled(true);

            System.out.println(patient.toString() + practitioner.toString() + appointmentType);
        }
    }

    private class SpinnerChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            timeChanged();
        }
    }

    public AddAppointment(ArrayList<Patient> allPatients, ArrayList<Practitioner> allPractitioners, AppointmentDayView dayView) {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dayView.setEnabled(true);
            }
        });

        this.day = dayView.getDay();
        this.dayView = dayView;
        this.startTimeStamp = new Date();
        this.endTimeStamp = new Date();
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(6, 2));

        contentPane.add(new JLabel("Patient"));
        patientJComboBox = new JComboBox<>();
        for (Patient p: allPatients) {
            patientJComboBox.addItem(p);
        }

        contentPane.add(patientJComboBox);

        contentPane.add(new JLabel("Practitioner"));
        practitionerJComboBox = new JComboBox<>();
        for (Practitioner p: allPractitioners) {
            practitionerJComboBox.addItem(p);
        }
        contentPane.add(practitionerJComboBox);

        contentPane.add(new JLabel("Type"));
        appointmentJComboBox = new JComboBox<>(appointmentTypes);
        appointmentJComboBox.addActionListener(new ComboChangeListener());
        contentPane.add(appointmentJComboBox);

        JPanel timeContainer = new JPanel(new GridLayout(2, 2));
        timeContainer.add(new JLabel("HH"));
        timeContainer.add(new JLabel("MM"));
        startHourEntryModel = new SpinnerNumberModel(9, 9, 16, 1);
        startMinuteEntryModel = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner startHourEntry = new JSpinner(startHourEntryModel);
        JSpinner startMinuteEntry = new JSpinner(startMinuteEntryModel);

        SpinnerChangeListener scl = new SpinnerChangeListener();
        startHourEntry.addChangeListener(scl);
        startMinuteEntry.addChangeListener(scl);

        timeContainer.add(startHourEntry);
        timeContainer.add(startMinuteEntry);
        contentPane.add(new JLabel("Start time"));
        contentPane.add(timeContainer);

        contentPane.add(new JLabel("End time"));
        endTimeLabel = new JLabel("-");
        contentPane.add(endTimeLabel);
        updateEndTimeLabel(endTimeStamp);

        contentPane.add(new JLabel());
        addAppointmentButton = new JButton("Add appointment");
        addAppointmentButton.addActionListener(new AddAppointmentButtonListener());
        contentPane.add(addAppointmentButton);

        timeChanged();


        pack();
        setVisible(true);
    }

    private class ComboChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            timeChanged();
            System.out.println("changed");
            if ( ((String) appointmentJComboBox.getSelectedItem()).equals("Treatment") ) {
                System.out.println("treatment");
                startMinuteEntryModel.setMaximum(0);
                if ( startHourEntryModel.getNumber().intValue() == 16 && startHourEntryModel.getNumber().intValue() > 0) {
                    startMinuteEntryModel.setValue(0);
                }
            }
            else {
                startMinuteEntryModel.setMaximum(40);
            }
        }
    }

    private void timeChanged() {
        String appointmentType = (String) appointmentJComboBox.getSelectedItem();
        int hour = startHourEntryModel.getNumber().intValue();
        int minute = startMinuteEntryModel.getNumber().intValue();
        Calendar startTimeCal = Calendar.getInstance();
        startTimeCal.set(Calendar.HOUR_OF_DAY, hour);
        startTimeCal.set(Calendar.MINUTE, minute);
        startTimeStamp = startTimeCal.getTime();
        endTimeStamp = computeEndTime(startTimeStamp, appointmentType);
        updateEndTimeLabel(endTimeStamp);
    }

    private void updateEndTimeLabel(Date endTime) {
        endTimeLabel.setText(dateFormat.format(endTime));
    }

    private Date computeEndTime(Date startTime, String appointmentType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        switch (appointmentType) {
            case "Checkup":
            case "Hygienist":
                cal.add(Calendar.MINUTE, 19);
                break;
            case "Treatment":
                cal.add(Calendar.MINUTE, 59);
                break;
        }
        return cal.getTime();
    }

    public static void main(String[] args) throws java.sql.SQLException {
        Patient[] patients = { new Patient("", "b", new Date(), 1, "14", "st74hr", null), new Patient("", "c", new Date(), 1, "14", "st74hr", null) };
        Practitioner[] practitioners = { new Practitioner("Mr a", "Dentist") };
//        AddAppointment ap = new AddAppointment(patients, practitioners);
    }
}
