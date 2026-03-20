package sia.tacocloud.tacos.converter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sia.tacocloud.tacos.model.Ingredient;
import sia.tacocloud.tacos.repository.InterfaceIngredientRepository;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private final InterfaceIngredientRepository ingredientRepo;

    @Autowired
    public IngredientByIdConverter(InterfaceIngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }


    @Override
    public Ingredient convert(String id) {
        return ingredientRepo.findById(id).orElse(null);
    }
}
