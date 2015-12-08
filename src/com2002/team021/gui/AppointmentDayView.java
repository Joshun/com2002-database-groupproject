package com2002.team021.gui;

import com2002.team021.Appointment;
import com2002.team021.Patient;
import com2002.team021.Practitioner;

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
    private DefaultTableModel tableModel;
    private ArrayList<Appointment> appointments;
    private int selectedRow;

    public String[] appointmentToRow(Appointment a) {
        Patient patient = a.getPatient();
        Practitioner practitioner = a.getPractitioner();
        Date startTime = a.getStartTime();
        Date endTime = a.getEndTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
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

    public AppointmentDayView() {
        this.appointments = appointments;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        JButton addAppButt = new JButton("Add");
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
        JScrollPane tableContainer = new JScrollPane(table);
        contentPane.add(tableContainer);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        AppointmentDayView adv = new AppointmentDayView();
    }
}
