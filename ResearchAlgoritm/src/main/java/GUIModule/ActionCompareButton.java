package GUIModule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class ActionCompareButton implements ActionListener {
    JTable table;
    String [] rowTitles = {
            "Алгоритм",
            "Всего пакетов",
            "Правил фильтрации",
            "Время применения алгоритма для 10ти пакетов",
            "Общее время выполнения алгоритма",
            "Время подготовки для 10 пакетов",
            "Общее время подготовки алгоритма",
            "Применено правил к пакету (в среднем)"
    };

    ArrayList<HashMap<String,Object>> previousStart;
    public ActionCompareButton(JTable table, ArrayList<HashMap<String,Object>> previousStart) {
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
        int rowCount = 8 + 2*(maxPackets/10);  //8 строк всегда(см. CollectStatistics.getFullStatistics() + времена за каждые 10 пакетов
        JTable fullTable = createFullTable(rowCount, indexes,maxPackets/10);
        fillTable(fullTable,previousStart,indexes,maxPackets/10);
        new FullTableWindow(fullTable);

    }

    private void fillTable(JTable fullTable, ArrayList<HashMap<String, Object>> previousStart, int[] indexes,int dynamicRowCount) {

            for(int i=0;i<indexes.length;i++){
                HashMap<String, Object> nextStat = previousStart.get(indexes[i]);
                int row = 0;
                for(int k=0;k<rowTitles.length;k++){
                    Object nextVal = nextStat.get(rowTitles[k]);
                    if(nextVal.getClass()==ArrayList.class){
                        row++;
                        ArrayList list = (ArrayList) nextStat.get(rowTitles[k]);
                        for(int j=0;j<list.size();j++){
                           fullTable.setValueAt(list.get(j),row++,i+1);
                        }
                    }
                    else{
                      fullTable.setValueAt(nextVal,row++,i+1);
                    }
                }
            }

    }
    private JTable createFullTable(int rowCount, int [] indexes,int dynamicRowCount ) {

        int colCount = indexes.length;
        String [] columns = new String [colCount+1];
        columns[0] = "";
        for (int i=1;i<colCount+1;i++){
            columns[i] = "Запуск №" + indexes[i-1];
        }
        DefaultTableModel model = new DefaultTableModel(rowCount, colCount){
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        model.setColumnIdentifiers(columns);
        JTable table = new JTable(model);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        ///////////////////////////////Don`t look that/////////////////////////////////

        for(int i =0; i<4;i++){
            table.setValueAt(rowTitles[i],i,0);
        }
        int j=1;
        for(int i=4; i<dynamicRowCount+4;i++){
            table.setValueAt(j++,i,0);
        }
        table.setValueAt(rowTitles[4],4+dynamicRowCount,0);
        table.setValueAt(rowTitles[5],5+dynamicRowCount,0);
        j=1;
        for(int k = 6+dynamicRowCount;k<rowCount-1;k++){
            table.setValueAt(j++,k,0);
        }
        table.setValueAt(rowTitles[6],rowCount-2,0);
        table.setValueAt(rowTitles[7],rowCount-1,0);
        //////////////////////////////////////////////////////////////////////////////////

        return table;
    }

}
