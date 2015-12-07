package com2002.team021.gui;

import com2002.team021.Patient;
import com2002.team021.Practitioner;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 07/12/15.
 */
public class AddAppointment extends JFrame {
    private JComboBox<Patient> patientJComboBox;
    private JComboBox<Practitioner> practitionerJComboBox;
    private JComboBox<String> appointmentJComboBox;
    private String[] appointmentTypes = { "Checkup", "Hygienist", "Dentist" };

    public AddAppointment(Patient[] allPatients, Practitioner[] allPractitioners) {
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 2));

        contentPane.add(new JLabel("Patient"));
        patientJComboBox = new JComboBox<>(allPatients);
        contentPane.add(patientJComboBox);

        contentPane.add(new JLabel("Practitioner"));
        practitionerJComboBox = new JComboBox<>(allPractitioners);
        contentPane.add(practitionerJComboBox);

        contentPane.add(new JLabel("Type"));
        appointmentJComboBox = new JComboBox<>(appointmentTypes);
        setVisible(true);
    }

    public static void main(String[] args) throws java.sql.SQLException {
        Patient[] patients = { new Patient("a", "b", 1, 1, "1", "1", "1") };
    }
}
