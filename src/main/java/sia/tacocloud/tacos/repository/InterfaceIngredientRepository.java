package sia.tacocloud.tacos.repository;

import sia.tacocloud.tacos.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface InterfaceIngredientRepository {

    List<Ingredient> findAll();
    Optional<Ingredient> findById(String id);
    Ingredient save(Ingredient ingredient);
}
