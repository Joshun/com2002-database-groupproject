package com2002.team021.gui;

import com2002.team021.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by joshua on 15/12/15.
 */
public class PaySummary extends JFrame {
    private Appointment appointment;
    private AppointmentDayView dayView;
    private ErrorHandler errorHandler;

    private class MarkPaidHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Patient patient = appointment.getPatient();
            Practitioner practitioner = appointment.getPractitioner();
            Date start = appointment.getStart();
            Date end = appointment.getEnd();
            try {
                ArrayList<Treatment> treatments = appointment.getTreatments();
                Appointment newAppointment = new Appointment(start, end, patient, practitioner, treatments, 0);
                dayView.updateAppointment(appointment, newAppointment);
                dayView.setEnabled(true);
                dispose();
            }
            catch (java.sql.SQLException ex) {
                errorHandler.showDialog("Couldn't mark paid ", ex);
            }
        }
    }

    private class CancelHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            dayView.setEnabled(true);
            dispose();
        }
    }

    public PaySummary(Appointment appointment, AppointmentDayView dayView) {
        errorHandler = new ErrorHandler(this);
        this.appointment = appointment;
        this.dayView = dayView;
        int appointmentTotalCost = appointment.getAmountDue();
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        ArrayList<Treatment> appointmentTreatments = null;
        try {
            appointmentTreatments = appointment.getTreatments();

            for (Treatment t: appointmentTreatments) {
                contentPane.add(new JLabel(t.getName() + " | £" + (t.getCost()/100)));
            }
            contentPane.add(new JLabel("=================="));
            contentPane.add(new JLabel("Total: £" + (appointmentTotalCost/100)));
        }
        catch (java.sql.SQLException e) {
            errorHandler.showDialog("Couldn't get treatments ", e);
        }

        JButton markPaid = new JButton("Mark Paid");
        markPaid.addActionListener(new MarkPaidHandler());
        if (appointment.getAmountDue() == 0) {
            markPaid.setText("All Costs Paid");
            markPaid.setEnabled(false);
        }
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new CancelHandler());
        JPanel buttonGrid = new JPanel(new GridLayout(1, 2));
        buttonGrid.add(markPaid);
        buttonGrid.add(cancel);
        contentPane.add(buttonGrid);

        pack();
        setVisible(true);

    }
}
