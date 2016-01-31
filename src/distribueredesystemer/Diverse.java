/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribueredesystemer;

import distribueredesystemer.data.Bruger;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author j
 */
public class Diverse {

	public static void trækBrugereUdFraCampusnetHtml(String data, ArrayList<Bruger> brugere) {
		//System.out.println("data="+data);
		for (String tr : data.split("<tr")) {
			if (tr.contains("context_header")) continue;
			String td[] = tr.split("<td");
			if (td.length!=6) continue; // Der er 6 kolonner i det, vi er interesserede i
			System.out.println("tr="+tr.replace('\n', ' '));
			for (String tde : td) {
				System.out.println("td="+tde.replace('\n', ' '));
			}
			System.out.flush();
			/*
			0 td= valign="top" class="context_alternating">
			1 td= height="76" valign="top" rowspan="2"><a href="/cnnet/participants/showperson.aspx?id=190186" class="link"><img src="/cnnet/UserPicture.ashx?x=56&amp;UserId=190186" style="border: 0; width: 56px" alt="" /></a></td>
			2 td=><p><a href="/cnnet/participants/showperson.aspx?id=190186" class="link">Thor Jørgensen</a> <a href="/cnnet/participants/showperson.aspx?id=190186" class="link">Mortensen</a></p></td>
			3 td=>                 </td>
			4 td=><p><a href="mailto:s140241@student.dtu.dk" class="link">s140241@student.dtu.dk</a><br /><br /></p></td>
			5 td=>STADS-tilmeldt<br /><br /><br />diploming. IT elektronik</td></tr>
			*/
			Bruger b = new Bruger();
			b.id = td[1].split("id=")[1].split("\"")[0];
			b.fornavn = td[2].split("class=\"link\">")[1].split("<")[0];
			b.efternavn = td[2].split("class=\"link\">")[2].split("<")[0];
			b.email = td[4].split("mailto:")[1].split("\"")[0];
			if (b.email.contains("@dtu.dk") && !b.email.contains("jacno@dtu.dk")) continue; // drom adm personale
			b.brugernavn = b.email.split("@")[0];
			b.studeretning = td[5].substring(1).replaceAll("<[^>]+>", "").replace("STADS-tilmeldt","").replace("diploming. ","").trim();
			b.adgangskode = "kode"+Integer.toString((int)(Math.random()*Integer.MAX_VALUE), Character.MAX_RADIX);

			//udskriv(b, System.out);
			System.out.println("Oprettet:" + udskriv(b, new StringBuilder()));
			brugere.add(b);
		}
	}


	public static Appendable udskriv(Object obj, Appendable out) {
		Class k = obj.getClass();
		//out.append(k.getSimpleName()).append(':');
		for (Field felt : k.getFields()) try {
			Object værdi = felt.get(obj);
			out.append(' ').append(felt.getName()).append('=').append(String.valueOf(værdi));
		} catch (Exception e) { e.printStackTrace(); }
		return out;
	}

	public static void sendMail(String emne, String tekst, String modtagere) throws MessagingException {
		// Husk først at sænke sikkerheden på https://www.google.com/settings/security/lesssecureapps
		final String afsender = "android.ihk@gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");


		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						try {
							String adgangskode = new String(Files.readAllBytes(Paths.get("gmail-adgangskode.txt")));
							return new PasswordAuthentication(afsender, adgangskode);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						return null;
					}
				});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(afsender));
		message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(modtagere));
		message.setRecipients(Message.RecipientType.BCC,	InternetAddress.parse("jacob.nordfalk@gmail.com"));
		message.setSubject(emne);
		message.setText(tekst);

		Transport.send(message);
	}
}
