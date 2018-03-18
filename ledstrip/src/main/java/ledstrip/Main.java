package ledstrip;

import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

	public static void main(String[] args) {
		/*
		 * Serial Initialization
		 */
		
		Serial serial = new Serial();
		serial.connect();
		Mediator.setSerial(serial);

		/*
		 * Mqtt Initialization
		 */
		
		Mqtt mqtt = null;

		try {
			mqtt = new Mqtt();
			mqtt.connect();
			mqtt.subscribeAll();
		} catch (MqttException me) {
			printException(me);
		}

		Mediator.setMqtt(mqtt);

		/*
		 * Test connection and launch loop
		 */

		Mediator.getSerial().serialTest();

		loop();

		System.exit(0);
	}

	private static void loop(){
		while(!Mediator.stopped()){

			waitForIt(5);

		}
	}

	private static void waitForIt(int time){
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void printException(MqttException me){
		System.out.println("reason "+me.getReasonCode());
		System.out.println("msg "+me.getMessage());
		System.out.println("loc "+me.getLocalizedMessage());
		System.out.println("cause "+me.getCause());
		System.out.println("excep "+me);
		me.printStackTrace();
	}

}
