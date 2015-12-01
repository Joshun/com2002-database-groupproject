package com2002.team021.gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ChoiceScreen.java
 */

public class ChoiceScreen extends JFrame {
    private String[] choices;
    public ChoiceScreen(String title, String[] choices) {
        setTitle(title);
        this.choices = choices;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        for (String choice: choices) {
            contentPane.add(new JButton(choice));
        }
        this.pack();
    }

    public static void main(String[] args) {
        String[] secretaryChoices = { "View / Edit Calendar", "Patient Management"};
        String[] practitionerChoices = { "View Calendar", "Log Treatment" };
        JFrame choiceScreen = new ChoiceScreen("Secretary choice", secretaryChoices);
        JFrame choiceScreen2 = new ChoiceScreen("Practitioner choice", practitionerChoices);
        choiceScreen.setVisible(true);
        choiceScreen2.setVisible(true);
    }


}
