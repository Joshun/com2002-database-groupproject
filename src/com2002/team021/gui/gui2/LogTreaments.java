package com2002.team021.gui.gui2;

import com2002.team021.Appointment;
import com2002.team021.Query;
import com2002.team021.Treatment;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by joshua on 14/12/15.
 */
public class LogTreaments extends JFrame {
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
            for (TreatmentCheckbox tc: treatmentCheckboxes) {
                Treatment associatedTreatment = tc.getAssociatedTreatment();
                if (tc.isSelected()) {
                    if (! appointmentTreatments.contains(associatedTreatment)) {
                        System.out.println("Adding treatment " + associatedTreatment);
                        appointmentTreatments.add(associatedTreatment);
                    }
                }
                else {
                    if (appointmentTreatments.contains(associatedTreatment)) {
                        System.out.println("Removing treatment " + associatedTreatment);
                        appointmentTreatments.remove(associatedTreatment);
                    }
                }
            }
        }
    }

    public LogTreaments(Appointment appointment) {
        treatmentCheckboxes = new ArrayList<>();
        setTitle("Log Treatment");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 1));
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.PAGE_AXIS));

        try {
            Query query = new Query();
            possibleTreatments = query.getTreatments();
//            appointmentTreatments = query.getTreatments();
            appointmentTreatments = query.getAppointmentTreatments(appointment);
        }
        catch (java.sql.SQLException e) {
            System.out.println("Could not load list of possible treatments: " + e);
        }

        if (appointmentTreatments == null) {
            appointmentTreatments = new ArrayList<>();
        }


         for (Treatment t: possibleTreatments) {
             TreatmentCheckbox checkbox;
            if (appointmentTreatments.contains(t)) {
                checkbox = new TreatmentCheckbox(t, true);
            }
            else {
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
        }
        catch (java.sql.SQLException e) {
            System.out.println("error");
        }
        new LogTreaments(appointments.get(0));
    }
}
