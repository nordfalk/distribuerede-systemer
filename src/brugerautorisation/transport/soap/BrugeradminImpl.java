package brugerautorisation.transport.soap;

import brugerautorisation.data.Brugerdatabase;
import java.util.ArrayList;
import javax.jws.WebService;

@WebService(endpointInterface = "brugerautorisation.transport.soap.Brugeradmin")
public class BrugeradminImpl implements Brugeradmin {
	private int saldo = 100; // man starter med 100 kroner
	private ArrayList<String> bevægelser = new ArrayList<>();
	Brugerdatabase db;

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
