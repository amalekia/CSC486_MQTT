package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public class Publisher implements AutoCloseable {

	private static final String BROKER = "tcp://test.mosquitto.org:1883";
	private static final String TOPIC = "adr-spence";
	private static final String CLIENT_ID = "csv-publisher";
	private MqttClient client;

	public Publisher() throws MqttException {
		client = new MqttClient(BROKER, CLIENT_ID);
		client.connect();
		System.out.println("Connected to BROKER: " + BROKER);
	}

	public void publishMessage(String message) throws MqttException {
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());
		mqttMessage.setQos(2); // Quality of Service
		client.publish(TOPIC, mqttMessage);
		System.out.println("Message published: " + message);
	}

	@Override
	public void close() throws MqttException {
		if (client != null && client.isConnected()) {
			client.disconnect();
			System.out.println("Disconnected from BROKER: " + BROKER);
		}
	}

	public static void main(String[] args) {
		String csvFile = "/Users/adrickmalekian/Desktop/CSC486_MQTT/src/main/java/mqtt/240729104131_data (1).csv";

		try (Publisher publisher = new Publisher()) {
			// Get the list of lines from the CSVReaderPublisher class
			List<String> csvLines = CSVReaderPublisher.readCSV(csvFile);

			System.out.println("Publishing messages from CSV...");

			// Publish each line with a 10-second delay
			for (String line : csvLines) {
				publisher.publishMessage(line);
			Thread.sleep(10000); // 10-second delay
			}

			System.out.println("Finished publishing all messages.");
		} catch (MqttException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
