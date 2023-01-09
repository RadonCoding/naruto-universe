package dev.radon.naruto_universe.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.radon.naruto_universe.NarutoUniverse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES,
            NarutoUniverse.MOD_ID);

    public static RegistryObject<SimpleParticleType> CHAKRA = PARTICLES.register("vapor", () ->
            new SimpleParticleType(true));

    public static class ModRenderTypes {
        public static final ParticleRenderType VAPOR = new ParticleRenderType() {
            public void begin(BufferBuilder buffer, TextureManager manager) {
                RenderSystem.depthMask(false);
                Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
                RenderSystem.enableBlend();
                RenderSystem.enableCull();
                RenderSystem.enableDepthTest();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE.value);
                RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
            }

            public void end(Tesselator tesselator) {
                tesselator.end();
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
                Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
            }
        };
    }
}