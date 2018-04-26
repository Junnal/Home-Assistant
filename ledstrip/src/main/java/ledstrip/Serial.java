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
	final String MSG_START =	"SERIAL";
	final String PORT =			"COM3";
	final String BAUDRATE =		"115200";
	final String PINGPROBE =	"false";

	private Link link;
	private boolean connected = false;
	private boolean listenerSet = false;
	private String lastMessage = "";

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
	
	public void disconnect(){
		try {
			link.removeCustomListener(this);
			link.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void processMessage(String message){
		switch(message.charAt(0)){
		case '1':
			Mediator.getMqtt().sendMessage("houlouhome/mqttstrip/getpower", "ON");
			break;
		case 'B':
			Mediator.getMqtt().sendMessage("houlouhome/mqttstrip/getbrightness", message.substring(message.indexOf('_') +1));
			break;
		case 'C':
			int index1 = message.indexOf('_');
			int index2 = message.indexOf('_', index1 + 1);
			int index3 = message.indexOf('_', index2 + 1);
			
			int r = Integer.parseInt(message.substring(index1 + 1, index2));
			int g = Integer.parseInt(message.substring(index2 + 1, index3));
			int b = Integer.parseInt(message.substring(index3 + 1));
			
			Mediator.getMqtt().sendMessage("houlouhome/mqttstrip/getcolor", r + "," + g + "," +b);
			break;
		case '0':
		default:
			Mediator.getMqtt().sendMessage("houlouhome/mqttstrip/getpower", "OFF");
			break;
		}
	}
	
	public void sendMessage(String message){
		lastMessage = message;
		
		try {
			link.sendCustomMessage(MSG_START + message);
			System.out.println("Serial message sent:\n\t" + message + '\n');

		} catch (IOException e) {
			System.out.println("can't send serial message");
			e.printStackTrace();
		}
	}

	@Override
	public void customEventReceived(CustomEvent event) {
		String messageString = event.getMessage();

		if (messageString.startsWith(MSG_START)) {
			messageString = messageString.substring(MSG_START.length());
			System.out.println("Serial message received:\n\t" + messageString  + '\n');
			
			if(messageString.equals("resent")) sendMessage(lastMessage);
			else processMessage(messageString);
		}
	}

}
