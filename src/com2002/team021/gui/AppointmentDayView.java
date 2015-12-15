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

    private ArrayList<Appointment> appointments;
    private Date day;
    private DefaultTableModel tableModel;
    private Practitioner practitioner;

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
            try {

                if (modify){
                    if (selectedRow >= 0) {
                        RescheduleAppointment ra = new RescheduleAppointment(appointments.get(selectedRow), dayView);
                        setEnabled(false);
                    }

                }
                else  {
                    AddAppointment ap = new AddAppointment(new Query().getPatients(), new Query().getPractitioners(), dayView);
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

    public class LogAppHandler implements ActionListener {
        private AppointmentDayView outerclass;
        public LogAppHandler(AppointmentDayView outerclass) {
            this.outerclass = outerclass;
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (selectedRow >= 0) {
                setEnabled(false);
                new LogTreatments(appointments.get(selectedRow), outerclass);
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

    public AppointmentDayView(Date day) {
        this(day, null);
    }

    public AppointmentDayView(Date day, Practitioner practitioner) {
        this.day = day;
        this.practitioner = practitioner;
        System.out.println(practitioner);

        if (day != null) {
            setTitle("Appointment listing for " + day.toString());
        }
        else {
            setTitle("Appointment listing");
        }
        setSize(1135,525);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JButton addAppButt = new JButton("Add Appointment");
        addAppButt.addActionListener(new AddAppHandler(this));
        JButton delAppButt = new JButton("Delete Appointment");
        delAppButt.addActionListener(new DelAppHandler());
        JButton changeAppButt = new JButton("Reschedule");
        changeAppButt.addActionListener(new AddAppHandler(this, true));
        JButton logAppButt = new JButton("Log Treament");
        logAppButt.addActionListener(new LogAppHandler(this));


        if (practitioner == null) {
            contentPane.add(addAppButt);
            contentPane.add(delAppButt);
            contentPane.add(changeAppButt);

            addAppButt.setBounds(10, 60, 225, 25);
            delAppButt.setBounds(10, 110, 225, 25);
            changeAppButt.setBounds(10, 160, 225, 25);
        }
        else {
            contentPane.add(logAppButt);

            logAppButt.setBounds(10, 60, 225, 25);
        }

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        String tableHeadings[] = { "Patient Surname", "Patient Forename", "Practitioner Role", "Practitioner Name", "Start Time", "End Time" };
        for (String heading: tableHeadings) {
            tableModel.addColumn(heading);
        }

        JTable table = new JTable(tableModel);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(20);



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
                for (int x = 0; x < table.getRowCount(); x++) {
                    for (int y = 0; y < table.getColumnCount(); y++){
                        if (value.equals(table.getValueAt(x, y))){
                            table.scrollRectToVisible(table.getCellRect(x, 0, true));  //make scroll go to location of x (row)
                            table.setRowSelectionInterval(x, x);  //focus on searched input
                            for (int i = 0; i <= table.getColumnCount() - 1; i++) {
                                table.getColumnModel().getColumn(i).setCellRenderer(new highlightRowRenderer());
                            }
                        }
                    }
                }
            }
        });
        contentPane.add(searchField);
        searchField.setBounds(965, 20, 150, 20);

        try {
            Query query = new Query();

            if (day != null) {
                System.out.println(day);
                System.out.println(day.getTime());
//                java.sql.Date sqlDate = new java.sql.Date(day.getTime());
//                this.appointments = query.getAppointmentsOnDay(sqlDate);
//                this.appointments = query.getAppointmentsOnDay(day);
                if (practitioner != null) {
                    this.appointments = query.getPractitionerAppointmentsOnDay(day, practitioner);
                }
                else {
                    this.appointments = query.getAppointmentsOnDay(day);
                }
                System.out.println(appointments);
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
        //pack();

        setResizable(false);
        setVisible(true);
    }

    public AppointmentDayView() {
        this(null);
    }

    public void addAppointment(Appointment a) {
        appointments.add(a);
        tableModel.addRow(appointmentToRow(a));
        try {
            Query query = new Query();
            query.addAppointment(a);
        }
        catch (java.sql.SQLException e) {
            System.out.println("Failed to add appointment " + e);
        }
    }

    public void removeAppointment(Appointment a) {
        tableModel.removeRow(appointments.indexOf(a));
        appointments.remove(a);
//        try {
//            Query query = new Query();
//        }
//        catch (java.sql.SQLException e) {
//            System.out.println("Failed to remove appointment " + e);
//        }
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
        try {
            Query query = new Query();
            query.updateAppointment(newAppointment, oldAppointment);
        }
        catch (java.sql.SQLException e) {
            System.out.println("Failed to update appointment " + e);
        }
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
