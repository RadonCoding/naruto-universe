package radon.naruto_universe;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jetbrains.annotations.NotNull;

public class NarutoParticleRenderTypes {
    public static final ParticleRenderType GLOW = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder buffer, @NotNull TextureManager manager) {
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
        }
    };

    public static final ParticleRenderType TRANSLUCENT = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder buffer, @NotNull TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(519);
            RenderSystem.enableBlend();
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.disableDepthTest();
            RenderSystem.depthFunc(515);
        }
    };
}