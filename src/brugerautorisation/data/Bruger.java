package brugerautorisation.data;
import java.io.*;
import java.util.HashMap;
public class Bruger implements Serializable
{
	// Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!
	private static final long serialVersionUID = 12345; // bare et eller andet nr.
  
  public String id; // campusnet database-ID
	public String brugernavn; // studienummer
	public String adgangskode;
	public String email;
  public String fornavn;
  public String efternavn;
  public String studeretning;
	public long sidstAktiv;
  public HashMap<String,Object> ekstraFelter = new HashMap<>();


	public String toString()
	{ 
		return "Bruger: a="+brugernavn+" tmp="+adgangskode;
	}
}