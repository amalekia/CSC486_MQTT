package mqtt;

import java.util.LinkedList;
import java.beans.PropertyChangeSupport;

class Repository extends PropertyChangeSupport {
    // Singleton instance
    private static Repository instance;
    private LinkedList<String> csvData;

    // Private constructor to prevent instantiation
    private Repository() {
        super(instance);
        csvData = new LinkedList<>();
        System.out.println("Repository is instantiated.");
    }

    // Singleton access method
    public static synchronized Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    // Store each line from the CSV in the repository
    public void addData(String key, Object value) {
        csvData.add(key + " : " + value);
        firePropertyChange(key, null, value);
    }

    // Getter to retrieve stored data
    public LinkedList<String> getCsvData() {
        return csvData;
    }

    // Getter to retrieve a specific line by index
    public String getCsvDataLine(int index) {
        if (index >= 0 && index < csvData.size()) {
            return csvData.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }
}
