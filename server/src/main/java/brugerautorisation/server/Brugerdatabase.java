/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation.server;

import brugerautorisation.data.Diverse;
import brugerautorisation.data.Bruger;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
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
	private static final String SERIALISERET_FIL = "brugere.ser";
	private static final Path SIKKERHEDSKOPI = Paths.get("sikkerhedskopi");
	private static long filSidstGemt;

	public ArrayList<Bruger> brugere = new ArrayList<>();
	public transient HashMap<String,Bruger> brugernavnTilBruger = new HashMap<>();

	public static Brugerdatabase getInstans()
	{
		if (instans!=null) return instans;

		try {
			instans = (Brugerdatabase) Serialisering.hent(SERIALISERET_FIL);
			instans.brugernavnTilBruger = new HashMap<>();
			System.out.println("Indlæste serialiseret Brugerdatabase: "+instans);
		} catch (Exception e) {
			instans = new Brugerdatabase();
			Path path = Paths.get("Deltagerliste.html");
			Scanner scanner = new Scanner(System.in);
			try {
				String data = new String(Files.readAllBytes(path));
				System.out.println("Det ser ud til at du ikke har en brugerdatabase endnu.");
				System.out.println("Jeg læser nu filen "+path+" og opretter en brugerdatabase fra den\n");
				indlæsDeltagerlisteFraCampusnetHtml(data, instans.brugere);
        Bruger b = new Bruger();
        b.campusnetId = "ukendt";
        b.ekstraFelter.put("webside", "http://www.diplom.dtu.dk/");
        b.fornavn = "Dennis";
        b.efternavn = "Demostudent";
        b.email = "s123456@student.dtu.dk";
        b.brugernavn = b.email.split("@")[0];
        b.studeretning = "demobruger";
        b.adgangskode = "kode1xyz";
        instans.brugere.add(b);
        System.out.println("Demobruger tilføjet: "+Diverse.toString(b));

        if (instans.brugere.size()==0) throw new IllegalStateException("Der blev ikke fundet nogen brugere i filen");
			} catch (IOException e2) {
				System.err.println("Deltagerlisten mangler vist. \n\nDu kan oprette den ved at hente\n"
						+ "https://cn.inside.dtu.dk/cnnet/participants/default.aspx?ElementID=612475&sort=fname&order=ascending&pos=0&lastPos=0&lastDisplay=listWith&cache=false&display=listWith&groupby=rights&interval=10000&search="
						+ "\nog gemme indholdet i filen "+path.toAbsolutePath());

				e2.printStackTrace();

				System.err.println("\n\nDer oprettes nu en enkelt bruger du kan teste med\n(tryk Ctrl-C for at annullere)");
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


	public void gemTilFil(boolean tvingSkrivning) {
		if (!tvingSkrivning && filSidstGemt>System.currentTimeMillis()-60000) return; // Gem højst 1 gang per minut
		// Lav en sikkerhedskopi - i fald der skal rulles tilbage eller filen blir beskadiget
		try {
			if (!Files.exists(SIKKERHEDSKOPI)) Files.createDirectories(SIKKERHEDSKOPI);
      if (Files.exists(Paths.get(SERIALISERET_FIL))) {
        Files.move(Paths.get(SERIALISERET_FIL), SIKKERHEDSKOPI.resolve(SERIALISERET_FIL+new Date()));
      }
		} catch (IOException e) { e.printStackTrace(); }
		try {
			Serialisering.gem(this, SERIALISERET_FIL);
			filSidstGemt = System.currentTimeMillis();
			System.out.println("Gemt brugerne pr "+new Date());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Bruger hentBruger(String brugernavn, String adgangskode) {
    // forsink lidt for at imødegå brute force angreb
    try { Thread.sleep((int)(Math.random()*100));	} catch (Exception ex) { }

		Bruger b = brugernavnTilBruger.get(brugernavn);
		System.out.println("hentBruger "+brugernavn+" gav "+b);
		if (b!=null && b.adgangskode.equals(adgangskode)) {
      b.sidstAktiv = System.currentTimeMillis();
      return b;
    }
    if (b!=null) {
      System.out.println("        forkert kode: '"+adgangskode+"' - korrekt kode er '"+b.adgangskode+"'");
    }
    // Forkert adgangskode - vent lidt for at imødegå brute force angreb
    try { Thread.sleep((int)(Math.random()*1000));	} catch (Exception ex) { }
    throw new IllegalArgumentException("Forkert brugernavn eller adgangskode for "+brugernavn);
	}

  public Bruger ændrAdgangskode(String brugernavn, String glAdgangskode, String nyAdgangskode) {
    // forsink lidt for at imødegå brute force angreb
    try { Thread.sleep((int)(Math.random()*100));	} catch (Exception ex) { }

    // Tjek først om brugerens adgangskode allerede ER ændret til nyAdgangskode - der er mange der kommer til at lave kaldet flere gange
		Bruger b = brugernavnTilBruger.get(brugernavn);
		System.out.println("ændrAdgangskode "+brugernavn+" fra "+glAdgangskode + " til "+nyAdgangskode+" gav b="+b);
		if (b!=null && !b.adgangskode.equals(glAdgangskode) && b.adgangskode.equals(nyAdgangskode)) {
      throw new IllegalStateException("Adgangskoden ER allerede ændret til "+nyAdgangskode+". Hvorfor vil du ændre den til det samme som den allerede er? (Vink: Kald kun ændrAdgangskode én gang :-)");
    }
    b = hentBruger(brugernavn, glAdgangskode); // Lav derefter det almindelige adgangskodetjek

    if (nyAdgangskode.isEmpty()) throw new IllegalArgumentException("Ny adgangskode må ikke være tom");
    if (nyAdgangskode.contains("\"") || nyAdgangskode.contains("'")) throw new IllegalArgumentException("Ugyldige tegn i ny adgangskode");
		b.adgangskode = nyAdgangskode;
		gemTilFil(false);
    return b;
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
			b.campusnetId = td[1].split("id=")[1].split("\"")[0].split("&")[0];
			b.ekstraFelter.put("webside", td[1].split("href=\"")[1].split("\"")[0]);
			b.fornavn = td[2].split("class=\"link\">")[1].split("<")[0];
			b.efternavn = td[2].split("class=\"link\">")[2].split("<")[0];
			b.email = td[4].split("mailto:")[1].split("\"")[0];
			if (b.email.contains("flhan@dtu.dk") || b.email.contains("phso@dtu.dk")) continue; // drom adm personale
			if (b.email.contains("jrei@dtu.dk") || b.email.contains("chbu@dtu.dk")) continue;
			if (b.email.contains("manyb@dtu.dk") || b.email.contains("chbu@dtu.dk")) continue;
			if (b.email.contains("feni@dtu.dk")) continue; // drom adm personale
			b.brugernavn = b.email.split("@")[0];
			b.studeretning = td[5].substring(1).replaceAll("<[^>]+>", " ")
              .replace("STADS-tilmeldt","")
              .replace("Afdelingen for Uddannelse og Studerende", "")
              .replace("Center for Diplomingeniøruddannelse ", "")
              .replace("diploming. ","").replaceAll("[ \n]+", " ").trim();
			if (b.studeretning.isEmpty()) b.studeretning = "IT-Økonomi"; // Hvorfor ITØ'ernes er tom ved jeg ikke....
			if (b.studeretning.startsWith("Tilføjet ")) b.studeretning = b.studeretning.substring(9)+" (tilf)";
			b.adgangskode = "kode"+Integer.toString((int)(Math.random()*Integer.MAX_VALUE), Character.MAX_RADIX);

			System.out.println("Oprettet:" + Diverse.toString(b));
			brugere.add(b);
		}
	}



  public Bruger hentBrugerOffentligt(String brugernavn) {
    Bruger b = brugernavnTilBruger.get(brugernavn);
    if (b==null) {
      // Ukendt bruger - vent lidt for at imødegå brute force angreb
      try { Thread.sleep((int)(Math.random()*1000));	} catch (Exception ex) { }
      throw new IllegalArgumentException("Bruger findes ikke");
    }
    Bruger off = new Bruger();
    off.brugernavn = b.brugernavn;
    off.campusnetId = b.campusnetId;
    off.efternavn = b.efternavn;
    off.email = b.email;
    off.fornavn = b.fornavn;
    off.sidstAktiv = b.sidstAktiv;
    off.studeretning = b.studeretning;
    off.adgangskode = "<IKKE OFFENTLIG>";
    return off;
  }
}
