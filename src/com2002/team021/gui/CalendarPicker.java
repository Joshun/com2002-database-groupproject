package com2002.team021.gui;

import java.util.Calendar;
import java.util.Date;


import javax.swing.*;
import java.awt.*;

/**
 * Calendar.java
 */
public class CalendarPicker extends JFrame {
    private enum Practitioner  { DENTIST, HYGIENIST }
    private final String[] WEEK_DAYS = { "Mon", "Tue", "Wed", "Thu", "Fri" };

    private boolean noModify = false;
    private JButton[] dentistButtons = new JButton[WEEK_DAYS.length];
    private JButton[] hygienistButtons = new JButton[WEEK_DAYS.length];


    private String makeAppointmentButtonLabel(Practitioner p) {
        int numAppointments = 0;
        String label;
        switch (p) {
            case DENTIST:
                label = "Dentist " + "(" + numAppointments + ")";
                break;
            case HYGIENIST:
                label = "Hygienist " + "(" + numAppointments + ")";
                break;
            default:
                label = "Undefined";
        }
        return label;
    }

    private Date nextWeek(Date current) {

        long theFuture = current.getTime() + (86400 * 7 * 1000);
        return new Date(theFuture);
    }

    private Date prevWeek(Date current) {
        long thePast = current.getTime() - (86400 * 7 * 1000);
        return new Date(thePast);
    }

    private Date getMonday(Date current) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        System.out.println(calendar);
        System.out.println(calendar.DAY_OF_WEEK);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if ( weekDay != Calendar.MONDAY ) {
            System.out.println(weekDay);
            long currentTimestamp = current.getTime();
            long mondayTimestamp;
            if ( weekDay == Calendar.SUNDAY) {
                // SUNDAY has index 1 but we want to go back 6 days to get to the last Monday
                mondayTimestamp = currentTimestamp - (86400 * 6 * 1000);
            }
            else {
                // weekDay-2 is used because SUNDAY has index 1, MONDAY has index 2 etc. and we juts want the difference
                mondayTimestamp = currentTimestamp - (86400 * (weekDay - 2) * 1000);
            }
            return new Date(mondayTimestamp);
        }
        else {
            return current;
        }
    }

    public CalendarPicker() {
        setTitle("Choose day");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 1));
        JPanel weekContainer = new JPanel(new GridLayout(1, WEEK_DAYS.length));
        for (int i=0; i<WEEK_DAYS.length; i++) {
            JPanel dayContainer = new JPanel(new GridLayout(2, 1));
            JPanel buttonContainer = new JPanel(new GridLayout(1, 2));

            JLabel label = new JLabel(WEEK_DAYS[i]);
            JButton dentistAppointments = new JButton(makeAppointmentButtonLabel(Practitioner.DENTIST));
            JButton hygienistAppointments = new JButton(makeAppointmentButtonLabel(Practitioner.HYGIENIST));
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
        JButton prev = new JButton("<");
        JButton next = new JButton(">");
        JButton today = new JButton("today");
        controlContainer.add(prev);
        controlContainer.add(today);
        controlContainer.add(next);
        contentPane.add(controlContainer);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(getMonday(nextWeek(new Date())));
    }

    public CalendarPicker(Practitioner p) {
        this();
        noModify = true;
        switch (p) {
            case HYGIENIST:
                for (JButton b: dentistButtons) {
                    b.setEnabled(false);
                }
                break;
            case DENTIST:
                for (JButton b: hygienistButtons) {
                    b.setEnabled(false);
                }
                break;
        }
    }

    public static void main(String[] args) {
        CalendarPicker cal = new CalendarPicker(Practitioner.HYGIENIST);
        cal.setVisible(true);
    }
}
