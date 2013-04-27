package GUIModule;


import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI extends JFrame {

    private final int MAX_RUN_NUMBER = 20;


    public GUI(){
        super("Исследование алгоритмов классификации пакетного трафика");
        initialize();

    }

    private void initialize(){
        FormLayout layout = new FormLayout(
                "left:pref,10dlu, 80dlu, 10dlu, 60dlu, 200dlu",
                "5dlu,p, 3dlu, p, 10dlu, 30dlu, 30dlu, 3dlu, p, 10dlu, p, 10dlu, p, 10dlu, p, 10dlu,p,110dlu");

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
        ActionStartButton startButtonAction = new ActionStartButton(algorithmLabel,trafficField,ruleField,
                                                                    table, this);
        startButton.addActionListener(startButtonAction);
        startButton.setPreferredSize(new Dimension(200,30));

        builder.add(startButton, cc.xyw(1,13,3,CellConstraints.CENTER,CellConstraints.BOTTOM));

        JButton fullReportButton = new JButton("Подробный отчет");
        fullReportButton.addActionListener(new ActionFullReportButton(table, startButtonAction.getPreviousStart()));
        fullReportButton.setPreferredSize(new Dimension(200, 30));

        builder.add(fullReportButton, cc.xyw(1, 15, 3, CellConstraints.CENTER, CellConstraints.BOTTOM));

        JButton compareFullReportButton = new JButton("Сравнить результаты");
        compareFullReportButton.addActionListener(new ActionCompareFullReportButton(table, startButtonAction.getPreviousStart()));
        compareFullReportButton.setPreferredSize(new Dimension(200, 30));

        builder.add(compareFullReportButton, cc.xyw(1,17,3,CellConstraints.CENTER,CellConstraints.BOTTOM));


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(menuBar, BorderLayout.NORTH);
        mainPanel.add(builder.getPanel(),BorderLayout.SOUTH);
        setContentPane(mainPanel);
        pack();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
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
                return false;
            }
        };
        model.setColumnIdentifiers(columnNames);
        JTable table = new JTable(model);
        return table;
    }

    public static void main(String[] args){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {

        }
        new GUI();
    }
}
