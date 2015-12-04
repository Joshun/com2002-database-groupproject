package com2002.team021.gui;

import com2002.team021.Practitioner;

/**
 * PractitionerCalendarPicker.java
 */
public class PractitionerCalendarPicker extends CalendarPicker {

    public PractitionerCalendarPicker(Practitioner dentist, Practitioner hygienist, Practitioner user) {
        super(dentist, hygienist, user.getRole());
        setVisible(true);
    }

    public static void main(String[] args) {
        Practitioner d = new Practitioner("John Doe", "Dentist");
        PractitionerCalendarPicker pcp = new PractitionerCalendarPicker(d, new Practitioner("Jane Doe", "Hygienist"), d);
    }
}
