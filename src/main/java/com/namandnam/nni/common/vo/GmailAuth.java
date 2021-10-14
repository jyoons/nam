package com.namandnam.nni.common.vo;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class GmailAuth extends Authenticator {
	public PasswordAuthentication getPasswordAuthentication() {
		String mailID = "dhkim@reflexion.co.kr";
		String mailPWD = "wlfkf12@";
		return new PasswordAuthentication(mailID, mailPWD);
	}
}
