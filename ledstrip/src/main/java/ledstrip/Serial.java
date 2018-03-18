package ledstrip;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.core.events.CustomEvent;
import org.ardulink.core.events.CustomListener;
import org.ardulink.util.URIs;

import jssc.SerialPortList;

public class Serial implements CustomListener{
	final String MSG_START = "SERIAL";
	final String PORT = "COM3";
	final String BAUDRATE = "9600";
	final String PINGPROBE = "false";

	private Link link;
	private boolean connected = false;
	private boolean listenerSet = false;

	public Serial() {

	}

	public void connect(){
		while(!listenerSet){
			System.out.println("Attempting Serial connection");

			String[] PortList = SerialPortList.getPortNames();
			System.out.print("Available ports: ");

			for (String port : PortList) {
				System.out.print(port + " ");
			}

			System.out.println(".");

			if(Arrays.asList(PortList).contains(PORT)){
				link = Links.getLink(URIs.newURI("ardulink://serial-jssc?port=" + PORT + "&baudrate=" + BAUDRATE + "&pingprobe=" + PINGPROBE));
				//link = Links.getLink(URIs.newURI("ardulink://serial-jssc?port=COM3&baudrate=9600&pingprobe=false"));
				connected = true;

				System.out.println("Serial connection established");
			}

			if(connected){
				try {
					link.addCustomListener(this);
					listenerSet = true;

					System.out.println("Serial listener set");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String message){
		try {
			link.sendCustomMessage(MSG_START + message);
			System.out.println("Serial message sent");

		} catch (IOException e) {
			System.out.println("can't send serial message");
			e.printStackTrace();
		}
	}

	public void serialTest(){
		try {
			link.sendCustomMessage(MSG_START + "kleine_petit_message");
			System.out.println("Serial test message sent");

		} catch (IOException e) {
			System.out.println("can't send serial test message");
			e.printStackTrace();
		}
	}

	@Override
	public void customEventReceived(CustomEvent event) {
		String messageString = event.getMessage();

		if (messageString.startsWith(MSG_START)) {
			messageString = messageString.substring(MSG_START.length());
			System.out.println("Serial message received: " + messageString);
			
			if(messageString.equals("0"))
				Mediator.getMqtt().sendMessage("houlouhome/mqttstrip/getpower", "OFF");
			else if(messageString.equals("1"))
				Mediator.getMqtt().sendMessage("houlouhome/mqttstrip/getpower", "ON");
		}
	}

}
