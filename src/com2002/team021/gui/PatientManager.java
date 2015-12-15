package com2002.team021.gui;

import com2002.team021.HealthcarePlan;
import com2002.team021.Patient;
import com2002.team021.Query;

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
    private ArrayList<Patient> patients;
    private int selectedRow = -1;

    private class PatientManagerButtonHandler implements ActionListener {
        private boolean modifyCurrent;
        private PatientManager patientManager;
        public PatientManagerButtonHandler(PatientManager superclass, Boolean modifyCurrent) {
            this.patientManager = superclass;
            this.modifyCurrent = modifyCurrent;
        }

        public void actionPerformed(ActionEvent actionEvent) {

            AddPatient ap = null;
            if (modifyCurrent) {
                if (selectedRow >= 0) {
                    int index = patientManager.getSelectedRow();
                    System.out.println("Trying to modify existing patient at index, " + selectedRow);
                    ap = new AddPatient(patientManager, patientManager.patients.get(selectedRow));
                }
            }
            else {
                ap = new AddPatient(patientManager);



            }
            if (ap != null) {
                ap.setVisible(true);
                setEnabled(false);
            }
        }
    }

    private class highlightRowRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if(row == table.getSelectedRow()) {

                // highlight row with a border colour
                setBorder(BorderFactory.createMatteBorder(2, 1, 2, 1, Color.BLACK));
            }
            return this;
        }
    }

    public String[] patientToRow(Patient p) {
        HealthcarePlan plan = p.getSubscription();
        String patientString[] = {
                String.valueOf(p.getId()),
                p.getForename(),
                p.getSurname(),
                (plan != null) ? plan.getName() : "-"
        };
        return patientString;
    }

    public void reload() {
        for (Patient p: patients) {
            patientTableModel.addRow(patientToRow(p));
        }
    }


    public PatientManager() {
        setTitle("Patient Manager");
        setSize(1135,525);

        patients = null;

//        patients.add(new Patient("", "b", new Date(), 1, "14", "st74hr", null));
//        patients.add(new Patient("", "c", new Date(), 1, "14", "st74hr", null));

        try {
            Query query = new Query();
            patients = query.getPatients();
        }
        catch (java.sql.SQLException e) {
            System.out.println("Failed to get list of patients " + e);
            System.exit(1);
        }

        Container contentPane = getContentPane();
        //contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setLayout(null);

        JButton addPatientButton = new JButton("Add patient");
        JButton modifyPatientButton = new JButton("More Information / Modify patient");
        addPatientButton.addActionListener(new PatientManagerButtonHandler(this, false));
        modifyPatientButton.addActionListener(new PatientManagerButtonHandler(this, true));
        contentPane.add(addPatientButton);
        contentPane.add(modifyPatientButton);
        addPatientButton.setBounds(10, 60, 225, 25);
        modifyPatientButton.setBounds(10, 110, 225, 25);

        patientTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        String[] headings = { "ID", "Forename", "Surname", "Plan" };
        for (String s: headings) {
            patientTableModel.addColumn(s);
        }
        JTable patientTable = new JTable(patientTableModel);
        patientTable.getTableHeader().setResizingAllowed(false);
        patientTable.getTableHeader().setReorderingAllowed(false);
        patientTable.setRowHeight(20);
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
        tableContainer.setBounds(250, 60, 865, 420);

        JLabel label = new JLabel("Search: ");
        contentPane.add(label);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 12));
        label.setBounds(900, 19, 70, 20);

        JTextField searchField = new JTextField();
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = searchField.getText();
                for (int x = 0; x < patientTable.getRowCount(); x++) {
                    for (int y = 0; y < patientTable.getColumnCount(); y++){
                        if (value.equals(patientTable.getValueAt(x, y))){
                            patientTable.scrollRectToVisible(patientTable.getCellRect(x, 0, true));  //make scroll go to location of x (row)
                            patientTable.setRowSelectionInterval(x, x);  //focus on searched input
                            for (int i = 0; i <= patientTable.getColumnCount() - 1; i++) {
                                patientTable.getColumnModel().getColumn(i).setCellRenderer(new highlightRowRenderer());
                            }
                        }
                    }
                }
            }
        });
        contentPane.add(searchField);
        searchField.setBounds(965, 20, 150, 20);

        reload();

        //pack();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public ArrayList<Patient> getPatients() {
        return this.patients;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void addPatient(Patient p) {
        patients.add(p);
        patientTableModel.addRow(patientToRow(p));
        try {
            Query query = new Query();
            query.addPatient(p);
        }
        catch (java.sql.SQLException e) {
            System.out.println("Failed to add patient " + e);
        }
    }

//    public int getPatientIndex(Patient p) {
//        return patients.indexOf(p);
//    }

    public void updatePatient(Patient oldPatient, Patient newPatient) {
        HealthcarePlan subscription = newPatient.getSubscription();
        int indexOfOld = patients.indexOf(oldPatient);
        System.out.println(indexOfOld);
        patients.set(indexOfOld, newPatient);
        patientTableModel.setValueAt(newPatient.getId(), indexOfOld, 0);
        patientTableModel.setValueAt(newPatient.getForename(), indexOfOld, 1);
        patientTableModel.setValueAt(newPatient.getSurname(), indexOfOld, 2);
        patientTableModel.setValueAt(
                (subscription != null) ? subscription.getName() : "-",
                indexOfOld,
                3
        );

        try {
            Query query = new Query();
            query.updatePatient(newPatient);
        }
        catch (java.sql.SQLException e) {
            System.out.println("Failed to update patient " + e);
        }
    }

    public static void main(String[] args) throws java.sql.SQLException {
        PatientManager pm = new PatientManager();
    }
}
