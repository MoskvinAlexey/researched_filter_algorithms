package GUIModule;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GUI extends JFrame {

    public GUI(){
        super("ResearchAlgorithm");

        FormLayout layout = new FormLayout(
                "right:pref,40dlu, 170dlu, 70dlu,  pref",
                "5dlu,p, 3dlu, p, 10dlu, 30dlu, p, 3dlu, p, 10dlu, 3dlu, p, 3dlu, p, 3dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenu helpMenu = new JMenu("Справка");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);


        builder.addSeparator("Алгоритм", cc.xyw(1,2,5));
        builder.add(new JButton("Выбрать алгоритм"), cc.xy(1,4));
        builder.add(new JLabel("Последовательный поиск"), cc.xyw(3, 4, 3));
        builder.addSeparator("Входные данные", cc.xyw(1,6,5));


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
    public static void main(String[] args){
        new GUI();
    }
}
