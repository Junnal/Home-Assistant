package ledstrip;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Mqtt{

	int qos             = 0;
	String broker       = "tcp://localhost:1883";
	String clientId     = "ArdulinkMQTT";
	String[] subTopics	= {"houlouhome/mqttstrip/setpower"};

	MemoryPersistence persistence;
	MqttClient client;
	MqttConnectOptions connOpts;

	public Mqtt() throws MqttException {
		persistence = new MemoryPersistence();
		client = new MqttClient(broker, clientId, persistence);
		client.setCallback( new MqttCallbackImplementation() );
		connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
	}

	public void connect() throws MqttException{
		System.out.println("Connecting to MQTT broker: "+broker);
		client.connect(connOpts);
		for (String topic : subTopics) {
			client.subscribe(topic);
		}
		System.out.println("Connected");
	}

	public void disconnect() throws MqttException{
		System.out.println("Disconnecting from MQTT broker...");
		client.disconnect();
		System.out.println("Disconnected");
	}
	
	public void sendMessage(String topic, String message) throws MqttException{
		System.out.println("Publishing MQTT message: "+message);
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());

		mqttMessage.setQos(qos);

		client.publish(topic, mqttMessage);
		System.out.println("MQTT message published");
	}
	
	public void subscribe(String topic) throws MqttException{
		client.subscribe(topic);
	}


}
