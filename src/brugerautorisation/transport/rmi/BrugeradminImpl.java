package brugerautorisation.transport.rmi;
import brugerautorisation.Diverse;
import brugerautorisation.data.Bruger;
import brugerautorisation.data.Brugerdatabase;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.mail.MessagingException;

public class BrugeradminImpl extends UnicastRemoteObject implements Brugeradmin
{
	Brugerdatabase db;

	public BrugeradminImpl() throws java.rmi.RemoteException
	{
	}

	@Override
	public Bruger hentBruger(String brugernavn, String adgangskode) throws RemoteException {
		return db.hentBruger(brugernavn, adgangskode);
	}

	@Override
	public Bruger ændrAdgangskode(String brugernavn, String adgangskode, String nyAdgangskode) throws RemoteException {
		Bruger b = db.hentBruger(brugernavn, adgangskode);
		b.adgangskode = nyAdgangskode;
		b.sidstAktiv = System.currentTimeMillis();
		db.gemTilFil();
		return b;
	}

	@Override
	public void sendEmail(String brugernavn, String adgangskode, String emne, String tekst) throws RemoteException {
		Bruger b = db.hentBruger(brugernavn, adgangskode);
		try {
			Diverse.sendMail("DIST: "+emne, tekst, b.email);
		} catch (MessagingException ex) {
			ex.printStackTrace();
			throw new RemoteException("fejl", ex);
		}
	}

	@Override
	public void sendGlemtAdgangskodeEmail(String brugernavn) throws RemoteException {
		Bruger b = db.brugernavnTilBruger.get(brugernavn);
		try {
			Diverse.sendMail("DIST: Din adgangskode ", "Din adgangskode er: "+b.adgangskode, b.email);
		} catch (MessagingException ex) {
			ex.printStackTrace();
			throw new RemoteException("fejl", ex);
		}
	}

	@Override
	public Object getEkstraFelt(String brugernavn, String adgangskode, String feltnavn) throws RemoteException {
		return db.hentBruger(brugernavn, adgangskode).ekstraFelter.get(feltnavn);
	}

	@Override
	public void setEkstraFelt(String brugernavn, String adgangskode, String feltnavn, Object værdi) throws RemoteException {
		db.hentBruger(brugernavn, adgangskode).ekstraFelter.put(feltnavn, værdi);
	}
}