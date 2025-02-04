package mqtt;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewPanel extends JPanel implements PropertyChangeListener {

    private JTextArea textArea;
    private JLabel label;

    public ViewPanel() {
        setLayout(new GridLayout(1, 2));
        textArea = new JTextArea();
        label = new JLabel();
        add(textArea);
        add(label);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        textArea.append(evt.getNewValue().toString() + "\n");
        label.setOpaque(true);
        label.setBackground(new Color(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)));
    }
}

