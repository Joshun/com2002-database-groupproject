package com2002.team021.gui;

import com2002.team021.Appointment;
import com2002.team021.Patient;
import com2002.team021.Practitioner;
import com2002.team021.Query;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Created by joshua on 08/12/15.
 */
public class AppointmentDayView extends JFrame {
    Date day;
    private DefaultTableModel tableModel;
    private ArrayList<Appointment> appointments;
    private int selectedRow;
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");


    public String[] appointmentToRow(Appointment a) {
        Patient patient = a.getPatient();
        Practitioner practitioner = a.getPractitioner();
        Date startTime = a.getStartTime();
        Date endTime = a.getEndTime();
//        GregorianCalendar startCal = new GregorianCalendar();
//        GregorianCalendar endCal = new GregorianCalendar();
//        startCal.setTime(startTime);
//        endCal.setTime(endTime);

        String[] appString = { patient.getSurname(), patient.getForename(), practitioner.getRole(), practitioner.getName(), dateFormat.format(startTime), dateFormat.format(endTime), "type"};
        return appString;
    }

    public void reload() {
        for (Appointment a: appointments) {
            tableModel.addRow(appointmentToRow(a));
        }
    }

    public class AddAppHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Query query = new Query();
            try {
                AddAppointment ap = new AddAppointment(query.getPatients(), query.getPractitioners(), new Date());
            }
            catch (java.sql.SQLException ex) {
                System.out.println("Couldn't add appointment " + ex);
            }
        }
    }

    public AppointmentDayView(Date day) {
        this.day = day;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        JButton addAppButt = new JButton("Add");
        addAppButt.addActionListener(new AddAppHandler());
        JButton delAppButt = new JButton("Delete");
        JButton changeAppButt = new JButton("Change");
        contentPane.add(addAppButt);
        contentPane.add(delAppButt);
        contentPane.add(changeAppButt);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        String tableHeadings[] = { "PatientSurname", "PatientForename", "PractitionerRole", "PractitionerName", "Start", "End", "Type" };
        for (String heading: tableHeadings) {
            tableModel.addColumn(heading);
        }

        JTable table = new JTable(tableModel);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                Point rowPoint = event.getPoint();
                int row = table.rowAtPoint(rowPoint);
                if (row >= 0) {
                    selectedRow = row;
                    System.out.println("Selected: " + selectedRow);
                }
            }
        });

        JScrollPane tableContainer = new JScrollPane(table);
        contentPane.add(tableContainer);

//        reload();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void addAppointment(Appointment a) {
        appointments.add(a);
        tableModel.addRow(appointmentToRow(a));
    }

    public void removeAppointment(Appointment a) {
        tableModel.removeRow(appointments.indexOf(a));
        appointments.remove(a);
    }

    public void updateAppointment(Appointment oldPatient, Appointment newAppointment) {
        int indexOfOld = appointments.indexOf(oldPatient);
        appointments.set(indexOfOld, newAppointment);
        Patient p = newAppointment.getPatient();
        Practitioner practitioner = newAppointment.getPractitioner();
        tableModel.setValueAt(p.getSurname(), indexOfOld, 0);
        tableModel.setValueAt(p.getForename(), indexOfOld, 1);
        tableModel.setValueAt(practitioner.getRole(), indexOfOld, 2);
        tableModel.setValueAt(practitioner.getName(), indexOfOld, 3);
        tableModel.setValueAt(dateFormat.format(newAppointment.getStartTime()), indexOfOld, 4);
        tableModel.setValueAt(dateFormat.format(newAppointment.getEndTime()), indexOfOld, 5);
        tableModel.setValueAt("role", indexOfOld, 6);
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public static void main(String[] args) {
        AppointmentDayView adv = new AppointmentDayView(new Date());
    }
}
