package ledstrip;

import java.io.IOException;

import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.core.events.CustomEvent;
import org.ardulink.core.events.CustomListener;
import org.ardulink.util.URIs;

public class Serial implements CustomListener{
	final String MSG_START = "SERIAL";
	Link link = Links.getLink(URIs.newURI("ardulink://serial-jssc?port=COM3&baudrate=9600&pingprobe=false"));

	public void serialTest(){
		try {
			link.sendCustomMessage("1");
			
		} catch (IOException e) {
			System.out.println("can't send custom message");
			e.printStackTrace();
		}
	}

	@Override
	public void customEventReceived(CustomEvent event) {
		String messageString = event.getMessage();

        if (messageString.startsWith(MSG_START)) {
            messageString = messageString.substring(MSG_START.length());
            
        }		
	}

}
