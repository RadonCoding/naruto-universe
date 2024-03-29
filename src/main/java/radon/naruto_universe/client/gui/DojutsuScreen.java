package radon.naruto_universe.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import radon.naruto_universe.ability.Ability;
import radon.naruto_universe.ability.NarutoAbilities;
import radon.naruto_universe.capability.ninja.NinjaPlayerHandler;
import radon.naruto_universe.client.ability.ClientAbilityHandler;
import radon.naruto_universe.network.PacketHandler;
import radon.naruto_universe.network.packet.TriggerAbilityC2SPacket;
import radon.naruto_universe.util.HelperMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DojutsuScreen extends Screen {
    private final List<Ability> abilities = new ArrayList<>();

    private int hovered = -1;

    public DojutsuScreen() {
        super(Component.nullToEmpty(null));
    }

    @Override
    protected void init() {
        super.init();

        assert this.minecraft != null;
        this.abilities.addAll(NarutoAbilities.getDojutsuAbilities(this.minecraft.player));

        if (this.abilities.isEmpty()) {
            this.onClose();
        }
    }

    private void drawSlot(PoseStack poseStack, BufferBuilder buffer, float centerX, float centerY,
                          float radiusIn, float radiusOut, float startAngle, float endAngle, int color) {
        float angle = endAngle - startAngle;
        float PRECISION = 2.5F / 360.0F;
        int sections = Math.max(1, Mth.ceil(angle / PRECISION));

        angle = endAngle - startAngle;

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = (color >> 24) & 0xFF;

        float slice = angle / sections;

        for (int i = 0; i < sections; i++) {
            float angle1 = startAngle + i * slice;
            float angle2 = startAngle + (i + 1) * slice;

            float x1 = centerX + radiusIn * (float) Math.cos(angle1);
            float y1 = centerY + radiusIn * (float) Math.sin(angle1);
            float x2 = centerX + radiusOut * (float) Math.cos(angle1);
            float y2 = centerY + radiusOut * (float) Math.sin(angle1);
            float x3 = centerX + radiusOut * (float) Math.cos(angle2);
            float y3 = centerY + radiusOut * (float) Math.sin(angle2);
            float x4 = centerX + radiusIn * (float) Math.cos(angle2);
            float y4 = centerY + radiusIn * (float) Math.sin(angle2);

            Matrix4f pose = poseStack.last().pose();
            buffer.vertex(pose, x2, y2, 0.0F).color(r, g, b, a).endVertex();
            buffer.vertex(pose, x1, y1, 0.0F).color(r, g, b, a).endVertex();
            buffer.vertex(pose, x4, y4, 0.0F).color(r, g, b, a).endVertex();
            buffer.vertex(pose, x3, y3, 0.0F).color(r, g, b, a).endVertex();
        }
    }

    @Override
    public void onClose() {
        if (this.hovered != -1) {
            Ability ability = this.abilities.get(this.hovered);
            PacketHandler.sendToServer(new TriggerAbilityC2SPacket(ability.getId()));
            ClientAbilityHandler.triggerAbility(ability);
        }
        super.onClose();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTicks);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.pushPose();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        float radiusIn = 30.0F;
        float radiusOut = radiusIn * 2.0F;

        assert this.minecraft != null;
        assert this.minecraft.player != null;
        
        for (int i = 0; i < this.abilities.size(); i++) {
            float startAngle = getAngleFor(i - 0.5F);
            float endAngle = getAngleFor(i + 0.5F);

            Ability ability = this.abilities.get(i);
            AtomicBoolean toggled = new AtomicBoolean(false);
            this.minecraft.player.getCapability(NinjaPlayerHandler.INSTANCE).ifPresent(cap -> toggled.set(cap.hasToggledAbility(ability)));

            int white = HelperMethods.toRGB24(255, 255, 255, 150);
            int black = HelperMethods.toRGB24(0, 0, 0, 150);

            int color;

            if (toggled.get()) {
                color = this.hovered == i ? black : white;
            }
            else {
                color = this.hovered == i ? white : black;
            }
            this.drawSlot(pPoseStack, buffer, centerX, centerY, radiusIn, radiusOut, startAngle, endAngle, color);
        }
        tesselator.end();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.translate(-8.0F, -8.0F, 0.0F);

        float iconRadius = (radiusIn + radiusOut) / 2.0F;

        for (int i = 0; i < this.abilities.size(); i++) {
            float startAngle = getAngleFor(i - 0.5F);
            float endAngle = getAngleFor(i + 0.5F);
            float middle = (startAngle + endAngle) / 2.0F;
            int posX = (int) (centerX + iconRadius * (float) Math.cos(middle));
            int posY = (int) (centerY + iconRadius * (float) Math.sin(middle));

            Ability ability = this.abilities.get(i);
            RenderSystem.setShaderTexture(0, ability.getDisplay(this.minecraft.player).getIcon());
            blit(pPoseStack, posX, posY, 0, 0, 16, 16,
                    16, 16);
        }
        pPoseStack.popPose();
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        double mouseAngle = Math.atan2(pMouseY - centerY, pMouseX - centerX);
        double mousePos = Math.sqrt(Math.pow(pMouseX - centerX, 2.0D) + Math.pow(pMouseY - centerY, 2.0D));

        if (this.abilities.size() > 0) {
            float startAngle = getAngleFor(-0.5F);
            float endAngle = getAngleFor(this.abilities.size() - 0.5F);

            while (mouseAngle < startAngle) {
                mouseAngle += Mth.TWO_PI;
            }
            while (mouseAngle >= endAngle) {
                mouseAngle -= Mth.TWO_PI;
            }
        }

        float radiusIn = 30.0F;
        float radiusOut = radiusIn * 2.0F;

        this.hovered = -1;

        for (int i = 0; i < this.abilities.size(); i++) {
            float startAngle = getAngleFor(i - 0.5F);
            float endAngle = getAngleFor(i + 0.5F);

            if (mouseAngle >= startAngle && mouseAngle < endAngle && mousePos >= radiusIn && mousePos < radiusOut) {
                this.hovered = i;
                break;
            }
        }
    }

    private float getAngleFor(double i)
    {
        if (this.abilities.size() == 0) {
            return 0;
        }
        return (float) (((i / this.abilities.size()) + 0.25) * Mth.TWO_PI + Math.PI);
    }
}
