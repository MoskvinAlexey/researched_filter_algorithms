package GUIModule;

import algorithmModule.AbstractAlgorithm;
import algorithmModule.ControlAlgorithm;
import algorithmModule.SimpleAlgorithm;
import filterRuleModule.FilterRules;
import statisticsModule.CollectStatistics;
import statisticsModule.WrapInProxy;
import trafficModule.TrafficGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;



public class ActionStartButton implements ActionListener {
    JLabel algorithmLabel;
    JTextField trafficField;
    JTextField ruleField;
    JTable table;
    JFrame mainFrame;
    JDialog progressWindow;


    ArrayList<HashMap<String,Object>> previousStartStatistics;
    ArrayList<ArrayList<String>> previousStartResult;
    private int runNumber;



    public ActionStartButton(JLabel algorithmLabel, JTextField trafficField, JTextField ruleField,
                            JTable table, JFrame mainFrame) {
        this.algorithmLabel = algorithmLabel;
        this.trafficField = trafficField;
        this.ruleField = ruleField;
        this.table = table;
        this.mainFrame = mainFrame;

        previousStartStatistics = new ArrayList<HashMap<String,Object>>();
        previousStartResult = new ArrayList<ArrayList<String>>();
        runNumber = 0;
        initProgressWindow();
    }

    private void initProgressWindow(){
        progressWindow = new JDialog(mainFrame, "Прогресс", true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        progressWindow.add(BorderLayout.CENTER, progressBar);
        progressWindow.add(BorderLayout.NORTH, new JLabel("Выполняется алгоритм..."));
        progressWindow.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        progressWindow.setSize(300, 75);
        progressWindow.setLocationRelativeTo(mainFrame);
    }

    public void actionPerformed(ActionEvent e) {
        new SwingWorker()
        {


            protected Object doInBackground() throws Exception {

                progressWindow.setVisible(true);
                return null;
            }
        }.execute();


        new SwingWorker()
        {

            protected Object doInBackground() throws Exception {
                mainFrame.setEnabled(false);
                CollectStatistics.setAlgorithmName(algorithmLabel.getText());
                FilterRules filterRules= WrapInProxy.wrapFilterRulesInpRoxy(new FilterRules());
                filterRules.loadFilterRules(ruleField.getText());
                // TODO:Добавить логику подключения алгоритма в зависимости от текста в
//                Class clazz = Class.forName("SimpleAlgorithm");
//                clazz.getConstructor().newInstance()
                AbstractAlgorithm simpleAlg = WrapInProxy.wrapAlgorithmInProxy(new SimpleAlgorithm());
                simpleAlg.setFilterRules(filterRules);

                ControlAlgorithm algUnderControl = new ControlAlgorithm();
                algUnderControl.setAlgorithm(simpleAlg);

                Thread th = new Thread(new TrafficGenerator(algUnderControl, trafficField.getText()));
                //TODO: Добавить валидацию полей с путями до файлов: 1.поля пустые 2.файлы пустые 3.файлы не того формата
                th.start();
                try {
                    th.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Done!");

                updateTableInformation(CollectStatistics.getSummaryStatistics(), table, runNumber);
                runNumber++;
                previousStartStatistics.add(CollectStatistics.getFullStatistics());
                previousStartResult.add(CollectStatistics.getResult());
                CollectStatistics.resetAll();
                FilterRules.resetFilterRules();
                progressWindow.setVisible(false);
                mainFrame.setEnabled(true);
                return null;
            }
        }.execute();

    }

    private void updateTableInformation(HashMap<String, Object> summaryStatistics, JTable table,int runNumber) {
        Set<String> keys = summaryStatistics.keySet();
        for(String key : keys){
            int index = findColumnIndex(key, table);
            if(index!=-1){
                table.setValueAt(summaryStatistics.get(key),runNumber,index);
            }
        }
    }

    private int findColumnIndex(String key, JTable table) {
        for(int i =0;i<table.getColumnCount();i++){
            if(table.getColumnName(i).equals(key)){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<HashMap<String, Object>> getPreviousStartStatistics() {
        return previousStartStatistics;
    }
    public ArrayList<ArrayList<String>> getPreviousStartResult(){
        return previousStartResult;
    }
}
