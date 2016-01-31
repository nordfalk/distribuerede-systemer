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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author j
 */
public class Brugerdatabase implements Serializable {
	// Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!
	private static final long serialVersionUID = 12345; // bare et eller andet nr.
	private static Brugerdatabase instans;

	public ArrayList<Bruger> brugere = new ArrayList<>();
	public transient HashMap<String,Bruger> brugernavnTilBruger = new HashMap<>();

	public static Brugerdatabase getInstans() throws IOException
	{
		if (instans!=null) return instans;

		try {
			instans = (Brugerdatabase) Serialisering.hent("brugere.ser");
			instans.brugernavnTilBruger = new HashMap<>();
			System.out.println("Indlæste serialiseret Brugerdatabase: "+instans);
		} catch (Exception e) {
			instans = new Brugerdatabase();
			Path path = Paths.get("deltagere.html");
			Scanner scanner = new Scanner(System.in);
			if (Files.exists(path)) {
				String data = new String(Files.readAllBytes(path));
				System.out.println("Det ser ud til at du ikke har en brugerdatabase endnu.");
				System.out.println("Jeg læser nu filen "+path+" og opretter en brugerdatabase fra den");
				Diverse.parseDeltagerlisteFraCampusnetHtml(data, instans.brugere);
				instans.gemTilFil();
			} else {
				new FileNotFoundException(
						"Deltagerlisten mangler. Du kan oprette den ved at hente\n"
						+ "https://www.campusnet.dtu.dk/cnnet/participants/default.aspx?ElementID=508173&sort=fname&order=ascending&pos=0&lastPos=0&lastDisplay=listWith&cache=true&display=listWith&groupby=rights&interval=10000&search="
						+ "\nog gemme indholdet i filen "+path.toAbsolutePath()).printStackTrace();
				System.err.println("\nDer oprettes nu en enkelt bruger du kan teste med\n(tryk Ctrl-C for at annullere)");
				Bruger b = new Bruger();
				System.err.print("Brugernavn: "); b.brugernavn = scanner.nextLine();
				System.err.print("Adgangskode: "); b.adgangskode = scanner.nextLine();
				System.err.print("Fornavn: "); b.fornavn = scanner.nextLine();
				System.err.print("Email: "); b.email = scanner.nextLine();
				instans.brugere.add(b);
				System.err.println("Fortsætter, med Brugerdatabase med en enkelt bruger: "+Diverse.toString(b));
				try {	Thread.sleep(2000); } catch (InterruptedException ex) {}
			}
		}
		// Gendan de transiente felter
		for (Bruger b : instans.brugere) {
			instans.brugernavnTilBruger.put(b.brugernavn, b);
		}
		return instans;
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
			if (b.adgangskode.equals(adgangskode)) {
				b.sidstAktiv = System.currentTimeMillis();
				return b;
			}
		}
		// Forkert adgangskode - vent lidt for at imødegå bruge force angreb
		try { Thread.sleep((int)(Math.random()*1000));	} catch (Exception ex) { }
		throw new IllegalArgumentException("Forkert brugernavn eller adgangskode");
	}
}
