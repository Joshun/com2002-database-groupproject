package com2002.team021.gui;

import com2002.team021.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.SQLException;

/**
 * Created by joshua on 14/12/15.
 */
public class LogTreatments extends JFrame {
    private Appointment appointmentToModify;
    private AppointmentDayView dayView;
    private ArrayList<Treatment> possibleTreatments;
    private ArrayList<Treatment> appointmentTreatments;
    private ArrayList<TreatmentCheckbox> treatmentCheckboxes;

    private class TreatmentCheckbox extends JCheckBox {
        private Treatment associatedTreatment;

        public TreatmentCheckbox(Treatment associatedTreatment, boolean checked) {
            super(associatedTreatment.getName() + " " + "(£" + associatedTreatment.getCost() + ")", checked);
            this.associatedTreatment = associatedTreatment;
        }

        public Treatment getAssociatedTreatment() {
            return associatedTreatment;
        }
    }

    private class TreatmentButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            boolean hasChanged = false;
            for (TreatmentCheckbox tc : treatmentCheckboxes) {
                Treatment associatedTreatment = tc.getAssociatedTreatment();
                if (tc.isSelected()) {
                    if (!appointmentTreatments.contains(associatedTreatment)) {
                        System.out.println("Adding treatment " + associatedTreatment);
                        hasChanged = true;
                        appointmentTreatments.add(associatedTreatment);
                    }
                } else {
                    if (appointmentTreatments.contains(associatedTreatment)) {
                        System.out.println("Removing treatment " + associatedTreatment);
                        hasChanged = true;
                        appointmentTreatments.remove(associatedTreatment);
                    }
                }
            }
            if (hasChanged) {
                try {
                    // appointmentToModify.setTreatments(appointmentTreatments);
                    Date start = appointmentToModify.getStart();
                    Date end = appointmentToModify.getEnd();
                    Patient patient = appointmentToModify.getPatient();
                    Practitioner practitioner = appointmentToModify.getPractitioner();
                    Appointment newAppointment = new Appointment(start, end, patient, practitioner, appointmentTreatments);
                    System.out.println(newAppointment);
                    System.out.println(appointmentToModify);
                    new Query().updateSessionTreatments(newAppointment);
                } catch (java.sql.SQLException ex) {
                    System.out.println("Couldn\'t update treatments: " + ex);
                }
            }
            setVisible(false);
            dayView.setEnabled(true);
        }
    }

    public LogTreatments(Appointment appointment, AppointmentDayView dayView) {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dayView.setEnabled(true);
            }
        });

        this.appointmentToModify = appointment;
        this.dayView = dayView;
        treatmentCheckboxes = new ArrayList<>();
        setTitle("Log Treatment");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 1));
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.PAGE_AXIS));

        try {
            possibleTreatments = new Query().getTreatments();
            appointmentTreatments = new Query().getAppointmentTreatments(appointment);
            System.out.println(appointmentTreatments);
        } catch (SQLException e) {
            System.out.println("Could not load list of possible treatments: " + e);
        }

        if (appointmentTreatments == null) {
            appointmentTreatments = new ArrayList<>();
        }

        System.out.println(possibleTreatments);
        for (Treatment t : possibleTreatments) {
            TreatmentCheckbox checkbox;
            if (appointmentTreatments.contains(t)) {
                System.out.println("contained");
                checkbox = new TreatmentCheckbox(t, true);
            } else {
                checkbox = new TreatmentCheckbox(t, false);
            }
            checkboxPanel.add(checkbox);
            treatmentCheckboxes.add(checkbox);
        }

        contentPane.add(checkboxPanel);
        JButton logTreatmentButton = new JButton("Log Treatment");
        logTreatmentButton.addActionListener(new TreatmentButtonHandler());
        contentPane.add(logTreatmentButton);

        pack();
        setVisible(true);
    }

//    public static void main(String[] args) {
//        ArrayList<Appointment> appointments = null;
//        try {
//            appointments = new Query().getAppointments();
//        } catch (java.sql.SQLException e) {
//            System.out.println("error");
//        }
//        new LogTreatments(appointments.get(0));
//    }
}
