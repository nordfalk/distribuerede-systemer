/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distribueredesystemer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DistribueredeSystemer {

	public static void main(String[] args) throws MessagingException {

		Diverse.sendMail("Testing Subject", "Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");

		System.out.println("Done");
	}

}
