//package skrald;
//
//import brugerautorisation.server.Brugerdatabase;
//import static spark.Spark.*;
//
//public class BrugeradminserverSparkRest
//{
//	public static void main(String[] arg)
//	{
//		Brugerdatabase db = Brugerdatabase.getInstans();
//		System.out.println("Publicerer Brugeradmin over REST Spark");
//
//    get("/", (req, res) -> "Velkommen.\n\nBesøg <a href='hjælp'>hjælp</a> for mere info.");
//    get("/hjælp", (req, res) -> "Hurra, det virker :-)");
//
//	}
//}
