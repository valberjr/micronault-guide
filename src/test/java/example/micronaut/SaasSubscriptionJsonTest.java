package example.micronaut;

import com.jayway.jsonpath.JsonPath;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false)
class SaasSubscriptionJsonTest {

    @Test
    void saasSubscriptionSerializationTest(JsonMapper json, ResourceLoader resourceLoader) throws IOException {
        var subscription = new SaasSubscription(99L, "Professional", 4900);
        var expected = getResourceAsString(resourceLoader, "expected.json");
        var result = json.writeValueAsString(subscription);
        assertThat(result).isEqualToIgnoringWhitespace(expected);

        var documentContext = JsonPath.parse(result);

        var id = documentContext.read("$.id");
        assertThat(id)
                .isNotNull()
                .isEqualTo(99);

        var name = documentContext.read("$.name");
        assertThat(name)
                .isNotNull()
                .isEqualTo("Professional");

        var cents = documentContext.read("$.cents");
        assertThat(cents)
                .isNotNull()
                .isEqualTo(4900);
    }

    @Test
    void saasSubscriptionDeserializationTest(JsonMapper json) throws IOException {
        var expected = """
                {
                    "id":100,
                        "name": "Advanced",
                        "cents":2900
                }
                """;

        assertThat(json.readValue(expected, SaasSubscription.class)).isEqualTo(new SaasSubscription(100L, "Advanced", 2900));
        assertThat(json.readValue(expected, SaasSubscription.class).id()).isEqualTo(100L);
        assertThat(json.readValue(expected, SaasSubscription.class).name()).isEqualTo("Advanced");
        assertThat(json.readValue(expected, SaasSubscription.class).cents()).isEqualTo(2900);
    }

    private static String getResourceAsString(ResourceLoader resourceLoader, String resourceName) {
        return resourceLoader.getResourceAsStream(resourceName)
                .flatMap(stream -> {
                    try (InputStream is = stream;
                         Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                         BufferedReader bufferedReader = new BufferedReader(reader)) {
                        return Optional.of(bufferedReader.lines().collect(Collectors.joining("\n")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Optional.empty();
                })
                .orElse("");
    }

}