package unitTest;

import fr.unice.polytech.cod.Order;
import fr.unice.polytech.cod.OrderState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdatableObjectTest {
    private Order myPendingOrder;

    @BeforeEach
    void Init(){
        myPendingOrder = new Order(null, OrderState.PENDING, null);
    }

    @Test
    void orderStateChangeTest() throws InterruptedException {
        myPendingOrder.setWaitingTime(500);
        assertEquals(OrderState.PENDING, myPendingOrder.getOrderState());

        myPendingOrder.updateState(OrderState.READY);
        assertEquals(OrderState.READY, myPendingOrder.getOrderState());

        sleep(1000);
        assertEquals(OrderState.OBSOLETE, myPendingOrder.getOrderState());
    }

    @Test
    void multipleOrderStateChangeTest() throws InterruptedException {
        myPendingOrder.setWaitingTime(10000);
        myPendingOrder.updateState(OrderState.READY);
        sleep(1000);
        myPendingOrder.updateState(OrderState.READY);

    }
}
