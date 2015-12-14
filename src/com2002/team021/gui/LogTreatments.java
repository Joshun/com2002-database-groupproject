package com2002.team021.gui;

import com2002.team021.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created by joshua on 14/12/15.
 */
public class LogTreatments extends JFrame {
    private Appointment appointmentToModify;
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
            for (TreatmentCheckbox tc : treatmentCheckboxes) {
                Treatment associatedTreatment = tc.getAssociatedTreatment();
                if (tc.isSelected()) {
                    if (!appointmentTreatments.contains(associatedTreatment)) {
                        System.out.println("Adding treatment " + associatedTreatment);
                        appointmentTreatments.add(associatedTreatment);
                    }
                } else {
                    if (appointmentTreatments.contains(associatedTreatment)) {
                        System.out.println("Removing treatment " + associatedTreatment);
                        appointmentTreatments.remove(associatedTreatment);
                    }
                }
            }
            try {
//                appointmentToModify.setTreatments(appointmentTreatments);
                Date start = appointmentToModify.getStart();
                Date end = appointmentToModify.getEnd();
                Patient patient = appointmentToModify.getPatient();
                Practitioner practitioner = appointmentToModify.getPractitioner();
                Appointment newAppointment = new Appointment(start, end, patient, practitioner, appointmentTreatments);
                Query query = new Query();
                System.out.println(newAppointment);
                System.out.println(appointmentToModify);
                query.updateAppointment(newAppointment, appointmentToModify);
            }
            catch (java.sql.SQLException ex) {
                System.out.println("Couldn\'t update treatments: " + ex);
//                ex.printStackTrace();
            }
        }
    }

    public LogTreaments(Appointment appointment) {
        this.appointmentToModify = appointment;
        treatmentCheckboxes = new ArrayList<>();
        setTitle("Log Treatment");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 1));
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.PAGE_AXIS));

        try {
            Query q1 = new Query();
            Query q2 = new Query();
            possibleTreatments = q1.getTreatments();
//            appointmentTreatments = query.getTreatments();
            appointmentTreatments = q2.getAppointmentTreatments(appointment);
            System.out.println(appointmentTreatments);
        } catch (java.sql.SQLException e) {
            System.out.println("Could not load list of possible treatments: " + e);
        }

        if (appointmentTreatments == null) {
            appointmentTreatments = new ArrayList<>();
        }


        for (Treatment t : possibleTreatments) {
            TreatmentCheckbox checkbox;
            if (appointmentTreatments.contains(t)) {
                System.out.println(t);
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        ArrayList<Appointment> appointments = null;
        try {
            Query query = new Query();
            appointments = query.getAppointments();
        } catch (java.sql.SQLException e) {
            System.out.println("error");
        }
        new LogTreaments(appointments.get(0));
    }
}
