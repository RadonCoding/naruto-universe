package radon.naruto_universe.mixin.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import radon.naruto_universe.client.event.PlayerModelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    private T entity;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float netHeadYaw;
    private float headPitch;

    @Inject(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void capture(T entity, float entityYaw, float deltaTicks, PoseStack poseStack, MultiBufferSource source, int light, CallbackInfo ci, boolean shouldSit, float f, float f1, float f2, float f6, float f7, float f8, float f5) {
        this.entity = entity;
        this.limbSwing = f5;
        this.limbSwingAmount = f8;
        this.ageInTicks = f7;
        this.netHeadYaw = f2;
        this.headPitch = f6;
    }

    @Redirect(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    public void render(M model, PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (this.entity instanceof Player && model instanceof PlayerModel) {
            if (!MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Pre((Player) this.entity, (PlayerModel<Player>) model, poseStack, consumer, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getDeltaFrameTime()))) {
                model.renderToBuffer(poseStack, consumer, light, overlay, red, green, blue, alpha);
                MinecraftForge.EVENT_BUS.post(new PlayerModelEvent.Render.Post((Player) this.entity, (PlayerModel<Player>) model, poseStack, consumer, light, overlay, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, Minecraft.getInstance().getDeltaFrameTime()));
            }
            return;
        }
        model.renderToBuffer(poseStack, consumer, light, overlay, red, green, blue, alpha);
    }
}