package brugerautorisation.transport.rmi;

import brugerautorisation.Diverse;
import brugerautorisation.data.Bruger;
import java.rmi.Naming;

public class Brugeradminklient {
	public static void main(String[] arg) throws Exception {
//		Brugeradmin ba =(Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");
		Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

    //ba.sendGlemtAdgangskodeEmail("jacno", "Dette er en test, husk at skifte kode");
		//ba.ændrAdgangskode("jacno", "kodenj4gvs", "xxx");
		Bruger b = ba.hentBruger("jacno", "xxx");
		System.out.println("Fik bruger = " + b);
		System.out.println("Data: " + Diverse.toString(b));
		// ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");
		ba.setEkstraFelt("jacno", "xxx", "s123456_testfelt", "Jeg er så glad");
		Object ekstraFelt = ba.getEkstraFelt("jacno", "xxx", "s123456_testfelt");
		System.out.println("Fik ekstraFelt = " + ekstraFelt);
	}
}
