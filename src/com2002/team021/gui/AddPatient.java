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
    private JComboBox<Integer> dOBDayEntry;
    private JComboBox<Integer> dOBMonthEntry;
    private JComboBox<Integer> dOBYearEntry;
    private JTextField houseNoEntry;
    private JTextField postcodeEntry;
    private JTextField phoneEntry;
    private JComboBox<String> planEntry;
    private JButton addPatientButton;
    private Calendar calendar;
    private PatientManager patientManager;
    private Patient patientToModify;

    private void errorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class AddPatientButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            String forename = forenameEntry.getText().trim();
            String surname = surnameEntry.getText().trim();
            int dOBDay = (Integer) dOBDayEntry.getSelectedItem();
            int dOBMonth = (Integer) dOBMonthEntry.getSelectedItem() - 1;
            int dOBYear = (Integer) dOBYearEntry.getSelectedItem();

            GregorianCalendar dOBCal = new GregorianCalendar();
            dOBCal.clear();
            dOBCal.set(Calendar.DAY_OF_MONTH, dOBDay);
            dOBCal.set(Calendar.MONTH, dOBMonth);
            dOBCal.set(Calendar.YEAR, dOBYear);
            Date dOBTimestamp = dOBCal.getTime();
            System.out.println(dOBCal);

            String houseNo = houseNoEntry.getText().trim();
            String postcode = postcodeEntry.getText().trim();
//            String phone = phoneEntry.getText();
            String phoneString = phoneEntry.getText().trim();

            String plan = (String) planEntry.getSelectedItem();

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
//                  newPatient = new Patient(forename, surname, dOBTimestamp.getTime(), phone, houseNo, postcode, plan);
                } catch (java.lang.NumberFormatException e) {
                    errorDialog("Invalid phone number.");
                    return;
                }
//                catch (SQLException e) {
//                    System.out.println("Coudln\'t create patient.\n" + e);
//                }

                System.out.println("Created new patient " + newPatient);
            }

        }
    }

    public AddPatient(PatientManager patientManager) {
        patientToModify = null;
        this.patientManager = patientManager;

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

        JPanel dOBContainer = new JPanel(new GridLayout(2, 3));
        dOBContainer.add(new JLabel("Day"));
        dOBContainer.add(new JLabel("Month"));
        dOBContainer.add(new JLabel("Year"));

        int currentYear = calendar.get(Calendar.YEAR);

        dOBDayEntry = new JComboBox<>();
        dOBMonthEntry = new JComboBox<>();
        dOBYearEntry = new JComboBox<>();

        for (int i = 0; i < 31; i++) {
            dOBDayEntry.addItem(new Integer(i + 1));
        }
        for (int i = 0; i < 12; i++) {
            dOBMonthEntry.addItem(new Integer(i + 1));
        }

        for (int i = 0; i < 120; i++) {
            dOBYearEntry.addItem(new Integer(currentYear - i));
        }

        dOBContainer.add(dOBDayEntry);
        dOBContainer.add(dOBMonthEntry);
        dOBContainer.add(dOBYearEntry);


        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
//        dOBYearEntry = new JSpinner(dOBYearEntryModel);

        contentPane.add(new JLabel("Date of Birth:"));
        contentPane.add(dOBContainer);

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

        ArrayList<HealthcarePlan> healthcarePlans = null;

        try {
            healthcarePlans = new Query().getHealthcarePlans();

        } catch (SQLException e) {
            System.out.println("Couldn't get treatment list\n" + e);
        }

        String[] planStrings = new String[healthcarePlans.size()];

        int i = 0;
        for (HealthcarePlan t : healthcarePlans) {
            planStrings[i] = t.getName();
            i++;
        }

        planEntry = new JComboBox<>(planStrings);
        contentPane.add(planEntry);

        contentPane.add(new JLabel());
        addPatientButton = new JButton("Add patient");
        addPatientButton.addActionListener(new AddPatientButtonHandler());
        contentPane.add(addPatientButton);

        pack();
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public AddPatient(PatientManager patientManager, Patient patientToModify) {
        this(patientManager);
        this.patientToModify = patientToModify;
        forenameEntry.setText(patientToModify.getForename());
        surnameEntry.setText(patientToModify.getSurname());

        int currentYear = calendar.get(Calendar.YEAR);

        long dob = patientToModify.getDob();
        Date dobDate = new Date(dob);
        GregorianCalendar dobCal = new GregorianCalendar();
        dobCal.setTime(dobDate);
        int dOBDay = dobCal.get(Calendar.DAY_OF_MONTH);
        int dOBMonth = dobCal.get(Calendar.MONTH);
        int dOBYear = dobCal.get(Calendar.YEAR);
        int yearIndex = currentYear - dOBYear;

        dOBDayEntry.setSelectedIndex(dOBDay - 1);
        dOBMonthEntry.setSelectedIndex(dOBMonth);
        dOBYearEntry.setSelectedIndex(yearIndex);

        houseNoEntry.setText(patientToModify.getHouseNumber());
        postcodeEntry.setText(patientToModify.getPostcode());
        phoneEntry.setText(String.valueOf(patientToModify.getPhone()));

        // TODO: need to figure out how to get this index
        planEntry.setSelectedIndex(0);

    }

    public static void main(String[] args) throws java.sql.SQLException {
        Patient[] patients = { new Patient("A", "b", 1, 1, "14", "st74hr", null), new Patient("", "c", 1, 1, "14", "st74hr", null) };
        Patient patient = new Patient("A", "b", 1, 1, "14", "st74hr", null);
        PatientManager pm = new PatientManager(patients);
        AddPatient ap = new AddPatient(pm, patient);
    }

}
