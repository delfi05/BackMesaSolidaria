package com.microservice_manager.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {
  @Autowired
  private JavaMailSender mailSender;

  //@Value("${spring.mail.username}")
  //private String fromMail;


  public void sendEmail(String to, String password) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    /** CAMBIAR DESPUES DE HACER TODAS LAS PRUEBAS */

    helper.setFrom("lorenzofdzdev@gmail.com", "RESTABLECER CONTRASEÑA");
    helper.setTo(to);

    String subject = "Restablecimiento de Contraseña para Mesa Solidaria";

    String body = "<p>Hola,</p>" +
        "<p>Recibimos una solicitud para restablecer la contraseña de tu cuenta en Mesa Solidaria.</p>" +
        "<p>Si no realizaste esta solicitud, puedes ignorar este correo electrónico.</p>" +
        "<p>Tu contraseña para Mesa Solidaria ha sido restablecida.</p>" +
        "<p><strong>Nueva Contraseña: " + password + "</strong></p>" +
        "<p>Utiliza esta contraseña para iniciar sesión en tu cuenta. Te recomendamos cambiarla inmediatamente después de iniciar sesión, desde la sección \"Cambiar Contraseña\" en tu perfil.</p>" +
        "<p>Gracias,</p>" +
        "<p>El equipo de Mesa Solidaria</p>" +
        "<p>Si tienes alguna pregunta, contáctanos en federacion.mesasolidariatandil@gmail.com</p>";

    helper.setSubject(subject);

    helper.setText(body, true);
    try {
      mailSender.send(message);
      System.out.println("Correo enviado correctamente");
    } catch (MailException ex) {
      System.err.println("Error al enviar el correo: " + ex.getMessage());
    }
  }
}