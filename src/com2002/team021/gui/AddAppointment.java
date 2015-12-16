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
            Appointment newAppointment = new Appointment(startTimeStamp.getTime(), endTimeStamp.getTime(), patient, practitioner, emptyArrLis);

            if (dayView.addAppointment(newAppointment)) {
                setVisible(false);
                dayView.setEnabled(true);
            }

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
        this.startTimeStamp = new Date(this.day.getTime());
        this.endTimeStamp = new Date(this.day.getTime());

        setTitle("Book Appointment");
        setSize(300,400);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel patient = new JLabel("Patient:");
        contentPane.add(patient);
        patientJComboBox = new JComboBox<>();
        for (Patient p: allPatients) {
            patientJComboBox.addItem(p);
        }
        contentPane.add(patientJComboBox);
        patient.setBounds(45,50,50,20);
        patientJComboBox.setBounds(95,50,150,20);


        JLabel practitioner = new JLabel("Practitioner:");
        contentPane.add(practitioner);
        practitionerJComboBox = new JComboBox<>();
        for (Practitioner p: allPractitioners) {
            practitionerJComboBox.addItem(p);
        }
        contentPane.add(practitionerJComboBox);
        practitioner.setBounds(25,90,100,20);
        practitionerJComboBox.setBounds(95,90,150,20);

        JLabel type = new JLabel("Type:");
        contentPane.add(type);
        appointmentJComboBox = new JComboBox<>(appointmentTypes);
        appointmentJComboBox.addActionListener(new ComboChangeListener());
        contentPane.add(appointmentJComboBox);
        type.setBounds(55,130,50,20);
        appointmentJComboBox.setBounds(95,130,150,20);

        JPanel timeContainer = new JPanel(new GridLayout(2, 2));
        timeContainer.add(new JLabel("HH"));
        timeContainer.add(new JLabel("MM"));
        timeContainer.setBounds(95,170,150,40);

        startHourEntryModel = new SpinnerNumberModel(9, 9, 16, 1);
        startMinuteEntryModel = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner startHourEntry = new JSpinner(startHourEntryModel);
        JSpinner startMinuteEntry = new JSpinner(startMinuteEntryModel);

        SpinnerChangeListener scl = new SpinnerChangeListener();
        startHourEntry.addChangeListener(scl);
        startMinuteEntry.addChangeListener(scl);

        timeContainer.add(startHourEntry);
        timeContainer.add(startMinuteEntry);

        JLabel startTime = new JLabel("Start time:");
        contentPane.add(startTime);
        startTime.setBounds(35,190,100,20);
        contentPane.add(timeContainer);

        JLabel endTime = new JLabel("End time:");
        contentPane.add(endTime);
        endTime.setBounds(40,230,100,20);
        endTimeLabel = new JLabel("-");
        contentPane.add(endTimeLabel);
        endTimeLabel.setBounds(95,230,150,20);
        updateEndTimeLabel(endTimeStamp);

        contentPane.add(new JLabel());
        addAppointmentButton = new JButton("Add appointment");
        addAppointmentButton.addActionListener(new AddAppointmentButtonListener());
        contentPane.add(addAppointmentButton);
        addAppointmentButton.setBounds(95,270,150,20);

        timeChanged();


        //pack();
        setResizable(false);
        setVisible(true);
    }

    private class ComboChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            timeChanged();
            if ( ((String) appointmentJComboBox.getSelectedItem()).equals("Treatment") ) {
//                startMinuteEntryModel.setMaximum(0);
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
        startTimeCal.setTime(day);
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
        Patient[] patients = { new Patient("", "", "b", new Date(), 1, "14", "st74hr", null), new Patient("", "", "c", new Date(), 1, "14", "st74hr", null) };
        Practitioner[] practitioners = { new Practitioner("Mr a", "Dentist") };
//        AddAppointment ap = new AddAppointment(patients, practitioners);
    }
}
