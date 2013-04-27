package GUIModule;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;


public class ActionCompareFullReportButton implements ActionListener {

    JTable table;
    ArrayList<HashMap<String,Object>> previousStart;

    public ActionCompareFullReportButton(JTable table, ArrayList<HashMap<String, Object>> previousStart) {
        this.table = table;
        this.previousStart = previousStart;
    }


    public void actionPerformed(ActionEvent e) {
        int [] indexes;
        indexes = table.getSelectedRows();

        if (indexes.length<=1){
            // TODO: Сделать вывод сообщения с подсказкой в диалоговом окне
            return;
        }
        for(int i=0;i<indexes.length;i++){
            if (indexes[i] >= previousStart.size()) {
                return;
            }
            if(previousStart.get(indexes[i]) == null){
                return;
                // TODO: Сделать вывод сообщения с подсказкой в диалоговом окне
            }
        }
        int maxPackets = 0;
        for(int i=0;i<indexes.length;i++){
            int packetCount = (Integer) previousStart.get(i).get("Всего пакетов");
            if(packetCount>maxPackets){
                maxPackets = packetCount;
            }
        }
        int rowCount = ReportTables.calcRowCount(maxPackets);
        JTable fullTable = ReportTables.createFullTable(rowCount, indexes, maxPackets / 10);
        ReportTables.fillTable(fullTable, previousStart, indexes, maxPackets / 10);
        new ReportTableWindow(fullTable);

    }




}
