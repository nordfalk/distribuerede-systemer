/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation.data;

import brugerautorisation.Diverse;
import brugerautorisation.Serialisering;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

	public static Brugerdatabase getInstans()
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
			try {
				String data = new String(Files.readAllBytes(path));
				System.out.println("Det ser ud til at du ikke har en brugerdatabase endnu.");
				System.out.println("Jeg læser nu filen "+path+" og opretter en brugerdatabase fra den\n");
				indlæsDeltagerlisteFraCampusnetHtml(data, instans.brugere);
			} catch (IOException e2) {
				e2.printStackTrace();
				System.err.println("Deltagerlisten mangler vist. Du kan oprette den ved at hente\n"
						+ "https://www.campusnet.dtu.dk/cnnet/participants/default.aspx?ElementID=508173&sort=fname&order=ascending&pos=0&lastPos=0&lastDisplay=listWith&cache=true&display=listWith&groupby=rights&interval=10000&search="
						+ "\nog gemme indholdet i filen "+path.toAbsolutePath());
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



	public static void indlæsDeltagerlisteFraCampusnetHtml(String data, ArrayList<Bruger> brugere) {
		//System.out.println("data="+data);
		for (String tr : data.split("<tr")) {
			if (tr.contains("context_header")) continue;
			String td[] = tr.split("<td");
			if (td.length!=6) continue; // Der er 6 kolonner i det, vi er interesserede i
			System.out.println("tr="+tr.replace('\n', ' '));
			for (String tde : td) {
				System.out.println("td="+tde.replace('\n', ' '));
			}
			System.out.flush();
			/*
			0 td= valign="top" class="context_alternating">
			1 td= height="76" valign="top" rowspan="2"><a href="/cnnet/participants/showperson.aspx?campusnetId=190186" class="link"><img src="/cnnet/UserPicture.ashx?x=56&amp;UserId=190186" style="border: 0; width: 56px" alt="" /></a></td>
			2 td=><p><a href="/cnnet/participants/showperson.aspx?campusnetId=190186" class="link">Thor Jørgensen</a> <a href="/cnnet/participants/showperson.aspx?campusnetId=190186" class="link">Mortensen</a></p></td>
			3 td=>                 </td>
			4 td=><p><a href="mailto:s140241@student.dtu.dk" class="link">s140241@student.dtu.dk</a><br /><br /></p></td>
			5 td=>STADS-tilmeldt<br /><br /><br />diploming. IT elektronik</td></tr>
			*/
			Bruger b = new Bruger();
			b.campusnetId = td[1].split("id=")[1].split("\"")[0];
			b.fornavn = td[2].split("class=\"link\">")[1].split("<")[0];
			b.efternavn = td[2].split("class=\"link\">")[2].split("<")[0];
			b.email = td[4].split("mailto:")[1].split("\"")[0];
			if (b.email.contains("@dtu.dk") && !b.email.contains("jacno@dtu.dk")) continue; // drom adm personale
			b.brugernavn = b.email.split("@")[0];
			b.studeretning = td[5].substring(1).replaceAll("<[^>]+>", " ").replace("STADS-tilmeldt","").replace("diploming. ","").replaceAll("[ \n]+", " ").trim();
			if (b.studeretning.isEmpty()) b.studeretning = "IT-Økonomi"; // Hvorfor ITØ'ernes er tom ved jeg ikke....
			b.adgangskode = "kode"+Integer.toString((int)(Math.random()*Integer.MAX_VALUE), Character.MAX_RADIX);

			System.out.println("Oprettet:" + Diverse.toString(b));
			brugere.add(b);
		}
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
