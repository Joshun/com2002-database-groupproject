package com2002.team021.gui;

import com2002.team021.Query;
import com2002.team021.Address;
import com2002.team021.Patient;
import com2002.team021.HealthcarePlan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.sql.SQLException;


/**
 * Created by joshua on 04/12/15.
 */
public class AddPatient extends JFrame {
    private JTextField forenameEntry;
    private JTextField surnameEntry;
    private DateWidget dOBEntry;
    private JTextField houseNoEntry;
    private JTextField postcodeEntry;
    private JTextField phoneEntry;
    private JComboBox<String> planEntry;
    private JButton addPatientButton;
    private Calendar calendar;
    private PatientManager patientManager;
    private Patient patientToModify;
    private ArrayList<String> healthcarePlans;
    private String noPlan = "<No plan>";


    private void errorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class AddPatientButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            String forename = forenameEntry.getText().trim();
            String surname = surnameEntry.getText().trim();

            Date dOBTimestamp = dOBEntry.getDate();
            String houseNo = houseNoEntry.getText().trim();
            String postcode = postcodeEntry.getText().trim();
            String phoneString = phoneEntry.getText().trim();
            String planString = (String) planEntry.getSelectedItem();

            if (forename.length() == 0
                    || surname.length() == 0
                    || houseNo.length() == 0
                    || postcode.length() == 0
                    || phoneString.length() == 0) {
                errorDialog("Fields cannot be empty.");
            } else {
                int phone = 0;
                Patient newPatient = null;
                try {
                    phone = Integer.parseInt(phoneString);
                    if (planString.equals(noPlan)) {
                        newPatient = new Patient(forename, surname, dOBTimestamp, phone, houseNo, postcode, null);
                    }
                    else {
                        newPatient = new Patient(forename, surname, dOBTimestamp, phone, houseNo, postcode, planString);
                    }
                    if (patientToModify == null) {
                        patientManager.addPatient(newPatient);
                    }
                    else {
                        patientManager.updatePatient(patientToModify, newPatient);
                    }
                    patientManager.setEnabled(true);
                    dispose();
                } catch (java.lang.NumberFormatException e) {
                    errorDialog("Invalid phone number.");
                }
                catch (java.sql.SQLException e) {
                    String error = "Coudln\'t create patient.\n" + e;
                    System.out.println(error);
                    errorDialog(error);
                }

                System.out.println("Created new patient " + newPatient);
            }

        }
    }

    public AddPatient(PatientManager patientManager) {
        patientToModify = null;
        this.patientManager = patientManager;
        healthcarePlans = new ArrayList<>();

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                patientManager.setEnabled(true);
            }
        });

        calendar = new GregorianCalendar();
        setTitle("Add patient");
        Container contentPane = getContentPane();
        /**
         * First name
         * Surname
         * DoB
         * Address (No., Postcode)
         * Phone
         * Plan
         */
        contentPane.setLayout(new GridLayout(8, 2));

        contentPane.add(new JLabel("Forename:"));
        forenameEntry = new JTextField(null, 10);
        contentPane.add(forenameEntry);

        contentPane.add(new JLabel("Surname:"));
        surnameEntry = new JTextField(null, 10);
        contentPane.add(surnameEntry);

        Calendar dOBInitial = Calendar.getInstance();
        dOBInitial.set(Calendar.DAY_OF_MONTH, 1);
        dOBInitial.set(Calendar.MONTH, 0);
        dOBEntry = new DateWidget(dOBInitial.getTime());

        contentPane.add(new JLabel("Date of Birth:"));
        contentPane.add(dOBEntry);

        contentPane.add(new JLabel("House No.:"));
        houseNoEntry = new JTextField(4);
        contentPane.add(houseNoEntry);
        contentPane.add(new JLabel("Postcode:"));
        postcodeEntry = new JTextField(8);
        contentPane.add(postcodeEntry);
        contentPane.add(new JLabel("Phone:"));
        phoneEntry = new JTextField(12);
        contentPane.add(phoneEntry);

        contentPane.add(new JLabel("Plan:"));


        ArrayList<HealthcarePlan> plans = null;

        try {
            plans = new Query().getHealthcarePlans();

        } catch (SQLException e) {
            System.out.println("Couldn't get plans list\n" + e);
            System.exit(1);
        }

        planEntry = new JComboBox<>();

        planEntry.addItem(noPlan);
        healthcarePlans.add(noPlan);

        for (HealthcarePlan p: plans) {
            planEntry.addItem(p.getName());
            healthcarePlans.add(p.getName());
        }

        contentPane.add(planEntry);

        contentPane.add(new JLabel());
        addPatientButton = new JButton("Add patient");
        addPatientButton.addActionListener(new AddPatientButtonHandler());
        contentPane.add(addPatientButton);

        pack();
        setVisible(true);

    }

    public AddPatient(PatientManager patientManager, Patient patientToModify) {
        this(patientManager);
        addPatientButton.setText("Modify patient");

        this.patientToModify = patientToModify;
        forenameEntry.setText(patientToModify.getForename());
        surnameEntry.setText(patientToModify.getSurname());

        Date dobDate = patientToModify.getDob();
        dOBEntry.setDate(dobDate);

        houseNoEntry.setText(patientToModify.getHouseNumber());
        postcodeEntry.setText(patientToModify.getPostcode());
        phoneEntry.setText(String.valueOf(patientToModify.getPhone()));

        HealthcarePlan plan = patientToModify.getSubscription();
        if (plan != null) {
            String planString = patientToModify.getSubscription().getName();
            planEntry.setSelectedIndex(healthcarePlans.indexOf(planString));
        }
        else {
            planEntry.setSelectedIndex(healthcarePlans.indexOf(noPlan));
        }

    }

    public static void main(String[] args) throws java.sql.SQLException {
        Patient[] patients = { new Patient("A", "b", new Date(), 1, "14", "st74hr", null), new Patient("", "c", new Date(), 1, "14", "st74hr", null) };
        Patient patient = new Patient("A", "b", new Date(), 1, "14", "st74hr", null);
        PatientManager pm = new PatientManager();
        AddPatient ap = new AddPatient(pm, patient);
    }

}
