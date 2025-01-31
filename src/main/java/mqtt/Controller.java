package mqtt;
import com.sun.tools.javac.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private Main viewMain;

    public Controller(Main viewMain) {
        this.viewMain = viewMain;
    }

    @Override

    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("About")) {
            viewMain.about()
        } else if (e.getActionCommand().equals("Start")) {
            System.out.println("Start");
        } else if (e.getActionCommand().equals("Stop")) {
            System.out.println("Stop");
            viewMain.pauseThread(true)
        }

    }
}
