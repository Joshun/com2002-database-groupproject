package com2002.team021.gui;

import com2002.team021.*;

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
    private ErrorHandler errorHandler;


    public String[] appointmentToRow(Appointment a) {
        Patient patient = a.getPatient();
        Practitioner practitioner = a.getPractitioner();
        Date startTime = a.getStart();
        Date endTime = a.getEnd();

        String[] appString = { patient.getSurname(), patient.getForename(), practitioner.getRole(), practitioner.getName(), dateFormat.format(startTime), dateFormat.format(endTime)};
        return appString;
    }

    public void reload() {
        for (Appointment a: appointments) {
            tableModel.addRow(appointmentToRow(a));
        }
    }

    private void noRowSelectedMessage() {
        errorHandler.showInfo("Please select a row and try again.");
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
                    else {
                        noRowSelectedMessage();
                    }

                }
                else  {
                    AddAppointment ap = new AddAppointment(new Query().getPatients(), new Query().getPractitioners(), dayView);
                    setEnabled(false);
                }
            }
            catch (java.sql.SQLException ex) {
                errorHandler.showDialog("Couldn't add appointment", ex);
            }
        }
    }

    public class DelAppHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (selectedRow >= 0) {
                removeAppointment(appointments.get(selectedRow));
                selectedRow = -1;
            }
            else {
                noRowSelectedMessage();
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
            else {
                noRowSelectedMessage();
            }
        }
    }

    private class PayAppHandler implements ActionListener {
        private AppointmentDayView outerclass;

        public PayAppHandler(AppointmentDayView outerclass) {
            this.outerclass = outerclass;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (selectedRow >= 0) {
                setEnabled(false);
                new PaySummary(appointments.get(selectedRow), outerclass);
            }
            else {
                noRowSelectedMessage();
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
        errorHandler = new ErrorHandler(this);
        this.day = day;
        this.practitioner = practitioner;

        if (day != null) {
            setTitle("Appointment listing for " + day.toString());
        }
        else {
            setTitle("Appointment listing");
        }
        setSize(1135,525);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JButton addAppButt = new JButton("Add");
        addAppButt.addActionListener(new AddAppHandler(this));
        JButton delAppButt = new JButton("Delete");
        delAppButt.addActionListener(new DelAppHandler());
        JButton changeAppButt = new JButton("Reschedule");
        changeAppButt.addActionListener(new AddAppHandler(this, true));
        JButton logAppButt = new JButton("Log Treatment");
        logAppButt.addActionListener(new LogAppHandler(this));
        JButton payAppButt = new JButton("Pay...");
        payAppButt.addActionListener(new PayAppHandler(this));


        if (practitioner == null) {
            contentPane.add(addAppButt);
            contentPane.add(delAppButt);
            contentPane.add(changeAppButt);
            contentPane.add(payAppButt);

            addAppButt.setBounds(10, 60, 225, 25);
            delAppButt.setBounds(10, 110, 225, 25);
            changeAppButt.setBounds(10, 160, 225, 25);
            payAppButt.setBounds(10, 210, 225, 25);

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

                if (practitioner != null) {
                    this.appointments = query.getPractitionerAppointmentsOnDay(day, practitioner);
                }
                else {
                    this.appointments = query.getAppointmentsOnDay(day);
                }
            }
            else {
                this.appointments = query.getAppointments();
            }
            reload();
        }
        catch (java.sql.SQLException e) {
            errorHandler.showDialog("Couldn\'t connect to db ", e);
        }

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //pack();

        setResizable(false);
        setVisible(true);
    }

    public AppointmentDayView() {
        this(null);
    }

    public boolean addAppointment(Appointment a) {

        try {
            Query query = new Query();
            query.addAppointment(a);
            appointments.add(a);
            tableModel.addRow(appointmentToRow(a));
        }
        catch (AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException e) {
            System.out.println("Appointment collides with another appointment \n" + e);
            errorHandler.showDialog("Appointment collides with another appointment", e);
            return false;
        }
        catch (java.sql.SQLException e) {
            errorHandler.showDialog("Failed to add appointment", e);
            return false;
        }
        return true;
    }

    public void removeAppointment(Appointment a) {
        try {
            new Query().deleteAppointment(a);
            tableModel.removeRow(appointments.indexOf(a));
            appointments.remove(a);
        }
        catch (java.sql.SQLException e) {
            errorHandler.showDialog("Failed to remove appointment", e);
        }
    }

    public boolean updateAppointment(Appointment oldAppointment, Appointment newAppointment) {

        try {
            new Query().updateAppointment(newAppointment, oldAppointment);

            int indexOfOld = appointments.indexOf(oldAppointment);
            appointments.set(indexOfOld, newAppointment);
            Patient p = newAppointment.getPatient();
            Practitioner practitioner = newAppointment.getPractitioner();

            Calendar oldAppCal = Calendar.getInstance();
            oldAppCal.setTime(oldAppointment.getStart());
            Calendar newAppCal = Calendar.getInstance();
            newAppCal.setTime(newAppointment.getStart());

            int d1 = oldAppCal.get(Calendar.DAY_OF_MONTH);
            int m1 = oldAppCal.get(Calendar.MONTH);
            int y1 = oldAppCal.get(Calendar.YEAR);

            int d2 = newAppCal.get(Calendar.DAY_OF_MONTH);
            int m2 = newAppCal.get(Calendar.MONTH);
            int y2 = newAppCal.get(Calendar.YEAR);

            if (d1 != d2
                    || m1 != m2
                    || y1 != y2) {
                tableModel.removeRow(indexOfOld);
            }
            else {
                tableModel.setValueAt(p.getSurname(), indexOfOld, 0);
                tableModel.setValueAt(p.getForename(), indexOfOld, 1);
                tableModel.setValueAt(practitioner.getRole(), indexOfOld, 2);
                tableModel.setValueAt(practitioner.getName(), indexOfOld, 3);
                tableModel.setValueAt(dateFormat.format(newAppointment.getStart()), indexOfOld, 4);
                tableModel.setValueAt(dateFormat.format(newAppointment.getEnd()), indexOfOld, 5);
            }
        }
        catch (AppointmentCollidesWithAnotherAppointmentInTheListOfAppointmentsFromTheDatabaseException e) {
            System.out.println("Appointment collides with another appointment \n" + e);
            errorHandler.showDialog("Appointment collides with another appointment", e);
            return false;
        }
        catch (java.sql.SQLException e) {
            errorHandler.showDialog("Failed to update appointment ", e);
            return false;
        }
        return true;
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
