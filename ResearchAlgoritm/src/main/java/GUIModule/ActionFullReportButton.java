package GUIModule;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;


public class ActionFullReportButton implements ActionListener {

    JTable table;
    ArrayList<HashMap<String,Object>> previousStart;

    ActionFullReportButton(JTable table,  ArrayList<HashMap<String,Object>> previousStart){
        this.previousStart = previousStart;
        this.table = table;
    }

    public void actionPerformed(ActionEvent e) {

        int [] indexes;
        indexes = table.getSelectedRows();

        if (indexes.length==0){
            // TODO: Сделать вывод сообщения с подсказкой в диалоговом окне
            return;
        }
        if(indexes[0]>=previousStart.size()){
            return;
        }
        int packets = (Integer) previousStart.get(indexes[0]).get("Всего пакетов");
        int rowCount =ReportTables.calcRowCount(packets) ;
        JTable fullReportTable = ReportTables.createFullTable(rowCount,indexes, packets/10);
        ReportTables.fillTable(fullReportTable, previousStart, indexes, packets / 10);
        new ReportTableWindow(fullReportTable);

    }
}
