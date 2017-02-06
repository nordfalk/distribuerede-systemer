package brugerautorisation.transport.rmi;

import brugerautorisation.data.Diverse;
import brugerautorisation.data.Bruger;
import java.rmi.Naming;

public class Brugeradminklient {
	public static void main(String[] arg) throws Exception {
//		Brugeradmin ba =(Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");
		Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

    //ba.sendGlemtAdgangskodeEmail("s912345", "Dette er en test, husk at skifte kode");
		ba.ændrAdgangskode("s912345", "kode1xyz", "xxx");
		Bruger b = ba.hentBruger("s912345", "xxx");
		System.out.println("Fik bruger = " + b);
		System.out.println("Data: " + Diverse.toString(b));
		// ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");

		Object ekstraFelt = ba.getEkstraFelt("s912345", "xxx", "hobby");
		System.out.println("Fik ekstraFelt = " + ekstraFelt);

		ba.setEkstraFelt("s912345", "xxx", "hobby", "Tennis og programmering"); // Skriv noget andet her

		String webside = (String) ba.getEkstraFelt("s912345", "xxx", "webside");
		System.out.println("webside = " + webside);
	}
}
