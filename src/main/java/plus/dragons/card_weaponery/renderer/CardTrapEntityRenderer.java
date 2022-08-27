package plus.dragons.card_weaponery.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.entity.CardTrapEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import plus.dragons.card_weaponery.model.CardModel;

public class CardTrapEntityRenderer extends EntityRenderer<CardTrapEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(CardWeaponry.MODID,"textures/entity/white_card.png");
    private final CardModel<CardTrapEntity> model;

    public CardTrapEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CardModel<>(context.bakeLayer(CardModel.LAYER));
        shadowRadius = 0.03F;
    }

    @Override
    public void render(CardTrapEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        // TODO
    }

    @Override
    public ResourceLocation getTextureLocation(CardTrapEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
