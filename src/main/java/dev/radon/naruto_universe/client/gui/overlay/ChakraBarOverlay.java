package dev.radon.naruto_universe.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.radon.naruto_universe.capability.NinjaPlayerHandler;
import dev.radon.naruto_universe.NarutoUniverse;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ChakraBarOverlay{
    public static final ResourceLocation TEXTURE = new ResourceLocation(NarutoUniverse.MOD_ID, "textures/overlay/chakra_bar.png");

    public static final IGuiOverlay HUD_CHAKRA_BAR = (gui, poseStack, partialTicks, width, height) -> {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        GuiComponent.blit(poseStack, 20, 20, 0, 0, 93, 9, 93, 16);

        LocalPlayer player = gui.getMinecraft().player;

        player.getCapability(NinjaPlayerHandler.INSTANCE).ifPresent(cap -> {
            float chakraWidth = (cap.getChakra() / cap.getMaxChakra()) * 94.0F;
            GuiComponent.blit(poseStack, 20, 21, 0, 9, (int) chakraWidth, 7, 93, 16);
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            gui.getFont().draw(poseStack, String.format("%f / %f", cap.getChakra(), cap.getMaxChakra()),
                    (20.0F * 2.0F) + 5.0F, (20.0F * 2.0F) + 5.5F, 16777215);
            poseStack.popPose();
        });

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    };
}