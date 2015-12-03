package com2002.team021.gui;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



import java.awt.geom.*;
import java.awt.font.*;

/**
 * OldCalendar.java
 */

public class OldCalendar extends JFrame {
    private Date week_beginning;
    private Date[][] month_days;
	private final int SCREEN_OFFSET = 40;
	private final String[] WEEK_DAYS = { "Mon", "Tue", "Wed", "Thu", "Fri" };
	private final int HOURS_PER_WORKING_DAY = 8;
    
    private final int CALENDAR_WIDTH = 400;
    private final int CALENDAR_HEIGHT = 400;
    private Graphics2D calendarPaintComponent;
	private final int HORIZONTAL_SPACING = 30;
	private final int VERTICAL_SPACING = 30;

	private int[] dayOffsets = new int[WEEK_DAYS.length];
	private int[] timeOffsets = new int[HOURS_PER_WORKING_DAY+1];

	private class StringMeasurer {
		Font font;
		FontRenderContext context;

		public StringMeasurer(Graphics2D g2) {
			font = g2.getFont();
			context = g2.getFontRenderContext();
		}

		public int[] getDimensions(String text) {
			Rectangle2D bounds = font.getStringBounds(text, context);
			int[] widthAndHeight = { (int)bounds.getWidth(), (int)bounds.getHeight() };
			return widthAndHeight;
		}
	}

	private void drawDayText(Graphics2D g2) {
		StringMeasurer measurer = new StringMeasurer(g2);
		int drawBeginY = SCREEN_OFFSET + 40;
		for (int i=0; i<5; i++) {
			String dayText = WEEK_DAYS[i];
			g2.drawString(dayText, 10, drawBeginY);
			dayOffsets[i] = drawBeginY;
			drawBeginY += measurer.getDimensions(dayText)[1] + VERTICAL_SPACING;
		}
	}

	private void drawTimeText(Graphics2D g2) {
		StringMeasurer measurer = new StringMeasurer(g2);
		int drawBeginX = 80;
		for (int i=0; i<HOURS_PER_WORKING_DAY+1; i++) {
			int hour = i + 9;
			String digit = String.valueOf(hour);
			String timeText = digit.length() == 1 ? "0" + digit + ":00" : hour + ":00";
			g2.drawString(timeText, drawBeginX, SCREEN_OFFSET);
			System.out.println(drawBeginX);
			timeOffsets[i] = drawBeginX;
			drawBeginX += measurer.getDimensions(timeText)[0] + HORIZONTAL_SPACING;
		}
	}

	private int computeTimeWidth(Graphics2D g2, int hour) {
		StringMeasurer measurer = new StringMeasurer(g2);
		String timeString = hour + ":00";
		return measurer.getDimensions(timeString)[0];
	}



//	private void drawAppointments(Graphics2D g2) {
//		for (int i=0; i<(8*20); i++) {
//			if (i % 3 == 0) {
//				System.out.println("Hour: " + (i/3));
//			}
//			else {
//				System.out.println("Minute: " + ((i/3)*20) - (i*20));
//			}
//		}
//	}
    
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
//			drawAppointments(g2);
    	}
    }
    
    public OldCalendar() {
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
    	JFrame calendar = new OldCalendar();
    	calendar.setVisible(true);
    }

}
