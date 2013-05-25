package GUIModule;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class ActionResultsReport implements ActionListener {
    public ArrayList<ArrayList<String>> previousResult;
    public JTable table;

    public ActionResultsReport(JTable table, ArrayList<ArrayList<String>> previousResult) {
        this.previousResult = previousResult;
        this.table = table;
    }


    public void actionPerformed(ActionEvent e) {
        int [] indexes;
        indexes = table.getSelectedRows();

        if (indexes.length==0){
            // TODO: Сделать вывод сообщения с подсказкой в диалоговом окне
            return;
        }
        if(indexes[0]>=previousResult.size()){
            return;
        }
        new ResultsWindow(previousResult.get(indexes[0]));

    }
}
