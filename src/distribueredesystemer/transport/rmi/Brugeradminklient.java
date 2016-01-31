package distribueredesystemer.transport.rmi;
import distribueredesystemer.data.Bruger;
import java.rmi.Naming;
import java.util.ArrayList;

public class Brugeradminklient
{
	public static void main(String[] arg) throws Exception
	{
		//  Brugeradmin k =(Brugeradmin) Naming.lookup("rmi://javabog.dk:20099/kontotjeneste");
		Brugeradmin ba =(Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");
    Bruger b = ba.hentBruger("jacno", "kode20gdze");

    b = ba.ændrAdgangskode("jacno", "kode20gdze", "xxx");
		ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");
	}
}