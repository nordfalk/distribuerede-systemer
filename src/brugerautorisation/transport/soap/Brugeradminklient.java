package brugerautorisation.transport.soap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author j
 */
public class Brugeradminklient {
	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL("http://localhost:9901/brugeradmin?wsdl");
		QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
		Service service = Service.create(url, qname);
		Brugeradmin ba = service.getPort(Brugeradmin.class);
    
    ba.overførsel(100);
    ba.overførsel(50);
		System.out.println( "Saldo er: "+ ba.saldo() );
		ba.overførsel(-200);
		ba.overførsel(51);
		System.out.println( "Saldo er: "+ ba.saldo() );
		ArrayList<String> bevægelser = ba.bevægelser();

		System.out.println( "Bevægelser er: "+ bevægelser );
	}
}
