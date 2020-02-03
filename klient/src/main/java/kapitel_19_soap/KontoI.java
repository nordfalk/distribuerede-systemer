package kapitel_19_soap;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebService;

@SuppressWarnings("NonAsciiCharacters")
@WebService
public interface KontoI
{
	@WebMethod void overførsel(int kroner);
	@WebMethod int saldo();
	@WebMethod ArrayList<String> bevægelser();
}