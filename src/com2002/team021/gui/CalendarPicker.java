package com2002.team021.gui;

import com2002.team021.Practitioner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;


import javax.swing.*;
import java.awt.*;

/**
 * Calendar.java
 */
public class CalendarPicker extends JFrame {
    private final int MILLIS_PER_DAY = 86400 * 1000;
//    private enum PractitionerType { DENTIST, HYGIENIST }
    private final String[] WEEK_DAYS = { "Mon", "Tue", "Wed", "Thu", "Fri" };

    private String user;

    private boolean userCanModifyAppointments = false;
    private AppointmentButton[] dentistButtons = new AppointmentButton[WEEK_DAYS.length];
    private AppointmentButton[] hygienistButtons = new AppointmentButton[WEEK_DAYS.length];
    private Date weekBeginning;
    private JLabel dateLabel = new JLabel("");

    private enum NavigationButtonType {
        PREV, TODAY, NEXT;
        public String toString() {
            switch (name()) {
                case "PREV":
                    return "<";
                case "TODAY":
                    return "today";
                case "NEXT":
                    return ">";
                default:
                    return "-";
            }
        }

    }

//    private Appointment[] getAppointments(Date d, Practitioner p)  {
//        Appointment[] appointments = new Appointment[3];
//        for (int i=0; i<appointments.length; i++) {
//            appointments[i] = new Appointment();
//        }
//        // MYSQL query to go here
//        return appointments;
//    }

    private int getAppointmentTotal(Date d, Practitioner p) {
        // MYSQL query to go here
        return 5;
    }

    private class NavigationButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            NavigationButton navButton = (NavigationButton) e.getSource();
            switch (navButton.getNavType()) {
                case PREV:
                    weekBeginning = prevWeek(weekBeginning);
                    break;
                case TODAY:
                    weekBeginning = getMonday(new Date());
                    break;
                case NEXT:
                    weekBeginning = nextWeek(weekBeginning);
                    break;
            }
            System.out.println("Changed date to " + weekBeginning);
            setDateLabel(weekBeginning);
        }
    }

    private class NavigationButton extends JButton {
        private NavigationButtonType navType;
        public NavigationButton(NavigationButtonType navType) {
            super(navType.toString());
            this.navType = navType;
        }

        public NavigationButtonType getNavType() {
            return navType;
        }
    }


    private String makeAppointmentButtonLabel(Practitioner p, int numAppointments) {
//        int numAppointments = 0;
        String label;
        switch (p.getRole()) {
            case "Dentist":
                label = "Dentist " + "(" + numAppointments + ")";
                break;
            case "Hygienist":
                label = "Hygienist " + "(" + numAppointments + ")";
                break;
            default:
                label = "Undefined";
        }
        return label;
    }

    private Date nextWeek(Date current) {

        long theFuture = current.getTime() + (MILLIS_PER_DAY * 7);
        return new Date(theFuture);
    }

    private Date prevWeek(Date current) {
        long thePast = current.getTime() - (MILLIS_PER_DAY * 7);
        return new Date(thePast);
    }

    private void setDateLabel(Date date) {
        dateLabel.setText("W/B: " + date);
    }

    private Date getMonday(Date current) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);

        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if ( weekDay != Calendar.MONDAY ) {
            long currentTimestamp = current.getTime();
            long mondayTimestamp;
            if ( weekDay == Calendar.SUNDAY) {
                // SUNDAY has index 1 but we want to go back 6 days to get to the last Monday
                mondayTimestamp = currentTimestamp - (MILLIS_PER_DAY * 6);
            }
            else {
                // weekDay-2 is used because SUNDAY has index 1, MONDAY has index 2 etc. and we juts want the difference
                mondayTimestamp = currentTimestamp - (MILLIS_PER_DAY * (weekDay - 2));
            }
            return new Date(mondayTimestamp);
        }
        else {
            return current;
        }
    }

    private class AppointmentButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AppointmentButton button = (AppointmentButton) e.getSource();
            Practitioner practitioner = button.getPractitioner();
            int day = button.getDayNum();
            System.out.println("Day: " + day + " Practitioner: " + practitioner.getRole());
            System.out.println("Date: " + computeButtonDate(day));
        }
    }

    private Date computeButtonDate(int dayNum) {
        if (dayNum > 0) {
            long futureDateStamp = weekBeginning.getTime() + (dayNum * MILLIS_PER_DAY);
            return new Date(futureDateStamp);
        }
        else {
            return weekBeginning;
        }
    }

    private class AppointmentButton extends JButton {
        private Practitioner practitioner;
        private int dayNum;
        private Date date;

        private AppointmentButton(Practitioner practitioner, int dayNum) {
            super(makeAppointmentButtonLabel(practitioner, getAppointmentTotal(computeButtonDate(dayNum), practitioner)));
            this.practitioner = practitioner;
            this.dayNum = dayNum;
        }

        public Practitioner getPractitioner() {
            return practitioner;
        }

        public int getDayNum() {
            return dayNum;
        }
    }

    public CalendarPicker(Practitioner dentist, Practitioner hygienist, String user) {
        this.user = user;
        weekBeginning = getMonday(new Date());
        setTitle("Choose day");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 1));

        setDateLabel(weekBeginning);
        contentPane.add(dateLabel);

        JPanel weekContainer = new JPanel(new GridLayout(1, WEEK_DAYS.length));
        for (int i=0; i<WEEK_DAYS.length; i++) {
            JPanel dayContainer = new JPanel(new GridLayout(2, 1));
            JPanel buttonContainer = new JPanel(new GridLayout(1, 2));

            JLabel label = new JLabel(WEEK_DAYS[i]);
            AppointmentButton dentistAppointments = new AppointmentButton(dentist, i);
            AppointmentButton hygienistAppointments = new AppointmentButton(hygienist, i);
//            JButton dentistAppointments = new JButton(makeAppointmentButtonLabel(Practitioner.DENTIST));
//            JButton hygienistAppointments = new JButton(makeAppointmentButtonLabel(Practitioner.HYGIENIST));
            dentistAppointments.addActionListener(new AppointmentButtonHandler());
            hygienistAppointments.addActionListener(new AppointmentButtonHandler());
            dentistButtons[i] = dentistAppointments;
            hygienistButtons[i] = hygienistAppointments;

            buttonContainer.add(dentistAppointments);
            buttonContainer.add(hygienistAppointments);
            dayContainer.add(label);
            dayContainer.add(buttonContainer);
            weekContainer.add(dayContainer);
        }
        contentPane.add(weekContainer);

        JPanel controlContainer = new JPanel(new GridLayout(1, 3));
        NavigationButton prev = new NavigationButton(NavigationButtonType.PREV);
        NavigationButton today = new NavigationButton(NavigationButtonType.TODAY);
        NavigationButton next = new NavigationButton(NavigationButtonType.NEXT);

        prev.addActionListener(new NavigationButtonHandler());
        today.addActionListener(new NavigationButtonHandler());
        next.addActionListener(new NavigationButtonHandler());

        controlContainer.add(prev);
        controlContainer.add(today);
        controlContainer.add(next);

        contentPane.add(controlContainer);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Date prevMonday  = getMonday(nextWeek(new Date()));
        Date prevSunday = new Date(prevMonday.getTime() - (MILLIS_PER_DAY));
        System.out.println(prevMonday);
        System.out.println(prevSunday);
        System.out.println(getMonday(prevSunday));

        switch (user) {
            case "Hygienist":
                for (JButton b: dentistButtons) {
                    b.setEnabled(false);
                }
                break;
            case "Dentist":
                for (JButton b: hygienistButtons) {
                    b.setEnabled(false);
                }
                break;
        }
    }


    public static void main(String[] args) {
        //Look and feel
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}

        CalendarPicker cal = new CalendarPicker(new Practitioner("John Doe", "Dentist"), new Practitioner("Jane Doe", "Hygienist"), "Secretary");
        cal.setVisible(true);
    }
}
