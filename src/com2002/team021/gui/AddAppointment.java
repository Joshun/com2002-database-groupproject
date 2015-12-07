package com2002.team021.gui;

import com2002.team021.Patient;
import com2002.team021.Practitioner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by joshua on 07/12/15.
 */


public class AddAppointment extends JFrame {
    private JComboBox<Patient> patientJComboBox;
    private JComboBox<Practitioner> practitionerJComboBox;
    private JComboBox<String> appointmentJComboBox;
    private String[] appointmentTypes = { "Checkup", "Hygienist", "Treatment" };

    public class AddAppointmentButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Patient patient = (Patient) patientJComboBox.getSelectedItem();
            Practitioner practitioner = (Practitioner) practitionerJComboBox.getSelectedItem();
            String appointmentType = (String) appointmentJComboBox.getSelectedItem();

            System.out.println(patient.toString() + practitioner.toString() + appointmentType);
        }
    }

    public AddAppointment(Patient[] allPatients, Practitioner[] allPractitioners) {
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(4, 2));

        contentPane.add(new JLabel("Patient"));
        patientJComboBox = new JComboBox<>(allPatients);
        contentPane.add(patientJComboBox);

        contentPane.add(new JLabel("Practitioner"));
        practitionerJComboBox = new JComboBox<>(allPractitioners);
        contentPane.add(practitionerJComboBox);

        contentPane.add(new JLabel("Type"));
        appointmentJComboBox = new JComboBox<>(appointmentTypes);
        contentPane.add(appointmentJComboBox);

        contentPane.add(new JLabel());
        JButton addAppointmentButton = new JButton("Add appointment");
        addAppointmentButton.addActionListener(new AddAppointmentButtonListener());
        contentPane.add(addAppointmentButton);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) throws java.sql.SQLException {
        Patient[] patients = { new Patient("", "b", 1, 1, "14", "st74hr", null), new Patient("", "c", 1, 1, "14", "st74hr", null) };
        Practitioner[] practitioners = { new Practitioner("Mr a", "Dentist") };
        AddAppointment ap = new AddAppointment(patients, practitioners);
    }
}
