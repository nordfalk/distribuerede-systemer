package brugerautorisation.transport.rmi;
import brugerautorisation.data.Bruger;
import java.rmi.Naming;
import java.util.ArrayList;

public class Brugeradminklient
{
	public static void main(String[] arg) throws Exception
	{
//		Brugeradmin ba =(Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");
		Brugeradmin ba =(Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
    //ba.sendGlemtAdgangskodeEmail("jacno", "Dette er en test, husk at skifte kode");
    //ba.ændrAdgangskode("jacno", "kodeplmew", "xxx");
    Bruger b = ba.hentBruger("jacno", "xxx");
		System.out.println("Fik bruger = "+b);
		/*
    Bruger b = ba.hentBruger("jacno", "xxx");
		ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");
		*/
	}
}