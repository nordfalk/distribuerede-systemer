/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation;

import brugerautorisation.data.Bruger;
import brugerautorisation.data.Brugerdatabase;
import java.rmi.RemoteException;
import java.util.Scanner;
import javax.mail.MessagingException;

/**
 *
 * @author j
 */
public class BenytBrugerdatabase {

	public static void main(String[] args) throws Exception {

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
				brugerautorisation.transport.soap.Kontoserver.main(null);
			} else
			if (valg==4) {
				System.out.println("Følgende brugere mangler at skifte deres kode: ");
				for (Bruger b : db.brugere) {
					if (b.sidstAktiv > 0) continue;
					System.out.println(Diverse.toString(b));
				}
				System.out.println("Skriv en linje med forklarende tekst");
				String forklarendeTekst = scanner.nextLine();

				for (Bruger b : db.brugere) {
					try {
						Diverse.sendMail("DIST: Din adgangskode ",
								"Kære "+b.fornavn+",\n\nDin adgangskode er: "+b.adgangskode
								+"\n\nDu skal skifte den snarest for at bevise at du følger kurset.\nSe hvordan på https://docs.google.com/document/d/1ZtbPbPrEKwSu32-SSmtcSWSQaeFid8YQI5FpI35Jkb0/edit?usp=sharing \n"
								+"\n\n"+forklarendeTekst,
								b.email);
					} catch (MessagingException ex) {
						ex.printStackTrace();
						throw new RemoteException("fejl", ex);
					}
				}
			} else
			if (valg==9) {
				break;
			} else {
				System.out.println("Ulovligt valg");
			}
		} catch (Throwable t) { t.printStackTrace(); }

		//db.gemTilFil();
		System.out.println("Afslutter programmet... ");
		db.gemTilFil();
		System.exit(0);
	}

}
