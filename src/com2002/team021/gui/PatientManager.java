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
    private ArrayList<Patient> searchList;
    private int selectedRow = -1;
    private ErrorHandler errorHandler;
    private JTextField searchField;

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
                    if (searchList == null) {
                        // Nothing is being searched so use the list of all the patients
                        ap = new AddPatient(patientManager, patientManager.patients.get(selectedRow));
                    }
                    else {
                        // Search has been carried out, get patient from this instead
                        ap = new AddPatient(patientManager, patientManager.searchList.get(selectedRow));
                    }
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

    private class ClearButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            searchField.setText("");
            clear();
            reload();
        }
    }

    private class AddAddressHandler implements ActionListener {
        private PatientManager outerclass;
        public AddAddressHandler(PatientManager outerclass) {
            this.outerclass = outerclass;
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            new AddAddress(outerclass);
            setEnabled(false);
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
                // If patient has plan display its name, otherwise just empty
                (plan != null) ? plan.getName() : "-"
        };
        return patientString;
    }

    public void reload() {
        for (Patient p: patients) {
            patientTableModel.addRow(patientToRow(p));
        }
    }

    public void clear() {
        if (patientTableModel.getRowCount() > 0) {
            for (int i = patientTableModel.getRowCount() - 1; i > -1; i--) {
                patientTableModel.removeRow(i);
            }
        }
    }

    public void loadSearchTerms(ArrayList<Patient> searchedPatients) {
        for (Patient p: searchedPatients) {
            patientTableModel.addRow(patientToRow(p));
        }
    }


    public PatientManager() {
        searchList = null;
        this.errorHandler = new ErrorHandler(this, true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new ChoiceScreen();
                dispose();
            }
        });
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
            errorHandler.showDialog("Failed to get list of patients", e);
        }

        Container contentPane = getContentPane();
        //contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setLayout(null);

        JButton addPatientButton = new JButton("Add Patient");
        JButton modifyPatientButton = new JButton("More Information / Modify");
        JButton addAddressButton = new JButton("Add Address");
        addPatientButton.addActionListener(new PatientManagerButtonHandler(this, false));
        modifyPatientButton.addActionListener(new PatientManagerButtonHandler(this, true));
        addAddressButton.addActionListener(new AddAddressHandler(this));
        contentPane.add(addPatientButton);
        contentPane.add(modifyPatientButton);
        contentPane.add(addAddressButton);
        addPatientButton.setBounds(10, 60, 225, 25);
        modifyPatientButton.setBounds(10, 110, 225, 25);
        addAddressButton.setBounds(10, 160, 225, 25);

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
                }
            }
        });


        JScrollPane tableContainer = new JScrollPane(patientTable);
        contentPane.add(tableContainer);
        tableContainer.setBounds(250, 60, 865, 420);

        JLabel label = new JLabel("Search: ");
        contentPane.add(label);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 12));
        label.setBounds(855, 19, 70, 20);

        searchField = new JTextField();
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = searchField.getText();
                if (value.length() > 0) {
                    try {
                        searchList = new Query().getPatientsByName(value);
                        clear();
                        loadSearchTerms(searchList);
                    } catch (java.sql.SQLException ex) {
                        errorHandler.showDialog("Couldn't search for patients ", ex);
                    }
                }
                else {
                    searchList = null;
                    clear();
                    reload();
                }
            }
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String value = searchField.getText();
//                for (int x = 0; x < patientTable.getRowCount(); x++) {
//                    for (int y = 0; y < patientTable.getColumnCount(); y++){
//                        if (value.equals(patientTable.getValueAt(x, y))){
//                            patientTable.scrollRectToVisible(patientTable.getCellRect(x, 0, true));  //make scroll go to location of x (row)
//                            patientTable.setRowSelectionInterval(x, x);  //focus on searched input
//                            for (int i = 0; i <= patientTable.getColumnCount() - 1; i++) {
//                                patientTable.getColumnModel().getColumn(i).setCellRenderer(new highlightRowRenderer());
//                            }
//                        }
//                    }
//                }
//            }
        });
        contentPane.add(searchField);
        searchField.setBounds(965, 20, 150, 20);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearButtonHandler());
        contentPane.add(clearButton);
        clearButton.setBounds(925, 20, 40, 20);
        clearButton.setFont(new Font(label.getFont().getName(), Font.PLAIN, 12));


        reload();

        //pack();
        setResizable(false);
        setVisible(true);
    }

    public ArrayList<Patient> getPatients() {
        return this.patients;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void addPatient(Patient p) {
        try {
            Query query = new Query();
            query.addPatient(p);
            patients.add(p);
            patientTableModel.addRow(patientToRow(p));
        }
        catch (java.sql.SQLException e) {
            errorHandler.showDialog("Failed to add patient", e);
        }
    }

//    public int getPatientIndex(Patient p) {
//        return patients.indexOf(p);
//    }

    public void updatePatient(Patient oldPatient, Patient newPatient) {

        try {
            Query query = new Query();
            query.updatePatient(newPatient);
            HealthcarePlan subscription = newPatient.getSubscription();
            int indexOfOld = patients.indexOf(oldPatient);
            patients.set(indexOfOld, newPatient);
            patientTableModel.setValueAt(newPatient.getId(), indexOfOld, 0);
            patientTableModel.setValueAt(newPatient.getForename(), indexOfOld, 1);
            patientTableModel.setValueAt(newPatient.getSurname(), indexOfOld, 2);
            patientTableModel.setValueAt(
                    (subscription != null) ? subscription.getName() : "-",
                    indexOfOld,
                    3
            );
        }
        catch (java.sql.SQLException e) {
            errorHandler.showDialog("Failed to update patient", e);
        }
    }

    public static void main(String[] args) throws java.sql.SQLException {
        PatientManager pm = new PatientManager();
    }
}
