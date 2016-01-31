package brugerautorisation.transport.soap;
import brugerautorisation.data.Brugerdatabase;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebService;
@WebService
public interface Brugeradmin
{
	@WebMethod void overførsel(int kroner);
	@WebMethod int saldo();
	@WebMethod ArrayList<String> bevægelser();
}