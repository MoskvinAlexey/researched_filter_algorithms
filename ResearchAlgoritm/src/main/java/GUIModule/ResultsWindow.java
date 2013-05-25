package GUIModule;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ResultsWindow extends JFrame {

    public ArrayList<String> previousResult;
    public ResultsWindow(ArrayList<String> previousResult){
        super("Результаты классификации");
        this.previousResult = previousResult;
        initialize();

    }

    private void initialize() {
        JTextArea textArea = new JTextArea();
        textArea.setText("0 - mac, 1 - arp, 2 - ip\n\n");
        for(String nextResult : previousResult){
            textArea.append(nextResult + "\n");
        }
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane);
        setContentPane(mainPanel);
        setSize(350,400);
        setVisible(true);

    }
}
