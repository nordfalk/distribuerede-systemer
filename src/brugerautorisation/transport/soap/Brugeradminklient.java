package brugerautorisation.transport.soap;

import brugerautorisation.data.Diverse;
import brugerautorisation.data.Bruger;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author j
 */
public class Brugeradminklient {
	public static void main(String[] args) throws MalformedURLException {
//		URL url = new URL("http://localhost:9901/brugeradmin?wsdl");
		URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
		QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
		Service service = Service.create(url, qname);
		Brugeradmin ba = service.getPort(Brugeradmin.class);

    //ba.sendGlemtAdgangskodeEmail("s123456", "Dette er en test, husk at skifte kode");
		//ba.ændrAdgangskode("s123456", "kode1xyz", "kode1xyz");
		Bruger b = ba.hentBruger("s123456", "kode1xyz");
		System.out.println("Fik bruger = " + b);
		System.out.println("Data: " + Diverse.toString(b));
		// ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");

		Object ekstraFelt = ba.getEkstraFelt("s123456", "kode1xyz", "hobby");
		System.out.println("Brugerens hobby er: " + ekstraFelt);

	}
}
