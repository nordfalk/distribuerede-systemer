/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation;

import brugerautorisation.data.Bruger;
import brugerautorisation.data.Brugerdatabase;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.mail.MessagingException;

/**
 *
 * @author j
 */
public class BenytBrugerdatabase {

	public static void main(String[] args) {

		Brugerdatabase db = Brugerdatabase.getInstans();
		System.out.println("\nDer er "+db.brugere.size()+" brugere i databasen");

		Scanner scanner = new Scanner(System.in); // opret scanner-objekt

		while (true) try {
			System.out.println();
			System.out.println("1 Udskriv brugere");
			System.out.println("2 Start RMI server");
			System.out.println("3 Start SOAP server");
			System.out.println("4 Send mail til alle brugere, der ikke har ændret deres kode endnu");
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
				brugerautorisation.transport.rmi.Brugeradminserver.main(null);
			} else
			if (valg==3) {
				brugerautorisation.transport.soap.Brugeradminserver.main(null);
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
					Diverse.sendMail("DIST: Din adgangskode ",
							"Kære "+b.fornavn+",\n\nDin adgangskode er: "+b.adgangskode
							+"\n\nDu skal skifte den som en del af kurset.\nSe hvordan på https://docs.google.com/document/d/1ZtbPbPrEKwSu32-SSmtcSWSQaeFid8YQI5FpI35Jkb0/edit?usp=sharing \n"
							+"\n\n"+forklarendeTekst,
							b.email);
				}
			} else
			if (valg==9) {
				break;
			} else {
				System.out.println("Ulovligt valg");
			}
		} catch (Throwable t) { t.printStackTrace(); scanner.nextLine(); }

		//db.gemTilFil();
		System.out.println("Afslutter programmet... ");
		db.gemTilFil();
		System.exit(0);
	}

}
