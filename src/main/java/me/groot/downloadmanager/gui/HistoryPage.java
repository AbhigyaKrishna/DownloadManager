package me.groot.downloadmanager.gui;

import me.groot.downloadmanager.jooq.codegen.Tables;
import org.jooq.DSLContext;
import reactor.core.publisher.Flux;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HistoryPage extends Screen {
    private final DSLContext ctx;
    public HistoryPage(DSLContext ctx){
        super("History");
        this.ctx = ctx;
    }


    @Override
    public void initialize() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Data for the table
//        String [][] data = {
//                {"https://www.google.com", "abc.xyz", "03.04.2024", "08:34:54", "23.43MB"},
//                {"https://www.yahoo.com", "abc.xyz", "03.04.2024", "08:34:54", "23.43MB"},
//                {"https://www.google.com", "abc.xyz", "03.04.2024", "08:34:54", "23.43MB"},
//                {"https://www.google.com", "abc.xyz", "03.04.2024", "08:34:54", "23.43MB"},
//                {"https://www.google.com", "abc.xyz", "03.04.2024", "08:34:54", "23.43MB"}
//        };

        // Column names
        String[] columnNames = {"URL", "File Name", "DateTime", "File Size"};

        // Create a default table model
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Return the appropriate class for each column
                return String.class;
            }
        };

        // Create the table with the default table model
        JTable table = new JTable(model);

        // Set grid lines visibility
        table.setShowGrid(true);
        table.setGridColor(new Color(170, 132, 220));
        table.setCellSelectionEnabled(false);
        table.setDragEnabled(false);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(600); // URL column
        table.getColumnModel().getColumn(1).setPreferredWidth(320); // File Name column
        table.getColumnModel().getColumn(2).setPreferredWidth(300); // DateTime column

        table.getColumnModel().getColumn(4).setPreferredWidth(350); // File Size column

        // Set font for table cells
        table.setFont(new Font("Arial", Font.PLAIN, 14)); // Change font size for cell values

        // Customizing the appearance of column headers
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16)); // Change font size for headers

        // Custom renderer for alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            Color alternateColor = new Color(209,185,225); // Light gray color for alternate rows

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && row % 2 == 0) {
                    c.setBackground(alternateColor);
                } else {
                    c.setBackground(new Color(188,151,213));
                }
                return c;
            }
        });

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Create "Clear History" button
        JButton clearButton = new JButton("Clear History");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the table data
                model.setRowCount(0);
            }
        });

        Flux.from(ctx.select(Tables.HISTORY)).subscribe(record ->{
            model.addRow(new String[]{
                    record.get(Tables.HISTORY.FILE_URL),
                    record.get(Tables.HISTORY.FILE_NAME),
                    record.get(Tables.HISTORY.FILE_DATETIME).toString(),
                    String.format("%.2f MB",((double) record.get(Tables.HISTORY.FILE_SIZE)/1e+6))
            });
        });

        // Create panel to hold the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(clearButton);

        // Add the scroll pane and button panel to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

        // Pack the frame and make it visible
        pack();
        setLocationRelativeTo(null); // Center the frame
        setBackground(new Color(170, 132, 220));
        setSize(400, 300);


    }
}
