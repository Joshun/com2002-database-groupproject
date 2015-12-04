package com2002.team021.gui;

import com2002.team021.Practitioner;

/**
 * SecretaryCalendarPicker.java
 */
public class SecretaryCalendarPicker extends CalendarPicker {
    public SecretaryCalendarPicker(Practitioner dentist, Practitioner hygienist) {
        super(dentist, hygienist, "Secretary");
        setVisible(true);
    }
}
