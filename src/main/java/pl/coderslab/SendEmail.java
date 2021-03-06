package pl.coderslab;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

public class SendEmail {

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

/*    public static void main(String args[]) throws AddressException, MessagingException {
        generateAndSendEmail();
        System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
    }*/


    public static void generateAndSendEmail(String email, UUID uuid) throws AddressException, MessagingException {

        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("GiveInGoodHandsApp@gmail.com"));
       /// generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        //generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("test2@crunchify.com"));
        generateMailMessage.setSubject("Witaj, dziękujęmy za zarejestrowanie się na stronie 'Oddam w dobre ręce'.");
        String emailBody = "Kliknij w poniższy link, aby uaktywnić konto: \n http://localhost:8080/activateAccount/" + uuid;
        generateMailMessage.setContent(emailBody, "text/html; charset=UTF-8");
        generateMailMessage.setHeader(emailBody, "text/html; charset=UTF-8");
        System.out.println("Mail Session has been created successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", "GiveInGoodHandsApp@gmail.com", "GiveAway123!");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
}
