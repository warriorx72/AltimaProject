package com.altima.springboot.app.models.service;

import java.io.ByteArrayOutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EnviarCorreoServiceImpl implements IEnviarCorreoService{
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Override
	public void enviarCorreoSencillo(String From, String To, String Subject, String Text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enviarCorreoArchivoAdjunto(String From, String To, String Subject, String Text, String pathFile,
			String nameFile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enviarCorreoArchivoAdjuntoConMime(String From, String To, String Subject, String Text, ByteArrayOutputStream baos, String nameFile) throws MessagingException {
		// TODO Auto-generated method stub
		/*
		 * Se construye el correo
		 */
		MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(From);
        helper.setTo(To);
        helper.setSubject(Subject);
        helper.setText(Text);
        /*
         * Se construye el archivo
         */
        /**
         * Archivo
         */
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(" ");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        DataSource pdfds = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
        messageBodyPart.setDataHandler(new DataHandler(pdfds));
        messageBodyPart.setFileName(nameFile);
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        /**
         * Se envia
         */
        javaMailSender.send(message);
	}

}
