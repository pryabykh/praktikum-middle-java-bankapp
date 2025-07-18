package com.pryabykh.bankapp.notifications;

import com.pryabykh.bankapp.notifications.dto.NotificationCreateDto;
import com.pryabykh.bankapp.notifications.service.NotificationService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

class NotificationApplicationTests extends SpringBootPostgreSQLTestContainerBaseTest {

	@Autowired
	private KafkaTemplate<String, String> notificationsKafkaTemplate;

	@MockitoSpyBean
	private NotificationService notificationService;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	void kafkaTest() throws JsonProcessingException, ExecutionException, InterruptedException {
		NotificationCreateDto dto = new NotificationCreateDto();
		dto.setMessage("message");
		dto.setLogin("login");
		dto.setSourceId(1L);

		notificationsKafkaTemplate.send(
				"notifications",
				UUID.randomUUID().toString(),
				new ObjectMapper().writeValueAsString(dto)
		).get();

		Thread.sleep(2000);
		Mockito.verify(notificationService, Mockito.times(1)).create(Mockito.anyString());
	}
}
