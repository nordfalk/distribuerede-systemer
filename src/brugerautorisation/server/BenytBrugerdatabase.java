/*
Idriftsættelse
cd /home/j/DistribueredeSystemer/Brugerautorisation
ant -q; rsync -a dist/* Deltagerliste.html  gmail-adgangskode.txt  javabog.dk:Brugerautorisation/

ssh javabog.dk
cd Brugerautorisation
screen -S dist java -jar "Brugerautorisation.jar"


screen -d -r

 */
package brugerautorisation.server;

import brugerautorisation.data.Diverse;
import brugerautorisation.data.Bruger;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Hovedprogrammet på serveren
 * @author j
 */
public class BenytBrugerdatabase {

	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in); // opret scanner-objekt

		System.out.println("**********************************************");
		System.out.println("**                                          **");
		System.out.println("**  SERVER for brugerautorisationsmodulet   **");
		System.out.println("**                                          **");
		System.out.println("**********************************************");
		System.out.println();
		System.out.println("Du er ved at starte serveren for brugerautorisationsmodulet");
		System.out.println();
		System.out.print("Er det det du ønsker? (tryk 'n' for at afbryde)");
    if (scanner.nextLine().toLowerCase().startsWith("n")) {
  		System.out.println("OK - afslutter programmet");
      System.exit(0);
    }

		Brugerdatabase db = Brugerdatabase.getInstans();
		System.out.println("\nDer er "+db.brugere.size()+" brugere i databasen");

		brugerautorisation.transport.rmi.Brugeradminserver.main(null);
		brugerautorisation.transport.soap.Brugeradminserver.main(null);

		while (true) try {
			System.out.println();
			System.out.println("1 Udskriv brugere");
			System.out.println("2 Generer kommasepareret fil med brugere");
			System.out.println("3 Lav script der opretter alle brugerne som Linux-brugere");
			System.out.println("4 Send mail til alle brugere, der ikke har ændret deres kode endnu");
			System.out.println("5 Tilføj bruger");
			System.out.println("6 Slet bruger");
			System.out.println("9 Gem databasen og stop programmet");
			System.out.print("Skriv valg: ");
			int valg = scanner.nextInt();
			scanner.nextLine();
			if (valg==1) {
				for (Bruger b : db.brugere) {
					System.out.println(Diverse.toString(b));
				}
			} else
			if (valg==2) {
				for (Bruger b : db.brugere) {
					System.out.println(Diverse.tilCsvLinje(b));
				}
			} else
			if (valg==3) {
				for (Bruger b : db.brugere) {
          if ("s123456 jacno stbn".contains(b.brugernavn)) continue;
					System.out.println("sudo useradd -m -s /bin/bash -p `mkpasswd '"+b.adgangskode+"'` "+b.brugernavn+"; sudo adduser "+b.brugernavn+" sudo");
				}
			} else
			if (valg==4) {
				ArrayList<Bruger> mglBru = new ArrayList<>();
				for (Bruger b : db.brugere) {
					if (b.sidstAktiv > 0) continue;
					mglBru.add(b);
				}
				System.out.println("Der er "+mglBru.size()+" brugere, der mangler at skifte deres kode.");
				System.out.println("Det er: "+mglBru);
				System.out.println("Skriv en linje med forklarende tekst");
				String forklarendeTekst = scanner.nextLine();
				System.out.println("Er du SIKKER på at du vil sende "+forklarendeTekst+" til "+mglBru.size()+" brugere?");
				System.out.print("Skriv JA: ");
				String accept = scanner.nextLine().trim();
				if (!accept.equals("JA")) {
					System.out.println("Afbrudt med "+accept);
					continue;
				}

				for (Bruger b : mglBru) {
					SendMail.sendMail("DIST: Din adgangskode skal skiftes",
							"Kære "+b.fornavn+"\n\nDu skal skifte adgangskoden som en del af kurset i Distribuerede Systemer."
							+"\n\nDit brugernavn er "+b.brugernavn+" og din midlertidige adgangskode er: "+b.adgangskode
							+"\n\nSe hvordan du skifter koden på https://goo.gl/26pBG9 \n"
							+"\n\n"+forklarendeTekst
							+"\n\nBesked sendt p.v.a. underviseren - Jacob Nordfalk (jacno@dtu.dk)",
							b.email);
					Thread.sleep(1000);
				}
			} else
			if (valg==5) {
        Bruger b = new Bruger();
        java.util.Scanner tastatur = new java.util.Scanner(System.in);  // forbered
        b.campusnetId = "ukendt";
        b.ekstraFelter.put("webside", "");
        System.out.print("Fornavn: ");
        b.fornavn = tastatur.nextLine();
        System.out.print("Efternavn: ");
        b.efternavn = tastatur.nextLine();
        System.out.print("Email: ");
        b.email = tastatur.nextLine();
        System.out.print("Brugernavn: ");
        b.brugernavn = tastatur.nextLine();
        System.out.print("Studieretning: ");
        b.studeretning = tastatur.nextLine();
        b.adgangskode = "kode"+Integer.toString((int)(Math.random()*Integer.MAX_VALUE), Character.MAX_RADIX);
        db.brugere.add(b);

        db.brugernavnTilBruger.put(b.brugernavn, b);
        System.out.println("Bruger tilføjet: "+Diverse.toString(b));
			} else
			if (valg==6) {
        java.util.Scanner tastatur = new java.util.Scanner(System.in);  // forbered
        System.out.print("Brugernavn der skal slettes: ");
        String brugernavn = tastatur.nextLine();
        Bruger b = db.brugernavnTilBruger.get(brugernavn);
        db.brugere.remove(b);
        System.out.println("Bruger fjernet: "+Diverse.toString(b));
			} else
			if (valg==9) {
				break;
			} else {
				System.out.println("Ulovligt valg");
			}
		} catch (Throwable t) { t.printStackTrace(); scanner.nextLine(); }

		//db.gemTilFil();
		System.out.println("Afslutter programmet... ");
		db.gemTilFil(true);
		System.exit(0);
	}

}
