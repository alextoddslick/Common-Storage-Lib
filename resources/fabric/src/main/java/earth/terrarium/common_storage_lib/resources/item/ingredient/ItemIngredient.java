package earth.terrarium.common_storage_lib.resources.item.ingredient;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("UnstableApiUsage")
public class ItemIngredient {
    public static final MapCodec<Ingredient> NON_EMPTY_MAP_CODEC = ItemIngredient.getNonEmptyMapCodec();

    public static Ingredient all(Ingredient... ingredients) {
        return DefaultCustomIngredients.all(ingredients);
    }

    public static Ingredient any(Ingredient... ingredients) {
        return DefaultCustomIngredients.any(ingredients);
    }

    public static Ingredient difference(Ingredient base, Ingredient subtracted) {
        return DefaultCustomIngredients.difference(base, subtracted);
    }

    public static Ingredient components(Ingredient base, DataComponentExactPredicate components) {
        return DefaultCustomIngredients.components(base, components.asPatch());
    }

    public static Ingredient components(ItemStack stack) {
        return DefaultCustomIngredients.components(stack);
    }

    private static MapCodec<Ingredient> getNonEmptyMapCodec() {
        // In MC 1.21.11, Ingredient is backed by HolderSet<Item> instead of Value[].
        // Fabric's IngredientMixin patches Ingredient.CODEC to transparently handle
        // custom ingredients (via CustomIngredientImpl dispatch), so we can use it directly.
        // We wrap it as a MapCodec via fieldOf() and add non-empty validation.
        return Ingredient.CODEC.fieldOf("ingredient").validate(ingredient -> {
            if (ingredient.isEmpty()) {
                return DataResult.error(() -> "Ingredient cannot be empty");
            }
            return DataResult.success(ingredient);
        });
    }
}
