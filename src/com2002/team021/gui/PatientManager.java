package com2002.team021.gui;

import com2002.team021.Patient;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by joshua on 07/12/15.
 */
public class PatientManager extends JFrame {


    private DefaultTableModel patientTableModel;
    private Patient[] patients;
    private int selectedRow, selectedCol;

    private class PatientManagerButtonHandler implements ActionListener {
        private boolean modifyCurrent;
        private PatientManager patientManager;
        public PatientManagerButtonHandler(PatientManager superclass, Boolean modifyCurrent) {
            this.patientManager = superclass;
            this.modifyCurrent = modifyCurrent;
        }

        public void actionPerformed(ActionEvent actionEvent) {

            AddPatient ap;
            if (modifyCurrent) {
                System.out.println("Trying to modify existing patient at index, " + patientManager.selectedRow);
                ap = new AddPatient(patientManager, patientManager.patients[patientManager.selectedRow]);
            }
            else {
                ap = new AddPatient(patientManager);

            }
            ap.setVisible(true);
            setEnabled(false);
        }
    }

    public String[] patientToRow(Patient p) {
        String patientString[] = { String.valueOf(p.getId()), p.getForename(), p.getSurname() };
        return patientString;
    }

    public void reload() {
        for (Patient p: patients) {
            patientTableModel.addRow(patientToRow(p));
        }
    }


    public PatientManager(Patient[] patients) {
        this.patients = patients;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        JButton addPatientButton = new JButton("Add patient");
        JButton modifyPatientButton = new JButton("More Information / Modify patient");
        addPatientButton.addActionListener(new PatientManagerButtonHandler(this, false));
        modifyPatientButton.addActionListener(new PatientManagerButtonHandler(this, true));
        contentPane.add(addPatientButton);
        contentPane.add(modifyPatientButton);

        patientTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        String[] headings = { "ID", "Forename", "Surname" };
        for (String s: headings) {
            patientTableModel.addColumn(s);
        }
        JTable patientTable = new JTable(patientTableModel);
        patientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                Point rowPoint = event.getPoint();
                int row = patientTable.rowAtPoint(rowPoint);
                if (row >= 0) {
                    selectedRow = row;
                    System.out.println("Selected: " + selectedRow);
                }
            }
        });


        JScrollPane tableContainer = new JScrollPane(patientTable);
        contentPane.add(tableContainer);

        reload();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) throws java.sql.SQLException {
        Patient[] patients = { new Patient("", "b", 1, 1, "14", "st74hr", null), new Patient("", "c", 1, 1, "14", "st74hr", null) };
        PatientManager pm = new PatientManager(patients);
    }
}
