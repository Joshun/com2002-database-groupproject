package com2002.team021.gui;

import com2002.team021.Query;
import com2002.team021.Treatment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by joshua on 03/12/15.
 */
public class LogTreatmentEntry extends JFrame {
    private Treatment selectedTreatment;
    private Treatment selectedTreatmentQuantity;

    private JComboBox<Treatment> treatmentSelector;
    private JSpinner treatmentCounter;
    private SpinnerNumberModel spinnerModel;
    private ArrayList<Treatment> treatments;

    class TreatmentEntryHandler implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Treatment treatment = (Treatment) treatmentSelector.getSelectedItem();
            int quantity = spinnerModel.getNumber().intValue();
            System.out.println("Adding treatment \"" + treatment + "\" with quantity " + quantity);
        }
    }

    public LogTreatmentEntry() {
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
        try {
            treatments = new Query().getTreatments();
        }
        catch (java.sql.SQLException e) {
            System.out.println("Could not connect");
        }

        treatmentSelector = new JComboBox<>();
        for (Treatment t: treatments) {
            treatmentSelector.addItem(t);
        }
        contentPane.add(treatmentSelector);

        spinnerModel = new SpinnerNumberModel(1, 1, null, 1);
        treatmentCounter = new JSpinner(spinnerModel);
        contentPane.add(treatmentCounter);

        JButton addTreatmentButton = new JButton("Add treatment");
        addTreatmentButton.addActionListener(new TreatmentEntryHandler());
        contentPane.add(new JLabel());
        contentPane.add(addTreatmentButton);

        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
//        Treatment treatments[] = {
//                new Treatment("Silver amalgam filling", 90),
//                new Treatment("Composite resin filling", 150),
//                new Treatment("Gold crown", 500)
//        };
        LogTreatmentEntry lt = new LogTreatmentEntry();
    }
}
