package com2002.team021.gui;

/**
 * Created by Afamezechukwu on 03/12/2015.
 */

import com2002.team021.Practitioner;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.util.*;

public class CalendarView extends JFrame {

    private Container contentPane;
    private DefaultTableModel tableModel;
    private int year, month, day, actualDay, actualYear, actualMonth;
    private JButton prev, next, listing;
    private JComboBox<String> comboBox;
    private JLabel label;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable table;
    private Point selectedCell;
    private Practitioner practitioner;

    public CalendarView() {
        this(null);
    }

    public CalendarView(Practitioner practitioner) {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new ChoiceScreen(practitioner);
                dispose();
            }
        });

        this.practitioner = practitioner;

        //Look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }

        //Frame
        String user = (practitioner != null)
                ? practitioner.getRole() + " " + practitioner.getName()
                : "Secretary";

        setTitle("Appointments Manager (" + user + ")");                           // Set title
        setSize(900, 500);                                //Set size

        //Pane
        contentPane = getContentPane();                 //Get content pane
        contentPane.setLayout(null);                              //Pane has a null layout


        //Buttons, Table, Table Model, Combo Box, Label
        label = new JLabel("January");
        comboBox = new JComboBox<>();
        prev = new JButton("<<");
        next = new JButton(">>");
        listing = new JButton("Appointments List");
        tableModel = new DefaultTableModel() {public boolean isCellEditable(int r, int c) {return false;}};
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        panel = new JPanel(null);


        //Register action listeners
        prev.addActionListener(new prev_Action());
        next.addActionListener(new next_Action());
        listing.addActionListener(new AppointmentListing());
        comboBox.addActionListener(new comboBox_Action());
        table.addMouseListener(new table_Action());

        //Add panel to pane; buttons to panel
        contentPane.add(panel);
        panel.add(label);
        panel.add(comboBox);
        panel.add(prev);
        panel.add(next);
        panel.add(listing);
        panel.add(scrollPane);

        //Set bounds
        panel.setBounds(0, 0, 880, 465);
        comboBox.setBounds(795, 430, 80, 20);
        listing.setBounds(131, 432, 243, 25);
        prev.setBounds(10, 25, 50, 25);
        next.setBounds(825, 25, 50, 25);
        scrollPane.setBounds(10, 50, 865, 380);

        //Make frame visible
        setResizable(false);
        setVisible(true);

        //Get real month/year
        GregorianCalendar cal = new GregorianCalendar();    //Gregorian Calendar
        day = cal.get(GregorianCalendar.DAY_OF_MONTH);      //Get current day
        month = cal.get(GregorianCalendar.MONTH);          //Get current month
        year = cal.get(GregorianCalendar.YEAR);            //Get current year
        actualDay = day;
        actualMonth = month;
        actualYear = year;

        //Add day headings
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            tableModel.addColumn(days[i]);
        }

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setColumnSelectionAllowed(true);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(59);

        tableModel.setColumnCount(7);
        tableModel.setRowCount(6);

        //Add +/- 100 years to current year in combo box
        for (int i = year - 100; i <= year + 100; i++) {
            comboBox.addItem(String.valueOf(i));
        }

        //Refresh calendar
        refresh(month, year);  //initially, Calendar is refreshed with current month, and year

    }


    public void refresh(int mnth, int yr) {

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int numberOfDays, startOfMonth;

        prev.setEnabled(true);
        next.setEnabled(true);

        if (mnth == 0 && yr <= year - 100) {
            prev.setEnabled(false);
        }
        if (mnth == 11 && yr >= year + 100) {
            next.setEnabled(false);
        }

        label.setText(months[mnth]);                                             //The label is the month
        label.setBounds(435 - label.getPreferredSize().width / 2, 25, 180, 25);  //Where month is written
        comboBox.setSelectedItem(String.valueOf(yr));                            //Year in the combo box

        //Clear table
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                tableModel.setValueAt(null, i, j);       //make all cells null
            }
        }

        GregorianCalendar cal = new GregorianCalendar(yr, mnth, 1);      //Get first day of month and number of days
        numberOfDays = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        startOfMonth = cal.get(GregorianCalendar.DAY_OF_WEEK);                  //Day which month starts; 1 - 7 for (Sun - Sat)

        //Draw calendar
        for (int i = 1; i <= numberOfDays; i++) {
            int row = (i + startOfMonth - 2) / 7;
            int column = (i + startOfMonth - 2) % 7;
            tableModel.setValueAt(i, row, column);
        }

        //Apply renderers
        table.setDefaultRenderer(table.getColumnClass(0), new tblCalendarRenderer());
    }

    private boolean isWeekend(int col) {
        return (col==0 || col==6);
    }


    private class tblCalendarRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (column == 0 || column == 6) { //Week-end
                setBackground(new Color(255, 220, 220));
            } else { //Week
                setBackground(new Color(255, 255, 255));

                if ( (table.isCellSelected(row, column)) && (value != null) ){
                    setBackground(new Color(220, 220, 255));
                }
            }
            if (value != null) {
                if (Integer.parseInt(value.toString()) == day && actualMonth == month && actualYear == year) { //Today
                    setBackground(new Color(220, 220, 55));
                }
            }

            setBorder(null);
            setForeground(Color.black);
            return this;
        }
    }

    private class prev_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (actualMonth == 0) { //Back one year
                actualMonth = 11;
                actualYear -= 1;
            } else { //Back one month
                actualMonth -= 1;
            }
            refresh(actualMonth, actualYear);
        }
    }

    private class next_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (actualMonth == 11) { //Foward one year
                actualMonth = 0;
                actualYear += 1;
            } else { //Foward one month
                actualMonth += 1;
            }
            refresh(actualMonth, actualYear);
        }
    }

    private class AppointmentListing implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (isWeekend(table.getSelectedColumn())) {
                return;
            }
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(Calendar.YEAR, actualYear);
            cal.set(Calendar.MONTH, actualMonth);
            cal.set(Calendar.DAY_OF_MONTH, actualDay);

            System.out.println("===PLEASE STAND BY===");
            System.out.println("Please stand by what?");
            System.out.println("A door?");
            System.out.println("A person?");
            System.out.println("A catfish?");
            System.out.println("I NEED ANSWERS PEOPLE?");
            if (practitioner != null) {
                new AppointmentDayView(cal.getTime(), practitioner);
            }
            else {
                new AppointmentDayView(cal.getTime());
            }

        }
    }

    private class comboBox_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (comboBox.getSelectedItem() != null) {
                String b = comboBox.getSelectedItem().toString();
                actualYear = Integer.parseInt(b);
                refresh(actualMonth, actualYear);
            }
        }
    }

    private class table_Action implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            //get the source of the event
            Object day = e.getSource();
            int x = table.getSelectedRow();
            int y = table.getSelectedColumn();

            tblCalendarRenderer a = new tblCalendarRenderer();
            Point point = new Point(x,y);
            selectedCell = point;

            Object value = tableModel.getValueAt(point.x, point.y);

            //if it was a click on JTable
            if (day instanceof JTable ) {
                if (value != null){
                    actualDay = (int) value;
                    refresh(actualMonth, actualYear);
                }
            }
        }
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
    }

    public static void main(String[] args){
        new CalendarView();
    }
}
