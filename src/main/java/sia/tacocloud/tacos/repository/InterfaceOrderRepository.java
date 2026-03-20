package sia.tacocloud.tacos.repository;

import sia.tacocloud.tacos.model.TacoOrder;

public interface InterfaceOrderRepository {
    TacoOrder save(TacoOrder order);
}
