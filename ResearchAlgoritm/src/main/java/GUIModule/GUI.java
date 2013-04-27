package GUIModule;

import algorithmModule.AbstractAlgorithm;
import algorithmModule.ControlAlgorithm;
import algorithmModule.SimpleAlgorithm;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import filterRuleModule.FilterRules;
import statisticsModule.CollectStatistics;
import statisticsModule.WrapInProxy;
import trafficModule.TrafficGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class GUI extends JFrame {

    private final int MAX_RUN_NUMBER = 20;
    private int runNumber;
    ArrayList<HashMap<String,Object>> previousStart;

    public GUI(){
        super("Исследование алгоритмов классификации пакетного трафика");
        runNumber = 0;
        previousStart = new ArrayList<HashMap<String,Object>>();
        initialize();

    }

    private void initialize(){
        FormLayout layout = new FormLayout(
                "left:pref,10dlu, 80dlu, 10dlu, 60dlu, 200dlu",
                "5dlu,p, 3dlu, p, 10dlu, 30dlu, 30dlu, 3dlu, p, 10dlu, p, 10dlu, p, 10dlu, p, 10dlu,p,120dlu");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenu helpMenu = new JMenu("Справка");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);


        builder.addSeparator("Алгоритм", cc.xyw(1,2,6));
        builder.add(new JButton("Выбрать алгоритм"), cc.xy(1,4));
        JLabel algorithmLabel = new JLabel("Последовательный поиск");
        builder.add(algorithmLabel, cc.xyw(3, 4, 3));
        builder.addSeparator("Входные данные", cc.xyw(1, 6, 6));

        builder.add(new JLabel("Источник трафика:"), cc.xy(1,7));
        JTextField trafficField = new JTextField();
        trafficField.setPreferredSize(new Dimension(80,23));
        builder.add(trafficField, cc.xy(3, 7));
        JButton browserToTrafficFile = new JButton("Обзор");
        addActionForBrowserButton(browserToTrafficFile,trafficField);
        builder.add(browserToTrafficFile, cc.xy(5, 7));

        builder.add(new JLabel("Правила фильтрации:"), cc.xy(1,9));
        JTextField ruleField = new JTextField();
        ruleField.setPreferredSize(new Dimension(80,23));
        builder.add(ruleField, cc.xy(3,9));
        JButton browserForRuleFile = new JButton("Обзор");
        addActionForBrowserButton(browserForRuleFile,ruleField);
        builder.add(browserForRuleFile, cc.xy(5,9));

        builder.addSeparator("Последние запуски", cc.xyw(5,11,2));

        JTable table = createSummaryTable();
        JScrollPane scrollPane = new JScrollPane(table);
        builder.add(scrollPane,cc.xywh(5, 13, 2, 6));


        JButton startButton = new JButton("Запустить");
        startButton.addActionListener(new ActionStartButton(algorithmLabel,trafficField,ruleField, table));
        startButton.setPreferredSize(new Dimension(200,30));

        builder.add(startButton, cc.xyw(1,13,3,CellConstraints.CENTER,CellConstraints.BOTTOM));

        JButton reportButton = new JButton("Подробный отчет");
        reportButton.setPreferredSize(new Dimension(200,30));

        builder.add(reportButton, cc.xyw(1,15,3,CellConstraints.CENTER,CellConstraints.BOTTOM));

        JButton compareButton = new JButton("Сравнить результаты");
        compareButton.addActionListener(new ActionCompareButton(table, previousStart));
        compareButton.setPreferredSize(new Dimension(200,30));

        builder.add(compareButton, cc.xyw(1,17,3,CellConstraints.CENTER,CellConstraints.BOTTOM));


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(menuBar, BorderLayout.NORTH);
        mainPanel.add(builder.getPanel(),BorderLayout.SOUTH);
        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setResizable(false);



        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        } );
    }

    private void addActionForBrowserButton(JButton button, final JTextField textField) {

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                fileChooser.setAcceptAllFileFilterUsed(false);

                int rVal = fileChooser.showOpenDialog(null);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    textField.setText(fileChooser.getSelectedFile().toString());
                }
            }
        });


    }

    private JTable createSummaryTable() {
        String[] columnNames = {
                "Алгоритм",
                "Правил фильтрации",
                "Всего пакетов",
                "Время работы"
        };
        int numRows = MAX_RUN_NUMBER;
        DefaultTableModel model = new DefaultTableModel(numRows, columnNames.length){
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        model.setColumnIdentifiers(columnNames);
        JTable table = new JTable(model);
        return table;
    }




    private class ActionStartButton implements ActionListener {
        JLabel algorithmLabel;
        JTextField trafficField;
        JTextField ruleField;
        JTable table;

        public ActionStartButton(JLabel algorithmLabel, JTextField trafficField, JTextField ruleField, JTable table) {
            this.algorithmLabel = algorithmLabel;
            this.trafficField = trafficField;
            this.ruleField = ruleField;
            this.table = table;
        }


        public void actionPerformed(ActionEvent e) {
            //TODO: Добавить валидацию полей с путями до файлов: 1.поля пустые 2.файлы пустые 3.файлы не того формата

            setEnabled(false);
            CollectStatistics.setAlgorithmName(algorithmLabel.getText());
            FilterRules filterRules= WrapInProxy.wrapFilterRulesInpRoxy(new FilterRules());
            filterRules.loadFilterRules(ruleField.getText());

            // TODO:Добавить логику подключения алгоритма в зависимости от текста в algorithmLabel
            AbstractAlgorithm simpleAlg = WrapInProxy.wrapAlgorithmInProxy(new SimpleAlgorithm());
            simpleAlg.setFilterRules(filterRules);

            ControlAlgorithm algUnderControl = new ControlAlgorithm();
            algUnderControl.setAlgorithm(simpleAlg);

            Thread th = new Thread(new TrafficGenerator(algUnderControl, trafficField.getText()));
            th.start();
            try {
                th.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("Done!");
            updateTableInformation(CollectStatistics.getSummaryStatistics(), table, runNumber);
            runNumber++;
            previousStart.add(CollectStatistics.getFullStatistics());
            CollectStatistics.resetAll();
            FilterRules.resetFilterRules();
            setEnabled(true);
        }
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

    public static void main(String[] args){
//        try {
//            UIManager.setLookAndFeel(
//                    UIManager.getCrossPlatformLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        new GUI();
    }


}
