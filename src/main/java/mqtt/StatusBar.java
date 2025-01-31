package mqtt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class StatusBar extends JPanel implements PropertyChangeListener {

    private JLabel statuslabel;

    public StatusBar() {
        statuslabel = new JLabel("Status: waiting");
        add(statuslabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("time")) {
            statuslabel.setText("Status: " + evt.getNewValue());
        }
    }
}
