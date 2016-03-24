package brugerautorisation.transport.rest;

import brugerautorisation.server.Diverse;
import brugerautorisation.data.Bruger;
import brugerautorisation.server.Brugerdatabase;
import brugerautorisation.server.SendMail;

public class BrugeradminImpl {
	Brugerdatabase db;

	public Bruger hentBruger(String brugernavn, String adgangskode) {
		return db.hentBruger(brugernavn, adgangskode);
	}

	public Bruger ændrAdgangskode(String brugernavn, String adgangskode, String nyAdgangskode) {
		Bruger b = db.hentBruger(brugernavn, adgangskode);
		b.adgangskode = nyAdgangskode;
		db.gemTilFil(false);
		return b;
	}

	public void sendEmail(String brugernavn, String adgangskode, String emne, String tekst) {
		Bruger b = db.hentBruger(brugernavn, adgangskode);
		try {
			SendMail.sendMail("DIST: "+emne, tekst, b.email);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("fejl", ex);
		}
	}

	public void sendGlemtAdgangskodeEmail(String brugernavn, String supplerendeTekst) {
		Bruger b = db.brugernavnTilBruger.get(brugernavn);
		try {
			SendMail.sendMail("DIST: Din adgangskode ",
					"Kære "+b.fornavn+",\n\nDit brugernavn er "+b.brugernavn+" og din adgangskode er: "+b.adgangskode
					+(b.sidstAktiv>0?"":"\n\nDu skal skifte adgangskoden for at bekræfte at du følger kurset.\nSe hvordan på https://docs.google.com/document/d/1ZtbPbPrEKwSu32-SSmtcSWSQaeFid8YQI5FpI35Jkb0/edit?usp=sharing \n")
					+"\n"+supplerendeTekst,
					b.email);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("fejl", ex);
		}
	}

	public Object getEkstraFelt(String brugernavn, String adgangskode, String feltnavn) {
		return db.hentBruger(brugernavn, adgangskode).ekstraFelter.get(feltnavn);
	}

	public void setEkstraFelt(String brugernavn, String adgangskode, String feltnavn, Object værdi) {
		db.hentBruger(brugernavn, adgangskode).ekstraFelter.put(feltnavn, værdi);
		db.gemTilFil(false);
	}
}
