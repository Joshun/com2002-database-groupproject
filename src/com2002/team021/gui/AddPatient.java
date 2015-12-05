package com2002.team021.gui;

import com2002.team021.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.SQLException;


/**
 * Created by joshua on 04/12/15.
 */
public class AddPatient extends JFrame {
    private JTextField forenameEntry;
    private JTextField surnameEntry;
    private JSpinner dOBDayEntry;
    private SpinnerNumberModel dOBDayEntryModel;
    private JSpinner dOBMonthEntry;
    private SpinnerNumberModel dOBMonthEntryModel;
    private JSpinner dOBYearEntry;
    private SpinnerNumberModel dOBYearEntryModel;
    private JTextField houseNoEntry;
    private JTextField postcodeEntry;
    private JTextField phoneEntry;
    private JComboBox<String> planEntry;
    private JButton addPatientButton;

    private class AddPatientButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            String forename = forenameEntry.getText();
            String surname = surnameEntry.getText();
            int dOBDay = dOBDayEntryModel.getNumber().intValue();
            int dOBMonth = dOBMonthEntryModel.getNumber().intValue();
            int dOBYear = dOBYearEntryModel.getNumber().intValue();
            String houseNo = houseNoEntry.getText();
            String postcode = postcodeEntry.getText();
            String phone = phoneEntry.getText();
            String plan = (String) planEntry.getSelectedItem();

            Patient newPatient = null;
            
            // TODO: need to generate patient ID, need to have a String for phone, need to have an object for DoB
            try {
                newPatient = new Patient(forename, surname, 1, 1, houseNo, postcode, plan);
                
            } catch (SQLException e) {
                System.out.println("Coudlnt create patient.\n" + e);
            }
            
            System.out.println("Created new patient " + newPatient);
        }
    }

    public AddPatient() {
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

        dOBDayEntryModel = new SpinnerNumberModel(1, 1, 31, 1);
        dOBDayEntry = new JSpinner(dOBDayEntryModel);
        dOBContainer.add(dOBDayEntry);

        dOBMonthEntryModel = new SpinnerNumberModel(1, 1, 12, 1);
        dOBMonthEntry = new JSpinner(dOBMonthEntryModel);
        dOBContainer.add(dOBMonthEntry);

        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        dOBYearEntryModel = new SpinnerNumberModel(1900, 1900, year, 1);
        dOBYearEntry = new JSpinner(dOBYearEntryModel);
        dOBContainer.add(dOBYearEntry);

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
        String[] plans = { "NHSFreePlan", "Maintenance", "Oral Health", "Dental Repair"};
        planEntry = new JComboBox<>(plans);
        contentPane.add(planEntry);

        contentPane.add(new JLabel());
        addPatientButton = new JButton("Add patient");
        addPatientButton.addActionListener(new AddPatientButtonHandler());
        contentPane.add(addPatientButton);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {
        AddPatient ap = new AddPatient();
    }
}
