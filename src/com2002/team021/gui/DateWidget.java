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
    private Calendar calendar;

    public DateWidget(Date currentDate) {
        System.out.println("d " + currentDate);
        setLayout(new GridLayout(2, 3));
        dayEntry = new JComboBox<>();
        monthEntry = new JComboBox<>();
        yearEntry = new JComboBox<>();

        Calendar now = Calendar.getInstance();
        int yearNow = now.get(Calendar.YEAR);

        calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);


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

    public Date getDate() {
        int day = (int) dayEntry.getSelectedItem();
        int month = (int) monthEntry.getSelectedItem() - 1;
        int year = (int) yearEntry.getSelectedItem();

        System.out.println(day + "/" + month + "/" + year);

        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        return calendar.getTime();
    }
}
