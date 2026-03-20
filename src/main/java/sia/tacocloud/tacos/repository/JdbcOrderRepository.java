package sia.tacocloud.tacos.repository;

import org.springframework.stereotype.Repository;
import sia.tacocloud.tacos.model.TacoOrder;

@Repository
public class JdbcOrderRepository implements InterfaceOrderRepository {

    @Override
    public TacoOrder save(TacoOrder order) {
        return null;
    }
}
