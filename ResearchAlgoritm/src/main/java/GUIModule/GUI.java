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

    private final int MAX_RUN_NUMBER = 25;
    JLabel algorithmLabel;
    JTextField trafficField;
    JTextField ruleField;
    String [] algorithmList = {"Последовательный поиск","Дерево решений"};

    public GUI(){
        super("Исследование алгоритмов классификации пакетного трафика");
        initialize();

    }

    private void initialize(){

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenu helpMenu = new JMenu("Справка");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        JPanel dataPane = initDataPanel();
        JPanel tablePanel = initTablePanel();

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(menuBar, BorderLayout.NORTH);
        mainPanel.add(dataPane,BorderLayout.CENTER);
        mainPanel.add(tablePanel,BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        //setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel initTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = createSummaryTable();

        TableToolBar tableToolBar = new TableToolBar(algorithmLabel,trafficField,ruleField,
                table, this);

        tablePanel.add(tableToolBar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(this.getWidth(), 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);



        return tablePanel;
    }

    private JPanel initDataPanel() {

        FormLayout layout = new FormLayout(
                "left:pref,10dlu, 80dlu, 10dlu, 60dlu, 200dlu",
                "5dlu,p, 3dlu, p, 10dlu, 30dlu, 30dlu, 3dlu, p, 10dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();


        builder.addSeparator("Алгоритм", cc.xyw(1,2,6));
        JButton button = new JButton(new AbstractAction() {
            {
                putValue(AbstractAction.NAME, "Выбрать алгоритм");
                putValue(AbstractAction.SHORT_DESCRIPTION, "Выбрать алгоритм фильтрации");
                //putValue(AbstractAction.SMALL_ICON, new IconUIResource());
            }


            public void actionPerformed(ActionEvent e) {

                Object algorithmName = JOptionPane.showInputDialog(GUI.this, "Выберите алгоритм", "Выбор алгоритма фильтрации",
                        JOptionPane.QUESTION_MESSAGE, null, algorithmList,"Titan");
                if(algorithmName!=null){
                    algorithmLabel.setText((String) algorithmName);
                }

            }
        });

        builder.add(button, cc.xy(1,4));
        algorithmLabel = new JLabel("Алгоритм не выбран");
        builder.add(algorithmLabel, cc.xyw(3, 4, 3));
        builder.addSeparator("Входные данные", cc.xyw(1, 6, 6));

        builder.add(new JLabel("Источник трафика:"), cc.xy(1,7));
        trafficField = new JTextField();
        trafficField.setPreferredSize(new Dimension(80,23));
        builder.add(trafficField, cc.xy(3, 7));
        JButton browserToTrafficFile = new JButton("Обзор");
        addActionForBrowserButton(browserToTrafficFile,trafficField);
        builder.add(browserToTrafficFile, cc.xy(5, 7));

        builder.add(new JLabel("Правила фильтрации:"), cc.xy(1,9));
        ruleField = new JTextField();
        ruleField.setPreferredSize(new Dimension(80,23));
        builder.add(ruleField, cc.xy(3,9));
        JButton browserForRuleFile = new JButton("Обзор");
        addActionForBrowserButton(browserForRuleFile,ruleField);
        builder.add(browserForRuleFile, cc.xy(5,9));

        builder.addSeparator("Последние запуски", cc.xyw(1,11,6));
        JPanel dataPanel = new JPanel();
        dataPanel.add(builder.getPanel());
        return dataPanel;
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
        //table.setPreferredSize(new Dimension(this.getWidth(),300));
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
