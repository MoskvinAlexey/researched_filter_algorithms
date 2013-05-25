package GUIModule;


import javax.swing.*;
import java.awt.*;

public class TableToolBar extends JToolBar {

    public TableToolBar(JLabel algorithmLabel, JTextField trafficField, JTextField ruleField,
                        JTable table, GUI gui) {

        Insets margins = new Insets(0, 0, 0, 0);

        ToolBarButton startButton = new ToolBarButton("data/start.png" );
        ActionStartButton startButtonAction = new ActionStartButton(algorithmLabel,trafficField,ruleField,
                table, gui);
        startButton.addActionListener(startButtonAction);
        add(startButton);
        startButton.setToolTipText("Запустить");
        startButton.setMargin(margins);

        ToolBarButton fullReportButton = new ToolBarButton("data/report.png");
        fullReportButton.addActionListener(new ActionFullReportButton(table, startButtonAction.getPreviousStartStatistics()));
        add(fullReportButton);
        fullReportButton.setToolTipText("Подробный отчет");
        fullReportButton.setMargin(margins);

        ToolBarButton compareFullReportButton = new ToolBarButton("data/compare.png");
        compareFullReportButton.addActionListener(new ActionCompareFullReportButton(table,
                                                                                startButtonAction.getPreviousStartStatistics()));
        add(compareFullReportButton);
        compareFullReportButton.setToolTipText("Сравнить запуски");

        ToolBarButton resultsButton = new ToolBarButton("data/results.png");
        resultsButton.addActionListener(new ActionResultsReport(table,startButtonAction.getPreviousStartResult()));
        add(resultsButton);
        resultsButton.setToolTipText("Результат классификации");

        compareFullReportButton.setMargin(margins);
        setTextLabels(true);
    }


    public void setTextLabels(boolean labelsAreEnabled) {
        Component c;
        int i = 0;
        while((c = getComponentAtIndex(i++)) != null) {
            ToolBarButton button = (ToolBarButton)c;
            if (labelsAreEnabled)
                button.setText(button.getToolTipText());
            else
                button.setText(null);
        }
    }

}
