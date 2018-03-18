package ledstrip;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallbackImplementation implements MqttCallback {

	public void connectionLost(Throwable throwable) {
		System.out.println("Connection to MQTT broker lost!");
		Mediator.getMqtt().connect();
	}

	public void messageArrived(String s, MqttMessage mqttMessage){
		String message = new String(mqttMessage.getPayload());
		System.out.println("Mqtt message received:\n\t"+ message);
		
		if(message.equals("ON"))
			Mediator.getSerial().sendMessage("1");
		else if(message.equals("OFF"))
			Mediator.getSerial().sendMessage("0");
	}

	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		// not used in this example
	}
}
