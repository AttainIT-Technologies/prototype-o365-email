
package attainittech.prototypeo365email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@SpringBootApplication
public class Application implements CommandLineRunner
{
	public void sendPlaintextEmail()
	{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(emailTo);
		msg.setFrom(emailFrom);
		msg.setSubject("plaintest email from spring boot at " + new Date().toString());
		msg.setText("Hello World\nSpring Boot Email\n" + new Date().toString());

		try
		{
			log.info("sending plaintext email");
			javaMailSender.send(msg);
			log.info("plaintext email sent");
		}
		catch(MailException e)
		{
			log.error("error sending plaintext email", e);
		}
	}

	public void sendMimeEmail()
	{
		MimeMessage msg = javaMailSender.createMimeMessage();
		try
		{
			MimeMessageHelper helper = new MimeMessageHelper(msg, true); // true = multipart message
			helper.setTo(emailTo);
			helper.setFrom(emailFrom);
			helper.setSubject("html test email from spring boot at " + new Date().toString());
			helper.setText("<h1>check attachment for the logo</h1>", true); // true = text/html
			//helper.addAttachment("logo.png", new FileSystemResource(new File("path/to/logo.png")));
			helper.addAttachment("logo.png", new ClassPathResource("logo.png"));
		}
		catch(MessagingException e)
		{
			log.error("error preparing email", e);
		}

		try
		{
			log.info("sending mime email");
			javaMailSender.send(msg);
			log.info("mime email sent");
		}
		catch(MailException e)
		{
			log.error("error sending mime email", e);
		}
	}

	@Override
	public void run(String... args) throws Exception
	{
		log.info("Starting AttainIT Technologies Email Prototype");

		sendPlaintextEmail();
		sendMimeEmail();

		log.info("Completed AttainIT Technologies Email Prototype");
	}

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${prototype.o365.email.from}")
	private String emailFrom;

	@Value("${prototype.o365.email.to}")
	private String emailTo;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Application.class);
}
