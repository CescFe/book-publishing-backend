package org.cescfe.bookpublishing.shared.infrastructure.adapters.input.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
class HealthControllerTest {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `health endpoint should return 200 OK`() {
        val response = restTemplate.getForEntity("/api/v1/health", Map::class.java)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body!!["status"] == "OK")
        assert(response.body!!["message"] == "Hello World")
    }
}
