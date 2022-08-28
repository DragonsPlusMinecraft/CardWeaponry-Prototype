package plus.dragons.card_weaponery.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import plus.dragons.card_weaponery.CardWeaponry;
import plus.dragons.card_weaponery.entity.CardEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import plus.dragons.card_weaponery.model.CardModel;

public class CardEntityRenderer extends EntityRenderer<CardEntity> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(CardWeaponry.MODID,"textures/entity/white_card.png");
    private final CardModel<CardEntity> model;

    public CardEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CardModel<>(context.bakeLayer(CardModel.LAYER));
        shadowRadius = 0.03F;
    }

    @Override
    public void render(CardEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        // Do not render name plate
        // super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        // TODO need solve "vanish when half way in" problem
        pPoseStack.pushPose();
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot())));
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.getRotationAngle(), pEntity.getRotationAngle() + pEntity.getRotationSpeedTick())));
        VertexConsumer vertexconsumer = pBuffer.getBuffer(model.renderType(TEXTURE_LOCATION));
        model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(CardEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
