package com2002.team021.gui;

import com2002.team021.Appointment;
import com2002.team021.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by joshua on 15/12/15.
 */
public class PaySummary extends JFrame {
    private Appointment appointment;
    private AppointmentDayView dayView;

    public PaySummary(Appointment appointment, AppointmentDayView dayView) {
        this.appointment = appointment;
        this.dayView = dayView;
        int appointmentTotalCost = 10;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        ArrayList<Treatment> appointmentTreatments = null;
        try {
            appointmentTreatments = appointment.getTreatments();

            for (Treatment t: appointmentTreatments) {
                contentPane.add(new JLabel(t.getName() + " | " + t.getCost()));
            }
            contentPane.add(new JLabel("=================="));
            contentPane.add(new JLabel("Total: £" + appointmentTotalCost));
        }
        catch (java.sql.SQLException e) {
            System.out.println("Couldn't get treatments " + e);
        }

    }
}
