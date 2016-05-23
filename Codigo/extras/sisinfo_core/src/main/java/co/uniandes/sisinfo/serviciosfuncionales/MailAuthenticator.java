/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author JuanCamilo
 */
public class MailAuthenticator extends Authenticator {

	private String user;
	private String pass;

	public MailAuthenticator(String nUser, String nPass) {
		user=nUser;
		pass=nPass;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user,pass );
	}
}

