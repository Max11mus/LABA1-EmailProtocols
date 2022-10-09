package ua.edu.knu.mailprotocols;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class ImapMailReceiver {

    public void getEmails() throws MessagingException, IOException {

        String imapHost = "imap.gmail.com";
        String username = "mail.lab.ua.edu.knu@gmail.com";
        String password = "ndbflzcnebaeclvc";

        System.out.println();
        System.out.println("Receive emails via IMAP Protocol");

        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", imapHost);
        props.put("mail.imap.port", "993");
        props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.imap.socketFactory.fallback","false");
        props.put("mail.imap.socketFactory.port", "993");

        Enumeration keys = props.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String value = (String)props.get(key);
            System.out.println(key + ": " + value);
        }


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Store store = null;
        Folder inbox = null;
        try {
            store = session.getStore();
            store.connect(imapHost, username, password);

            inbox = store.getDefaultFolder().getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();

            int messageCount = 1;
            for (Message message : messages) {
                System.out.println();
                System.out.println("___________________________________________________");
                System.out.println("MESSAGE: " + messageCount++);
                System.out.println("FROM: " + message.getFrom()[0].toString());
                System.out.println("SUBJECT: " + message.getSubject());
                System.out.println("SENT DATE: " + message.getSentDate().toString());

                System.out.println();
                System.out.println("___________________________________________________");
                System.out.println(getTextFromMessage(message));
                System.out.println("___________________________________________________");

            }

            System.out.println("Done");
            System.out.println();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        finally {
            if (inbox != null) {
                inbox.close();
            }
            if (store != null) {
                store.close();
            }
        }
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + html + "\n";
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

}
