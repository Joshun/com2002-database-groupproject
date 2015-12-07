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

    public class PatientManagerButtonHandler implements ActionListener {
        private PatientManager patientManager;
        public PatientManagerButtonHandler(PatientManager superclass) {
            this.patientManager = superclass;
        }
        public void actionPerformed(ActionEvent actionEvent) {
            AddPatient ap = new AddPatient(patientManager);
            ap.setVisible(true);
            setEnabled(false);
        }
    }


    public PatientManager() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        JButton addPatientButton = new JButton("Add patient");
        addPatientButton.addActionListener(new PatientManagerButtonHandler(this));
        contentPane.add(addPatientButton);

        DefaultTableModel patientTableModel = new DefaultTableModel();
        String[] headings = { "ID", "Forename", "Surname" };
        for (String s: headings) {
            patientTableModel.addColumn(s);
        }
        JTable patientTable = new JTable(patientTableModel);
        JScrollPane tableContainer = new JScrollPane(patientTable);
        contentPane.add(tableContainer);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        PatientManager pm = new PatientManager();
    }
}
