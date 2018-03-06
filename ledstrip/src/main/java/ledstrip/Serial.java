package ledstrip;

import java.io.IOException;

import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;

public class Serial {
	Link link = Links.getLink(URIs.newURI("ardulink://serial-jssc?port=COM3&baudrate=9600&pingprobe=false"));

	public void serialTest(){
		try {
			link.sendCustomMessage("1");
		} catch (IOException e) {
			System.out.println("can't send custom message");
			e.printStackTrace();
		}
	}

}
