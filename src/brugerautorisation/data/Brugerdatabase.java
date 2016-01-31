/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation.data;

import brugerautorisation.Diverse;
import brugerautorisation.Serialisering;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author j
 */
public class Brugerdatabase implements Serializable {
	// Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!
	private static final long serialVersionUID = 12345; // bare et eller andet nr.

	public ArrayList<Bruger> brugere = new ArrayList<>();
	public transient HashMap<String,Bruger> brugernavnTilBruger = new HashMap<>();

	public static Brugerdatabase indlæsBrugerdatabase() throws IOException
	{
		Brugerdatabase db;
		try {
			db = (Brugerdatabase) Serialisering.hent("brugere.ser");
			db.brugernavnTilBruger = new HashMap<>();
			System.out.println("Læst: "+db);
		} catch (Exception e) {
			db = new Brugerdatabase();
			Path path = Paths.get("deltagere.html");
			if (Files.exists(path)) {
				String data = new String(Files.readAllBytes(path));
				Diverse.parseDeltagerlisteFraCampusnetHtml(data, db.brugere);
				db.gemTilFil();
				System.out.println("Oprettet: "+db);
			} else {
				new FileNotFoundException(
						"Deltagerlisten mangler. Du kan oprette den ved at hente\n"
						+ "https://www.campusnet.dtu.dk/cnnet/participants/default.aspx?ElementID=508173&sort=fname&order=ascending&pos=0&lastPos=0&lastDisplay=listWith&cache=true&display=listWith&groupby=rights&interval=10000&search="
						+ "\nog gemme indholdet i filen "+path.toAbsolutePath()).printStackTrace();
				Bruger b = new Bruger();
				b.brugernavn = "s123456";
				b.adgangskode = "xxx";
				db.brugere.add(b);
				System.err.println("Fortsætter, med brugeren "+Diverse.toString(b));
				try {	Thread.sleep(500); } catch (InterruptedException ex) {}
			}
		}
		// Gendan de transiente felter
		for (Bruger b : db.brugere) {
			db.brugernavnTilBruger.put(b.brugernavn, b);
		}
		return db;
	}

	public void gemTilFil() {
		try {
			Serialisering.gem(this, "brugere.ser");
			System.out.println("Gemt brugerne");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Bruger hentBruger(String brugernavn, String adgangskode) {
		Bruger b = brugernavnTilBruger.get(brugernavn);
		System.out.println("hentBruger "+brugernavn+" gav "+b);
		if (b!=null) {
			System.out.println("         kode="+adgangskode+" b.kode="+b.adgangskode);
			if (b.adgangskode.equals(adgangskode)) return b;
		}
		// Forkert adgangskode - vent lidt for at imødegå bruge force angreb
		try { Thread.sleep((int)(Math.random()*1000));	} catch (Exception ex) { }
		throw new IllegalArgumentException("Forkert brugernavn eller adgangskode");
	}
}
