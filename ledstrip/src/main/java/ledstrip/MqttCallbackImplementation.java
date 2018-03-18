package ledstrip;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallbackImplementation implements MqttCallback {

	public void connectionLost(Throwable throwable) {
		System.out.println("Connection to MQTT broker lost!");
		Mediator.getSerial().sendMessage("0");
		Mediator.getMqtt().connect();
		Mediator.getMqtt().subscribeAll();
	}

	public void messageArrived(String topic, MqttMessage mqttMessage){
		String message = new String(mqttMessage.getPayload());
		System.out.println("MQTT message received:\n\t"+ topic + " " + message + '\n');
		
		for(SubTopics subTopic : SubTopics.values()){
			if(topic.contains(subTopic.toString())){
				Mediator.getMqtt().processMessage(subTopic, message);
			}
		}
	}

	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		// not used in this example
	}
}
