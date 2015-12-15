package com2002.team021.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by joshua on 15/12/15.
 */
public class DateWidget extends JPanel {
    private JComboBox<Integer> dayEntry;
    private JComboBox<Integer> monthEntry;
    private JComboBox<Integer> yearEntry;

    public DateWidget(Date currentDate) {
        setLayout(new GridLayout(2, 3));
        dayEntry = new JComboBox<>();
        monthEntry = new JComboBox<>();
        yearEntry = new JComboBox<>();

        Calendar now = Calendar.getInstance();
        int yearNow = now.get(Calendar.YEAR);

        Calendar previousCal = Calendar.getInstance();
        previousCal.setTime(currentDate);
        int currentDay = now.get(Calendar.DAY_OF_MONTH);
        int currentMonth = now.get(Calendar.MONTH);
        int currentYear = now.get(Calendar.YEAR);


        for (int i = 0; i < 31; i++) {
            dayEntry.addItem(new Integer(i + 1));
        }
        for (int i = 0; i < 12; i++) {
            monthEntry.addItem(new Integer(i + 1));
        }

        for (int i = 0; i < 120; i++) {
            yearEntry.addItem(new Integer(yearNow - i));
        }

        dayEntry.setSelectedIndex(currentDay-1);
        monthEntry.setSelectedIndex(currentMonth);
        yearEntry.setSelectedIndex(yearNow - currentYear);

        add(new JLabel("Day"));
        add(new JLabel("Month"));
        add(new JLabel("Year"));
        add(dayEntry);
        add(monthEntry);
        add(yearEntry);
    }

    private Date getDate() {
        int day = (int) dayEntry.getSelectedItem();
        int month = (int) monthEntry.getSelectedItem();
        int year = (int) yearEntry.getSelectedItem();

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        return cal.getTime();
    }
}
