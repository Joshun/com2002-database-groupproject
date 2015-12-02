package com2002.team021.gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Calendar.java
 */

public class Calendar extends JFrame {
    private Date week_beginning;
    private Date[][] month_days;
	private final int SCREEN_OFFSET = 40;
	private final String[] WEEK_DAYS = { "Mon", "Tue", "Wed", "Thu", "Fri" };
	private final int HOURS_PER_WORKING_DAY = 8;
    
    private final int CALENDAR_WIDTH = 400;
    private final int CALENDAR_HEIGHT = 400;
    private Graphics2D calendarPaintComponent;

	private void drawDayText(Graphics2D g2) {
		for (int i=0; i<5; i++) {
			g2.drawString(WEEK_DAYS[i], 0, (SCREEN_OFFSET + 30) + i*30);
		}
	}

	private void drawTimeText(Graphics2D g2) {
		for (int i=0; i<HOURS_PER_WORKING_DAY; i++) {
			int hour = i + 9;
			String timeText = hour + ":00";
			g2.drawString(timeText, i*80, SCREEN_OFFSET);
		}
	}
    
    private class CalendarPanel extends JPanel {
    	public void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		Graphics2D g2 = (Graphics2D) g;
    		g2.setPaint(Color.black);
    		g2.setFont(new Font("Arial", Font.PLAIN, 24));
    		g2.setRenderingHint(
    				RenderingHints.KEY_ANTIALIASING,
    				RenderingHints.VALUE_ANTIALIAS_ON );
//    		g2.drawString("Test", 0, 40);
			drawDayText(g2);
			drawTimeText(g2);
    	}
    }
    
    public Calendar() {
    	setTitle("Calendar");                                   //Size of Frame
    	Container contentPane = getContentPane();               //Container
    	contentPane.setLayout(new GridLayout(2, 1));
    	CalendarPanel calendarPanel = new CalendarPanel();
    	calendarPanel.setPreferredSize(new Dimension(CALENDAR_WIDTH, CALENDAR_HEIGHT));
    	
    	JPanel buttonsGrid = new JPanel(new GridLayout(1,1));
    	JButton addAppointmentButton = new JButton("Add appointment");
    	buttonsGrid.add(addAppointmentButton);
    	contentPane.add(calendarPanel);
    	contentPane.add(buttonsGrid);
    	this.pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        }
    }
    
    public static void main (String[] args) {
    	JFrame calendar = new Calendar();
    	calendar.setVisible(true);
    }

}
