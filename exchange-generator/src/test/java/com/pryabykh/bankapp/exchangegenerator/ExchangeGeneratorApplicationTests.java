package com.pryabykh.bankapp.exchangegenerator;

import com.pryabykh.bankapp.exchangegenerator.service.UpdateRandomCurrencyService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestOAuth2Config.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ExchangeGeneratorApplicationTests {

	@Autowired
	private UpdateRandomCurrencyService updateRandomCurrencyService;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Test
	@Order(1)
	void contextLoads() {
	}

	@Test
	@Order(2)
	void kafkaTest() {
		try (var consumerForTest = new DefaultKafkaConsumerFactory<>(
				KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker),
				new StringDeserializer(),
				new StringDeserializer()
		).createConsumer()) {
			// 0. Подписываем консьюмера на топики
			consumerForTest.subscribe(List.of("exchange-generator"));

			// 1. Отправляем сообщение в топик
			updateRandomCurrencyService.update();

			// 2. Проверяем сообщение, которое мы только что отправили в input-topic
			ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumerForTest);

			for (ConsumerRecord<String, String> record : records) {
				Assertions.assertNotNull(record.key());
				Assertions.assertNotNull(record.value());
			}
		}
	}
}
