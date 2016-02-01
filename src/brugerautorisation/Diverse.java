/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation;

import brugerautorisation.data.Bruger;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
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

/**
	* Tager et vilkårligt objekt og laver en streng ud af dets public variabler
	* @param obj Objektet
	* @return En streng med alle dets public variabler
	*/
	public static String toString(Object obj) {
		StringBuilder sb = new StringBuilder();
		Class k = obj.getClass();
		sb.append(k.getSimpleName()).append(':');
		for (Field felt : k.getFields()) try {
			Object værdi = felt.get(obj);
			sb.append(' ').append(felt.getName()).append('=').append('"').append(String.valueOf(værdi)).append('"');
		} catch (Exception e) { e.printStackTrace(); }
		return sb.toString();
	}

	public static void sendMail(String emne, String tekst, String modtagere) throws MessagingException {
		// Husk først at sænke sikkerheden på https://www.google.com/settings/security/lesssecureapps
		final String afsender = "android.ihk@gmail.com";
		System.out.println("sendMail "+emne+ " "+modtagere);

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");


		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						Path kodefil = Paths.get("gmail-adgangskode.txt");
						try {
							String adgangskode = new String(Files.readAllBytes(kodefil));
							return new PasswordAuthentication(afsender, adgangskode);
						} catch (IOException ex) {
							System.err.println("Du kan ikke sende mails før du har konfigurerer afsender ("+afsender+") til lav sikkerhed:\nhttps://www.google.com/settings/security/lesssecureapps\nog og lagt adgangskoden i "+kodefil);
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
