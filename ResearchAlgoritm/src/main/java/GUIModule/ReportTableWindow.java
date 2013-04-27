package GUIModule;

import javax.swing.*;
import java.awt.*;


public class ReportTableWindow extends JFrame {

    JTable table;

    ReportTableWindow(JTable table){
        super("Подробный отчет");
        this.table = table;
        initialize();

    }

    private void initialize() {
        JScrollPane scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
        setSize(500,200);
        setVisible(true);

    }


}
