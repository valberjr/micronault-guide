package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record SaasSubscription(Long id, String name, Integer cents) {
}
