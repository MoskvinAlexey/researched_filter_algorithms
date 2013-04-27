package GUIModule;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportTables {

    final static String [] rowTitles = { //(см. CollectStatistics.getFullStatistics()
            "Алгоритм",
            "Всего пакетов",
            "Правил фильтрации",
            "Время применения алгоритма для 10ти пакетов",
            "Общее время выполнения алгоритма",
            "Время подготовки для 10 пакетов",
            "Общее время подготовки алгоритма",
            "Применено правил к пакету (в среднем)"
    };

    public static JTable createFullTable(int rowCount, int [] indexes,int dynamicRowCount ) {

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
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        fillRowTitle(table, rowCount, dynamicRowCount);
        return table;
    }


    public static void fillTable(JTable fullTable, ArrayList<HashMap<String, Object>> previousStart, int[] indexes,int dynamicRowCount) {

        for(int i=0;i<indexes.length;i++){
            HashMap<String, Object> nextStat = previousStart.get(indexes[i]);
            int row = 0;
            for(int k=0;k<rowTitles.length;k++){
                Object nextVal = nextStat.get(rowTitles[k]);
                if(nextVal.getClass()==ArrayList.class){
                    row++;
                    ArrayList list = (ArrayList) nextStat.get(rowTitles[k]);
                    for(int j=0;j<dynamicRowCount;j++){
                        if(j<list.size()){
                            fullTable.setValueAt(list.get(j),row++,i+1);
                        }
                        else{
                            row++;
                        }
                    }
                }
                else{
                    fullTable.setValueAt(nextVal,row++,i+1);
                }
            }
        }

    }

    public static int calcRowCount(int packetsNumber){
        return rowTitles.length + 2*(packetsNumber/10);
    }


    /**
     * См. rowTitles
     * @param table
     * @param rowCount
     * @param dynamicRowCount
     */
    private static void fillRowTitle(JTable table,int rowCount, int dynamicRowCount){

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


    }
}
