package kapitel_19_soap;

import java.util.ArrayList;
import javax.jws.WebService;

@SuppressWarnings("NonAsciiCharacters")
@WebService(endpointInterface = "kapitel_19_soap.KontoI")
public class KontoImpl { // valgfrit men ikke nødvendigt: implements KontoI
	private int saldo = 100; // man starter med 100 kroner
	private ArrayList<String> bevægelser = new ArrayList<>();

	public void overførsel(int kroner)
	{
		saldo = saldo + kroner;
		String s = "Overførsel på "+kroner+" kr. Ny saldo er "+saldo+" kr.";
		bevægelser.add(s);
		System.out.println(s);
	}

	public int saldo()
	{
		System.out.println("Der spørges om saldoen. Den er "+saldo+" kr.");
		return saldo;
	}

	public ArrayList<String> bevægelser()
	{
		System.out.println("Der spørges på alle bevægelser.");
		return bevægelser;
	}
}
