package com2002.team021.gui;

import com2002.team021.Address;
import com2002.team021.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by joshua on 16/12/15.
 */
public class AddAddress extends JFrame {
    private JTextField houseNoEntry;
    private JTextField streetNameEntry;
    private JTextField districtEntry;
    private JTextField cityEntry;
    private JTextField postcodeEntry;
    private ErrorHandler errorHandler;
    private PatientManager patientManager;

    private class AddAddressHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String houseNo = houseNoEntry.getText();
            String streetName = streetNameEntry.getText();
            String district = districtEntry.getText();
            String city = cityEntry.getText();
            String postcode = postcodeEntry.getText();

            Address newAddress = new Address(houseNo, streetName, district, city, postcode);
            try {
                new Query().addAddress(newAddress);
            }
            catch (java.sql.SQLException ex) {
                errorHandler.showDialog("Couldn't add address", ex);
                return;
            }
            errorHandler.showInfo("Address has been added successfully.");
            patientManager.setEnabled(true);
            dispose();
        }
    }

    public AddAddress(PatientManager pm) {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pm.setEnabled(true);
            }
        });

        this.patientManager = pm;
        errorHandler = new ErrorHandler(this);
        // houseno, streetname, district, city, postcode
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(6, 2));
        contentPane.add(new JLabel("House no.: "));
        houseNoEntry = new JTextField();
        contentPane.add(houseNoEntry);
        contentPane.add(new JLabel("Street name: "));
        streetNameEntry = new JTextField();
        contentPane.add(streetNameEntry);
        contentPane.add(new JLabel("District: "));
        districtEntry = new JTextField();
        contentPane.add(districtEntry);
        contentPane.add(new JLabel("City: "));
        cityEntry = new JTextField();
        contentPane.add(cityEntry);
        contentPane.add(new JLabel("Postcode: "));
        postcodeEntry = new JTextField();
        contentPane.add(postcodeEntry);
        contentPane.add(new JLabel());
        JButton addAddress = new JButton("Add Address");
        addAddress.addActionListener(new AddAddressHandler());
        contentPane.add(addAddress);

        pack();
        setVisible(true);
    }

}
