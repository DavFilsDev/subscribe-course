package com.example.demo.receipt;

import com.example.demo.entity.SubscriptionEntity;
import java.io.File;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

@Component
public class ReceiptGenerator {

  @SneakyThrows
  public File generate(SubscriptionEntity subscription) {
    var user = subscription.getUser();
    var course = subscription.getCourse();

    File file = File.createTempFile("receipt-" + subscription.getId(), ".pdf");

    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);

      try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
        cs.newLineAtOffset(50, 750);
        cs.showText("Reçu d'inscription");
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 12);
        cs.newLineAtOffset(50, 700);
        cs.showText("Nom : " + user.getFirstName() + " " + user.getLastName());
        cs.newLineAtOffset(0, -20);
        cs.showText("Cours : " + course.getTitle());
        cs.newLineAtOffset(0, -20);
        cs.showText("Date d'inscription : " + subscription.getSubscribedAt());
        cs.newLineAtOffset(0, -20);
        cs.showText("Référence : " + subscription.getId());
        cs.endText();
      }

      document.save(file);
    }

    return file;
  }
}
