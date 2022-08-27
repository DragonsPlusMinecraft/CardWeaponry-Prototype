package plus.dragons.card_weaponery.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.entity.FlyingCardEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import plus.dragons.card_weaponery.model.CardModel;

public class FlyingCardEntityRenderer extends EntityRenderer<FlyingCardEntity> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(CardWeaponry.MODID,"textures/entity/white_card.png");
    private final CardModel<FlyingCardEntity> model;

    public FlyingCardEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CardModel<>(context.bakeLayer(CardModel.LAYER));
        shadowRadius = 0.03F;
    }

    @Override
    public void render(FlyingCardEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.pushPose();
        VertexConsumer vertexconsumer = pBuffer.getBuffer(model.renderType(TEXTURE_LOCATION));
        model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(FlyingCardEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
