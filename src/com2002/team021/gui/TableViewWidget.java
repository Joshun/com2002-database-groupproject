package com2002.team021.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by joshua on 08/12/15.
 */
public abstract class TableViewWidget extends JPanel {
    private DefaultTableModel tableModel;
    private int selectedRow;

    public TableViewWidget(String[] headings) {

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        JScrollPane tableContainer = new JScrollPane(table);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                Point rowPoint = event.getPoint();
                int row = table.rowAtPoint(rowPoint);
                if (row >= 0) {
                    selectedRow = row;
                    System.out.println("Selected: " + selectedRow);
                    onMouseClick();
                }
            }
        });

        for (String s: headings) {
            tableModel.addColumn(s);
        }
    }

    public void addItem(Object o) {
        tableModel.addRow(itemToRow(o));
    }

    public void delItem(int index) {
        tableModel.removeRow(index);
    }

    public abstract void updateItem(Object o, int index);

    public abstract void onMouseClick();
    public abstract String[] itemToRow(Object o);

    public int getSelectedRow() {
        return selectedRow;
    }
}
