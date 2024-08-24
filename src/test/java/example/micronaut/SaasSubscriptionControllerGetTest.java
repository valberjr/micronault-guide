package example.micronaut;

import com.jayway.jsonpath.JsonPath;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

@MicronautTest
class SaasSubscriptionControllerGetTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void shouldReturnASaasSubscriptionWhenDataIsSaved() {
        var client = httpClient.toBlocking();
        var response = client.exchange("/subscriptions/99", String.class);
        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.OK.getCode());

        var documentContext = JsonPath.parse(response.body());

        var id = documentContext.read("$.id");
        assertThat(id)
                .isNotNull()
                .isEqualTo(99);

        var name = documentContext.read("$.name");
        assertThat(name)
                .isNotNull()
                .isEqualTo("Advanced");

        var cents = documentContext.read("$.cents");
        assertThat(cents)
                .isNotNull()
                .isEqualTo(2900);
    }

    @Test
    void shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        var client = httpClient.toBlocking();
        var thrown = catchThrowableOfType(() -> client.exchange("/subscriptions/100", String.class), HttpClientResponseException.class);
        assertThat(thrown.getStatus().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
        assertThat(thrown.getResponse().getBody()).isEmpty();
    }

}