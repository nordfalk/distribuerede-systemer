package distribueredesystemer;
import distribueredesystemer.data.Bruger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
public class HentOgGemData
{
  public static ArrayList<Bruger> brugere;
	public static void main(String[] arg) throws Exception
	{
		try {
			brugere = (ArrayList<Bruger>) Serialisering.hent("brugere.ser");
			System.out.println("Læst: "+brugere);
		} catch (Exception e) {
			brugere = new ArrayList<Bruger>();
			System.out.println("Oprettet: "+brugere);
			// Hentes fra https://www.campusnet.dtu.dk/cnnet/participants/default.aspx?ElementID=508173&sort=fname&order=ascending&pos=0&lastPos=0&lastDisplay=listWith&cache=true&display=listWith&groupby=rights&interval=10000&search=
			String data = new String(Files.readAllBytes(Paths.get("deltagere.html")));
			Diverse.trækBrugereUdFraCampusnetHtml(data, brugere);
		}

		/*
		Bruger b = new Bruger();
		b.brugernavn   = "bru"+brugere.size();
		b.adgangskode = "kode"+Integer.toString((int)(Math.random()*Integer.MAX_VALUE), Character.MAX_RADIX);
		brugere.add(b);
				*/

		for (Bruger b : brugere) {
			System.out.println(Diverse.udskriv(b, new StringBuilder()));
		}

		System.out.println("Gemt: "+brugere.size()+" brugere");
		Serialisering.gem(brugere,"brugere.ser");
	}
}