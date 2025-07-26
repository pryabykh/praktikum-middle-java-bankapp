package com.pryabykh.bankapp.exchange;

import com.pryabykh.bankapp.exchange.dto.UpdateRandomCurrencyDto;
import com.pryabykh.bankapp.exchange.service.RateService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.concurrent.ExecutionException;


@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ExchangeApplicationTests extends SpringBootPostgreSQLTestContainerBaseTest {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@MockitoSpyBean
	private RateService rateService;

	@Test
	@Order(2)
	void contextLoads() {
	}

	@Test
	@Order(1)
	void kafkaTest() throws JsonProcessingException, ExecutionException, InterruptedException {
		UpdateRandomCurrencyDto dto = new UpdateRandomCurrencyDto();
		dto.setValue(1);

		kafkaTemplate.send(
				"exchange-generator",
				UUID.randomUUID().toString(),
				new ObjectMapper().writeValueAsString(dto)
		).get();

		Thread.sleep(2000);
		Mockito.verify(rateService, Mockito.times(1)).updateRandomCurrency(Mockito.anyString());
	}
}
