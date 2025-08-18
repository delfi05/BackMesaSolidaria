package com.microservice_voluntary.service;

import com.microservice_voluntary.dto.VoluntaryDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {
  @Autowired
  private JavaMailSender mailSender;

  @Value("${email-service.fromMail}")
  private String fromMail;


  public void sendEmail(VoluntaryDTO voluntaryDTO) throws MessagingException, UnsupportedEncodingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    /** CAMBIAR DESPUES DE HACER TODAS LAS PRUEBAS */
    // CAMBIAR EL FROM Y EL TO
    helper.setFrom(fromMail, "SE HA REGISTRADO UN NUEVO VOLUNTARIO");
    helper.setTo(fromMail);

    String subject = "NUEVO REGISTRO DE UN VOLUNTARIO";

    String body = "<p>Hola,</p>" +
        "<p>¡Tenemos un nuevo voluntario registrado en Mesa Solidaria!</p>" +
        "<p><strong>Detalles del voluntario:</strong></p>" +
        "<ul>" +
        "    <li><strong>Nombre:</strong> " + voluntaryDTO.getName() + "</li>" +
        "    <li><strong>Apellido:</strong> " + voluntaryDTO.getLastName() + "</li>" +
        "    <li><strong>Correo Electrónico:</strong> " + voluntaryDTO.getEmail() + "</li>" +
        "    <li><strong>Teléfono:</strong> " + voluntaryDTO.getPhone() + "</li>" +
        "</ul>" +
        "<p>Por favor, ponte en contacto con el voluntario lo antes posible para darle la bienvenida y coordinar su participación.</p>" +
        "<p>Gracias</p>";

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