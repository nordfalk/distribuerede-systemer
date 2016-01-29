package distribueredesystemer.rmi;
import distribueredesystemer.Diverse;
import distribueredesystemer.data.Bruger;
import distribueredesystemer.data.Brugerdatabase;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
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
			Diverse.sendMail(emne, tekst, b.email);
		} catch (MessagingException ex) {
			ex.printStackTrace();
			throw new RemoteException("fejl", ex);
		}
	}
}