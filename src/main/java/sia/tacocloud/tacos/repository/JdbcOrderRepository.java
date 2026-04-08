package sia.tacocloud.tacos.repository;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import sia.tacocloud.tacos.model.Ingredient;
import sia.tacocloud.tacos.model.IngredientRef;
import sia.tacocloud.tacos.model.Taco;
import sia.tacocloud.tacos.model.TacoOrder;

import java.lang.reflect.Type;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcOrderRepository implements InterfaceOrderRepository {

    private JdbcOperations jdbcOperations;

    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }




    @Override
    @Transactional
    public TacoOrder save(TacoOrder order) {
        PreparedStatementCreatorFactory pscf =
        new PreparedStatementCreatorFactory(
                "insert into Taco_Order "
                + "(delivery_name, delivery_street, delivery_city, "
                + "delivery_state, delivery_zip, cc_number, "
                + "cc_expiration, cc_cvv, placed_at) "
                + "values(?,?,?,?,?,?,?,?,?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );
        pscf.setReturnGeneratedKeys(true);
        order.setPlacedAt(new Date());
        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                order.getDeliveryName(),
                                order.getDeliveryStreet(),
                                order.getDeliveryCity(),
                                order.getDeliveryArea(),
                                order.getDeliveryZip(),
                                order.getCcNumber(),
                                order.getCcExpiration(),
                                order.getCcCW(),
                                order.getPlacedAt()
                        )
                );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        Long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();
        int i = 0;
        for(Taco taco: tacos) {
            saveTaco(orderId, i++, taco);
        }
        return order;
    }

    private long saveTaco(Long orderId, int orderKey, Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(
                        "insert into Taco "
                        + "(name, created_at, taco_order, taco_order_key) "
                        + "values (?, ?, ?, ?)",
                        Types.VARCHAR, Types.TIMESTAMP, Types.BIGINT, Types.BIGINT
                );
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc =
                pscf.newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                taco.getCreatedAt(),
                                orderId,
                                orderKey
                        )
                );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        Long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);
        saveIngredientRefs(tacoId, taco.getIngredientRefs());
        return tacoId;
    }

    private void saveIngredientRefs(Long tacoId, List<IngredientRef> ingredientRefs) {
        int key = 0;
        for (IngredientRef ingredientRef: ingredientRefs) {
            jdbcOperations.update(
                    "insert into ingredient_ref (ingredient, taco, taco_key) "
                    + "values (?, ?, ?)",
                    ingredientRef.getIngredient(), tacoId, key++

            );
        }
    }
}
