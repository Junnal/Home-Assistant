package ledstrip;

import java.util.concurrent.TimeUnit;

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

		mqtt = new Mqtt();
		mqtt.connect();
		mqtt.subscribeAll();

		Mediator.setMqtt(mqtt);

		/*
		 * Test connection and launch loop
		 */

		waitForIt(5);
		
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

}
