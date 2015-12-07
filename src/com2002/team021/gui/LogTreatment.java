package com2002.team021.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LogTreatment.java
 */
public class LogTreatment extends JFrame {

    private JPanel appointmentListingPane;

    private class AddTreatmentButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            appointmentListingPane.add(new JLabel("New treatment!"));
            validate();
            System.out.println("Added");
        }
    }

    public LogTreatment() {
        setTitle("Log treatment");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 1));

        JLabel appointmentLabel = new JLabel("Add treatments for appointment <appointment>");
        appointmentListingPane = new JPanel();
        appointmentListingPane.setLayout(new BoxLayout(appointmentListingPane, BoxLayout.PAGE_AXIS));
        JButton addTreatmentButton = new JButton("***Add treatment***");
        addTreatmentButton.addActionListener(new AddTreatmentButtonHandler());
        contentPane.add(appointmentLabel);
        contentPane.add(appointmentListingPane);
        contentPane.add(addTreatmentButton);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        LogTreatment lt = new LogTreatment();
        lt.setVisible(true);
    }
}
