package brugerautorisation.transport.rmi;
import brugerautorisation.data.Bruger;
import java.rmi.Naming;
import java.util.ArrayList;

public class Brugeradminklient
{
	public static void main(String[] arg) throws Exception
	{
		//  Brugeradmin k =(Brugeradmin) Naming.lookup("rmi://javabog.dk:20099/kontotjeneste");
		Brugeradmin ba =(Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");
    ba.sendGlemtAdgangskodeEmail("jacno");
		/*
    Bruger b = ba.hentBruger("jacno", "xxx");

    b = ba.ændrAdgangskode("jacno", "xxx", "xxx");
		ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");
				*/
	}
}