package brugerautorisation.transport.soap;

import brugerautorisation.Diverse;
import brugerautorisation.data.Bruger;
import brugerautorisation.data.Brugerdatabase;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.mail.MessagingException;

@WebService(endpointInterface = "brugerautorisation.transport.soap.Brugeradmin")
public class BrugeradminImpl implements Brugeradmin {
	Brugerdatabase db;

	@Override
	public Bruger hentBruger(String brugernavn, String adgangskode) {
		return db.hentBruger(brugernavn, adgangskode);
	}

	@Override
	public Bruger ændrAdgangskode(String brugernavn, String adgangskode, String nyAdgangskode) {
		Bruger b = db.hentBruger(brugernavn, adgangskode);
		b.adgangskode = nyAdgangskode;
		db.gemTilFil();
		return b;
	}

	@Override
	public void sendEmail(String brugernavn, String adgangskode, String emne, String tekst) {
		Bruger b = db.hentBruger(brugernavn, adgangskode);
		try {
			Diverse.sendMail("DIST: "+emne, tekst, b.email);
		} catch (MessagingException ex) {
			ex.printStackTrace();
			throw new RuntimeException("fejl", ex);
		}
	}

	@Override
	public void sendGlemtAdgangskodeEmail(String brugernavn, String supplerendeTekst) {
		Bruger b = db.brugernavnTilBruger.get(brugernavn);
		try {
			Diverse.sendMail("DIST: Din adgangskode ",
					"Kære "+b.fornavn+",\n\nDit brugernavn er "+b.brugernavn+" og din adgangskode er: "+b.adgangskode
					+(b.sidstAktiv>0?"":"\n\nDu skal skifte adgangskoden for at bekræfte at du følger kurset.\nSe hvordan på https://docs.google.com/document/d/1ZtbPbPrEKwSu32-SSmtcSWSQaeFid8YQI5FpI35Jkb0/edit?usp=sharing \n")
					+"\n"+supplerendeTekst,
					b.email);
		} catch (MessagingException ex) {
			ex.printStackTrace();
			throw new RuntimeException("fejl", ex);
		}
	}

	@Override
	public Object getEkstraFelt(String brugernavn, String adgangskode, String feltnavn) {
		return db.hentBruger(brugernavn, adgangskode).ekstraFelter.get(feltnavn);
	}

	@Override
	public void setEkstraFelt(String brugernavn, String adgangskode, String feltnavn, Object værdi) {
		db.hentBruger(brugernavn, adgangskode).ekstraFelter.put(feltnavn, værdi);
	}
}
