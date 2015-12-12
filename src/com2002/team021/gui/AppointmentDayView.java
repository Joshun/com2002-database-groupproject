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
    // Stores the currently selected row, set to -1 when none are selected
    private int selectedRow = -1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");


    public String[] appointmentToRow(Appointment a) {
        Patient patient = a.getPatient();
        Practitioner practitioner = a.getPractitioner();
        Date startTime = a.getStart();
        Date endTime = a.getEnd();
//        GregorianCalendar startCal = new GregorianCalendar();
//        GregorianCalendar endCal = new GregorianCalendar();
//        startCal.setTime(startTime);
//        endCal.setTime(endTime);
        System.out.println(practitioner);
        System.out.println(patient);

        String[] appString = { patient.getSurname(), patient.getForename(), practitioner.getRole(), practitioner.getName(), dateFormat.format(startTime), dateFormat.format(endTime)};
        return appString;
    }

    public void reload() {
        for (Appointment a: appointments) {
            tableModel.addRow(appointmentToRow(a));
        }
    }

    public class AddAppHandler implements ActionListener {
        private AppointmentDayView dayView;
        private boolean modify;

        public AddAppHandler(AppointmentDayView dv, Boolean modify) {
            this.dayView = dv;
            this.modify = modify;
        }
        public AddAppHandler(AppointmentDayView dv) {
            this(dv,false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Query query = new Query();
            try {

                if (modify){
                    if (selectedRow >= 0) {
                        RescheduleAppointment ra = new RescheduleAppointment(appointments.get(selectedRow), dayView);
                        setEnabled(false);
                    }

                }
                else  {
                    AddAppointment ap = new AddAppointment(query.getPatients(), query.getPractitioners(), dayView);
                    setEnabled(false);
                }
            }
            catch (java.sql.SQLException ex) {
                System.out.println("Couldn't add appointment " + ex);
            }
        }
    }

    public class DelAppHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (selectedRow >= 0) {
                appointments.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                selectedRow = -1;
            }
        }
    }

    public AppointmentDayView(Date day) {
        this.day = day;

        if (day != null) {
            setTitle("Appointment listing for " + day.toString());
        }
        else {
            setTitle("Appointment listing");
        }

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        JButton addAppButt = new JButton("Add");
        addAppButt.addActionListener(new AddAppHandler(this));
        JButton delAppButt = new JButton("Delete");
        delAppButt.addActionListener(new DelAppHandler());
        JButton changeAppButt = new JButton("Reschedule");
        changeAppButt.addActionListener(new AddAppHandler(this, true));
        contentPane.add(addAppButt);
        contentPane.add(delAppButt);
        contentPane.add(changeAppButt);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        String tableHeadings[] = { "PatientSurname", "PatientForename", "PractitionerRole", "PractitionerName", "Start", "End" };
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

        try {
            Query query = new Query();

            if (day != null) {
                java.sql.Date sqlDate = new java.sql.Date(day.getTime());
                this.appointments = query.getAppointmentsOnDay(sqlDate);
            }
            else {
                this.appointments = query.getAppointments();
            }
            System.out.println(appointments.size());
            reload();
        }
        catch (java.sql.SQLException e) {
            System.out.println("Couldn\'t connect to db " + e);
        }

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public AppointmentDayView() {
        this(null);
    }

    public void addAppointment(Appointment a) {
        appointments.add(a);
        tableModel.addRow(appointmentToRow(a));
    }

    public void removeAppointment(Appointment a) {
        tableModel.removeRow(appointments.indexOf(a));
        appointments.remove(a);
    }

    public void updateAppointment(Appointment oldAppointment, Appointment newAppointment) {
        int indexOfOld = appointments.indexOf(oldAppointment);
        appointments.set(indexOfOld, newAppointment);
        Patient p = newAppointment.getPatient();
        Practitioner practitioner = newAppointment.getPractitioner();
        tableModel.setValueAt(p.getSurname(), indexOfOld, 0);
        tableModel.setValueAt(p.getForename(), indexOfOld, 1);
        tableModel.setValueAt(practitioner.getRole(), indexOfOld, 2);
        tableModel.setValueAt(practitioner.getName(), indexOfOld, 3);
        tableModel.setValueAt(dateFormat.format(newAppointment.getStart()), indexOfOld, 4);
        tableModel.setValueAt(dateFormat.format(newAppointment.getEnd()), indexOfOld, 5);
//        tableModel.setValueAt("role", indexOfOld, 6);
    }

    public Date getDay() {
        return day;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public static void main(String[] args) {
        AppointmentDayView adv = new AppointmentDayView(null);
    }
}
