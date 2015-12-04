package com2002.team021.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 03/12/15.
 */
public class LogTreatmentEntry extends JFrame {
    public LogTreatmentEntry() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 2));
        contentPane.add(new JLabel("Treatment"));
        contentPane.add(new JLabel("Quantity"));

        String[] treatments = { "Silver amalgam filling", "White composite resin filling", "Gold crown" };
        JComboBox treatmentSelector = new JComboBox(treatments);
        contentPane.add(treatmentSelector);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, null, 1);
        JSpinner treatmentCount = new JSpinner(spinnerModel);
        contentPane.add(treatmentCount);

        JButton addTreatmentButton = new JButton("Add treatment");
        contentPane.add(new JLabel());
        contentPane.add(addTreatmentButton);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        LogTreatmentEntry lt = new LogTreatmentEntry();
    }
}
