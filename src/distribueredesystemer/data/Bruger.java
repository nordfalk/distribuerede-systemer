package distribueredesystemer.data;
import java.io.*;
public class Bruger implements Serializable
{
	// Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!
	private static final long serialVersionUID = 12345; // bare et eller andet nr.
  
	public String brugernavn;
	public String adgangskode;
	public String email;
	public long sidstAktiv;
  public String id;
  public String fornavn;
  public String efternavn;
  public String studeretning;


	public String toString()
	{ 
		return "Bruger: a="+brugernavn+" tmp="+adgangskode;
	}
}