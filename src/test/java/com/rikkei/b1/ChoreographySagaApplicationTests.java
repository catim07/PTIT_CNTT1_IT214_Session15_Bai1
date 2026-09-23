package com.rikkei.b1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.rikkei.b1.service.SagaChoreographyService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChoreographySagaApplicationTests {

    @Autowired
    private SagaChoreographyService sagaService;

    @Test
    void testChoreographySagaFlow() {
        var created = sagaService.createOrder("O100", "P1", 2, 100.0);
        var payment = sagaService.processPayment(created);
        var stock = sagaService.reserveStock(payment);

        assertTrue(payment.isSuccess());
        assertTrue(stock.isReserved());
        assertEquals("COMPLETED", sagaService.getStatus("O100"));
    }
}
