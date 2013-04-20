package GUIModule;

import algorithmModule.AbstractAlgorithm;
import algorithmModule.ControlAlgorithm;
import algorithmModule.SimpleAlgorithm;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import filterRuleModule.FilterRules;
import statisticsModule.WrapInProxy;
import trafficModule.TrafficGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GUI extends JFrame {

    public GUI(){
        super("ResearchAlgorithm");

        FormLayout layout = new FormLayout(
                "left:pref,10dlu, 80dlu, 10dlu, 60dlu, 150dlu",
                "5dlu,p, 3dlu, p, 10dlu, 30dlu, 30dlu, 3dlu, p, 10dlu, p, 10dlu, p, 10dlu, p, 10dlu,p,90dlu");

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

        JTable table = createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        builder.add(scrollPane,cc.xywh(5, 13, 2, 6));


        JButton startButton = new JButton("Запустить");
        startButton.addActionListener(new ActionStartButton(algorithmLabel,trafficField,ruleField));
        startButton.setPreferredSize(new Dimension(200,30));

        builder.add(startButton, cc.xyw(1,13,3,CellConstraints.CENTER,CellConstraints.BOTTOM));

        JButton reportButton = new JButton("Подробный отчет");
        reportButton.setPreferredSize(new Dimension(200,30));

        builder.add(reportButton, cc.xyw(1,15,3,CellConstraints.CENTER,CellConstraints.BOTTOM));

        JButton compareButton = new JButton("Сравнить результаты");
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

    private JTable createTable() {
        String[] columnNames = {
                "Алгоритм",
                "Правил фильтрации",
                "Всего пакетов",
                "Время работы"
        };
        int numRows = 20;
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

    public static void main(String[] args){
        new GUI();
    }


    private class ActionStartButton implements ActionListener {
        JLabel algorithmLabel;
        JTextField trafficField;
        JTextField ruleField;

        public ActionStartButton(JLabel algorithmLabel, JTextField trafficField, JTextField ruleField) {
            this.algorithmLabel = algorithmLabel;
            this.trafficField = trafficField;
            this.ruleField = ruleField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: Добавить валидацию полей с путями до файлов: 1.поля пустые 2.файлы пустые 3.файлы не того формата

            setEnabled(false);
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

            setEnabled(true);
        }
    }
}
