package mqtt;

import javax.swing.*;
import java.awt.*;


/**
 * Main class is a JFrame that contains a JMenuBar, a ViewPanel and a StatusBar.
 * It also contains an Engine that is a Runnable.
 *
 * @author javiergs
 * @version 1.0
 */
public class Main extends JFrame {

    private Engine engine;

    private JMenuBar createMenuBar() {
        // controller
        Controller controller = new Controller(this);
        // construct
        JMenuBar menuBar = new JMenuBar();
        JMenu connectMenu = new JMenu("Connection");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        JMenuItem startMenuItem = new JMenuItem("Start");
        JMenuItem stopMenuItem = new JMenuItem("Stop");
        connectMenu.add(startMenuItem);
        connectMenu.add(stopMenuItem);
        helpMenu.add(aboutMenuItem);
        startMenuItem.addActionListener(controller);
        stopMenuItem.addActionListener(controller);
        aboutMenuItem.addActionListener(controller);
        menuBar.add(connectMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    public Main() {
        // model
        engine = new Engine();
        Thread modelThread = new Thread(engine);
        modelThread.start();
        // construct
        setJMenuBar(createMenuBar());
        ViewPanel centralPanel = new ViewPanel();
        StatusBar viewStatusBar = new StatusBar();
        setLayout(new BorderLayout());
        add(centralPanel, BorderLayout.CENTER);
        add(viewStatusBar, BorderLayout.SOUTH);
        Blackboard.getInstance().addPropertyChangeListener(centralPanel);
        Blackboard.getInstance().addPropertyChangeListener(viewStatusBar);
    }

    public void about() {
        // view
        JOptionPane.showMessageDialog(this, "About Skeleton");
    }

    public void pauseThread(boolean b) {
        // controller
        engine.pause(b);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setSize(800, 600);
        main.setLocationRelativeTo(null);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
        main.setTitle("Skeleton");
    }

}
