package com.example.demo.service.event;

import com.example.demo.endpoint.event.model.SendSubscriptionConfirmationRequested;
import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import com.example.demo.receipt.ReceiptGenerator;
import com.example.demo.repository.SubscriptionRepository;
import jakarta.mail.internet.InternetAddress;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SendSubscriptionConfirmationRequestedService
    implements Consumer<SendSubscriptionConfirmationRequested> {

  private static final Duration RECEIPT_LINK_DURATION = Duration.ofDays(7);

  private final SubscriptionRepository subscriptionRepository;
  private final Mailer mailer;
  private final ReceiptGenerator receiptGenerator;
  private final BucketComponent bucketComponent;

  @SneakyThrows
  @Override
  public void accept(SendSubscriptionConfirmationRequested event) {
    var subscription =
        subscriptionRepository
            .findById(event.getSubscriptionId())
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "Subscription not found: " + event.getSubscriptionId()));

    var user = subscription.getUser();
    var course = subscription.getCourse();

    var receiptFile = receiptGenerator.generate(subscription);
    var bucketKey = "receipts/" + subscription.getId() + ".pdf";
    bucketComponent.upload(receiptFile, bucketKey);
    var downloadUrl = bucketComponent.presign(bucketKey, RECEIPT_LINK_DURATION);

    subscription.setReceiptKey(bucketKey);
    subscriptionRepository.save(subscription);

    var email =
        new Email(
            new InternetAddress(user.getEmail()),
            List.of(),
            List.of(),
            "Confirmation d'inscription - " + course.getTitle(),
            """
            <p>Bonjour %s,</p>
            <p>Vous êtes bien inscrit(e) au cours <strong>%s</strong>.</p>
            <p><a href="%s">Télécharger votre reçu d'inscription</a></p>
            <p>Ce lien est valable 7 jours.</p>
            """
                .formatted(user.getFirstName(), course.getTitle(), downloadUrl),
            List.of());

    mailer.accept(email);
    log.info("Confirmation email with receipt link sent for subscription {}", subscription.getId());
  }
}
