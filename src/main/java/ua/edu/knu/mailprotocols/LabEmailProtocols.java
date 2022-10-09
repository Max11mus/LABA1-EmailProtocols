package ua.edu.knu.mailprotocols;

import javax.mail.MessagingException;
import java.io.IOException;

public class LabEmailProtocols {
    public static void main(String[] args) throws MessagingException, IOException {

        long startTime, elapsedTime;

        startTime = System.currentTimeMillis();
        SmtpMailSender smtpMailSender = new SmtpMailSender();
        smtpMailSender.sendEmail();
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed " + ((double) elapsedTime / 1000) + " seconds.");
        System.out.println();

        startTime = System.currentTimeMillis();
        Pop3MailReceiver pop3MailReceiver = new Pop3MailReceiver();
        pop3MailReceiver.getEmails();
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed " + ((double) elapsedTime / 1000) + " seconds.");
        System.out.println();

        startTime = System.currentTimeMillis();
        ImapMailReceiver imapMailReceiver = new ImapMailReceiver();
        imapMailReceiver.getEmails();
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed " + ((double) elapsedTime / 1000) + " seconds.");
        System.out.println();
    }

}
