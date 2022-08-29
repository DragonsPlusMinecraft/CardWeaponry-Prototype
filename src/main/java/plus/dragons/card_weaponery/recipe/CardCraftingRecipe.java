package plus.dragons.card_weaponery.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import plus.dragons.card_weaponery.forgeregistry.CardWeaponryRegistries;
import plus.dragons.card_weaponery.card.CardFeature;
import plus.dragons.card_weaponery.ini.RecipeRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CardCraftingRecipe implements Recipe<OneItemContainer> {

    public static Codec<CardCraftingRecipe> code(@Nullable ResourceLocation resourceLocation) {
        return Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
            var json = dynamic.convert(JsonOps.INSTANCE).getValue().getAsJsonObject();
            ResourceLocation rl;
            if (json.has("id"))
                rl = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, json.getAsJsonPrimitive("id")).getOrThrow(false, err -> {
                    throw new JsonSyntaxException(err);
                });
            else rl = resourceLocation;
            List<Ingredient> ingredients = new ArrayList<>();
            if (json.has("ingredients")) {
                try {
                    for (JsonElement jsonElement : json.getAsJsonArray("ingredients")) {
                        ingredients.add(Ingredient.fromJson(jsonElement));
                    }
                } catch (Exception e) {
                    throw new JsonSyntaxException("Error in recipe json id" + resourceLocation);
                }
            } else {
                return DataResult.error("Recipe json must have ingredients.");
            }
            List<CardFeature> features1 = new ArrayList<>();
            if (json.has("features")) {
                try {
                    for (JsonElement jsonElement : json.getAsJsonArray("features")) {
                        features1.add(CardWeaponryRegistries.CARD_FEATURES.get().getValue(new ResourceLocation(jsonElement.getAsString())));
                    }
                } catch (Exception e) {
                    throw new JsonSyntaxException("Error in recipe json id" + resourceLocation);
                }
            } else {
                return DataResult.error("Recipe json must have features.");
            }
            return DataResult.success(new CardCraftingRecipe(rl, features1, ingredients));
        }, recipe -> {
            var result = new JsonObject();
            result.add("id", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, recipe.id).getOrThrow(false, err -> {
            }));
            var ingredientsJsonArray = new JsonArray();
            for(var i: recipe.ingredients){
                ingredientsJsonArray.add(i.toJson());
            }
            result.add("ingredients", ingredientsJsonArray);
            var featuresJsonArray = new JsonArray();
            for(var f: recipe.features){
                featuresJsonArray.add(f.getRegistryName().toString());
            }
            result.add("features", featuresJsonArray);
            return new Dynamic<>(JsonOps.INSTANCE, result);
        });
    }


    private final ResourceLocation id;
    private final List<Ingredient> ingredients;
    private final List<CardFeature> features;


    public CardCraftingRecipe(ResourceLocation id, List<CardFeature> features, List<Ingredient> ingredients) {
        this.id = id;
        this.ingredients = ingredients;
        this.features = features;
    }

    @Override
    public boolean matches(@NotNull OneItemContainer container, @NotNull Level level) {
        return ingredients.stream().anyMatch(ingredient -> ingredient.test(container.get()));
    }

    // Returns an Item that is the result of this recipe
    // Here we do not use this method for exporting result. So EMPTY is returned.
    @Override
    public ItemStack assemble(@NotNull OneItemContainer container) {
        return ItemStack.EMPTY;
    }

    // Can Craft at any dimension
    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    // Get the result of this recipe, usually for display purposes (e.g. recipe book).
    // Here we do not use this method for exporting result. So EMPTY is returned.
    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.CARD_CRAFTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.CARD_CRAFTING.get();
    }


    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CardCraftingRecipe> {

        @Override
        public CardCraftingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            var a = code(resourceLocation).parse(JsonOps.INSTANCE, jsonObject);
            if (a.get().right().isPresent()) {
                throw new JsonSyntaxException(a.get().right().get().message());
            }
            return a.get().left().get();
        }

        @Nullable
        @Override
        public CardCraftingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf packetBuffer) {
            return packetBuffer.readWithCodec(code(resourceLocation));
        }

        @Override
        public void toNetwork(FriendlyByteBuf packetBuffer, CardCraftingRecipe itemStackReversionRecipe) {
            packetBuffer.writeWithCodec(code(null), itemStackReversionRecipe);
        }
    }
}
