package com2002.team021.gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * ChoiceScreen.java         1.1 01/12/
 *
 */

/**
 * ChoiceScreen.java
 *
 *
 *
 * @version 1.1
 *
 * @author
 *
 */

public class ChoiceScreen extends JFrame {
    private String[] choices;

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ChoiceButton button = (ChoiceButton) e.getSource();
            System.out.println("You clicked " + button.getIndex());
        }
    }

    public ChoiceScreen(String title, String[] choices) {
        setTitle(title);
        this.choices = choices;
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        ButtonHandler buttonListener = new ButtonHandler();
        for (int i=0; i<choices.length; i++) {
            ChoiceButton newButton = new ChoiceButton(choices[i], i);
            newButton.addActionListener(buttonListener);
            contentPane.add(newButton);
        }
        this.pack();
    }

    private class ChoiceButton extends JButton {
        private int index;

        public ChoiceButton(String text, int index) {
            super(text);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
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
