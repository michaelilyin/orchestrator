package online.ilyin.controller

import online.ilyin.SchedulerApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest
import ru.tinkoff.kora.test.extension.junit5.KoraAppTestConfigModifier
import ru.tinkoff.kora.test.extension.junit5.KoraConfigModification
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

@Testcontainers
@KoraAppTest(value = SchedulerApplication::class)
class SchedulesControllerScenarioTest: KoraAppTestConfigModifier {

    @Container
    internal val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:16"))
        .withReuse(true)

    private val client = HttpClient.newHttpClient()

    override fun config(): KoraConfigModification {
        return KoraConfigModification.ofSystemProperty("POSTGRES_JDBC_URL", postgres.getJdbcUrl())
                .withSystemProperty("POSTGRES_USER", postgres.username)
                .withSystemProperty("POSTGRES_PASSWORD", postgres.password)
    }

    @Test
    fun testGetAllSchedules() {
        val result = client.send(
                HttpRequest.newBuilder().GET()
                        .uri(URI.create("http://localhost:8080/schedules"))
                        .build(),
                BodyHandlers.ofByteArray()
        )

        assertThat(result.statusCode()).isEqualTo(200)
    }
}