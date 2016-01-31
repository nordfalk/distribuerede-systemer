/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation;

import brugerautorisation.data.Bruger;
import brugerautorisation.data.Brugerdatabase;
import java.util.Scanner;

/**
 *
 * @author j
 */
public class BenytBrugerdatabase {

	public static void main(String[] args) throws Exception {

		Brugerdatabase db = Brugerdatabase.indlæsBrugerdatabase();
		System.out.println("\nDer er "+db.brugere.size()+" brugere i databasen");

		Scanner scanner = new Scanner(System.in); // opret scanner-objekt

		while (true) try {
			System.out.println();
			System.out.println("1 Udskriv brugere");
			System.out.println("2 Start RMI server");
			System.out.println("3 Start SOAP server");
			System.out.println("9 Gem databasen og stop programmet");
			int valg = scanner.nextInt();
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
