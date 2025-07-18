package com.pryabykh.bankapp.accounts;

import com.pryabykh.bankapp.accounts.feign.notifications.NotificationCreateDto;
import com.pryabykh.bankapp.accounts.service.NotificationKafkaService;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;

class AccountsApplicationTests extends SpringBootPostgreSQLTestContainerBaseTest {

	@Autowired
	private NotificationKafkaService notificationKafkaService;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	void kafkaTest() {
		NotificationCreateDto dto = new NotificationCreateDto();
		dto.setMessage("message");
		dto.setLogin("login");
		dto.setSourceId(1L);

		try (var consumerForTest = new DefaultKafkaConsumerFactory<>(
				KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker),
				new StringDeserializer(),
				new StringDeserializer()
		).createConsumer()) {
			// 0. Подписываем консьюмера на топики
			consumerForTest.subscribe(List.of("notifications"));

			// 1. Отправляем сообщение в топик
			notificationKafkaService.create(dto);

			// 2. Проверяем сообщение, которое мы только что отправили в input-topic
			var inputMessage = KafkaTestUtils.getSingleRecord(consumerForTest, "notifications", Duration.ofSeconds(10));
			Assertions.assertNotNull(inputMessage.key());
			Assertions.assertNotNull(inputMessage.value());
		}
	}
}
