package plus.dragons.card_weaponery.ini;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.recipe.CardCraftingRecipe;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, CardWeaponry.MODID);

    public static final RegistryObject<RecipeType<CardCraftingRecipe>> CARD_CRAFTING = RECIPE_TYPES.register("card_crafting", () -> new RecipeType<>() {
        public String toString() {
            return "reverting_from_item";
        }
    });

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CardWeaponry.MODID);
    public static final RegistryObject<RecipeSerializer<CardCraftingRecipe>> CARD_CRAFTING_SERIALIZER = RECIPE_SERIALIZERS.register("card_crafting", CardCraftingRecipe.Serializer::new);
}
