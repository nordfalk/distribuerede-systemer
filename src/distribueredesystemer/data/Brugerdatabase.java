/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribueredesystemer.data;

import distribueredesystemer.Diverse;
import distribueredesystemer.Serialisering;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
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
			System.out.println("Oprettet: "+db);
			// Hent data fra https://www.campusnet.dtu.dk/cnnet/participants/default.aspx?ElementID=508173&sort=fname&order=ascending&pos=0&lastPos=0&lastDisplay=listWith&cache=true&display=listWith&groupby=rights&interval=10000&search=
			String data = new String(Files.readAllBytes(Paths.get("deltagere.html")));
			Diverse.trækBrugereUdFraCampusnetHtml(data, db.brugere);
			db.gemTilFil();
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
		if (b!=null && b.adgangskode.equals(adgangskode)) return b;
		// Forkert kode - vent lidt for at imødegå bruge force angreb
		try { Thread.sleep((int)(Math.random()*100));	} catch (Exception ex) { }
		throw new IllegalArgumentException("Forkert brugernavn eller adgangskode");
	}
}
