package com2002.team021.gui;

import com2002.team021.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by joshua on 03/12/15.
 */
public class LogTreatmentEntry extends JFrame {
    public LogTreatmentEntry(Treatment[] treatments) {
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 2));
        contentPane.add(new JLabel("Treatment"));
        contentPane.add(new JLabel("Quantity"));

//        String[] treatments = { "Silver amalgam filling", "White composite resin filling", "Gold crown";
//        ArrayList<String> treatmentList = new ArrayList<>();
//        for (Treatment t: treatments) {
//            String treatmentString = t.getName() + " (" + t.getCost() + ")";
//            treatmentList.add(treatmentString);
//        }


        JComboBox<Treatment> treatmentSelector = new JComboBox<>(treatments);
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
        Treatment treatments[] = {
                new Treatment("Silver amalgam filling", 90),
                new Treatment("Composite resin filling", 150),
                new Treatment("Gold crown", 500)
        };
        LogTreatmentEntry lt = new LogTreatmentEntry(treatments);
    }
}
