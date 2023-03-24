package radon.naruto_universe.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FlameParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected FlameParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.lifetime = 15;

        this.xd = pXSpeed;
        this.yd = this.random.nextFloat() * pYSpeed;
        this.zd = pZSpeed;

        this.rCol = 255.0F / 255.0F;
        this.gCol = 75.0F / 255.0F;
        this.bCol = 0.0F / 255.0F;

        this.alpha = 0.5F;

        this.quadSize = Math.max(3.0F, this.random.nextFloat() * 3.0F);

        this.sprites = pSprites;
        this.setSprite(this.sprites.get(this.random));
    }

    @Override
    public void tick() {
        super.tick();
        this.setSprite(this.sprites.get(this.random));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRegistry.ModRenderTypes.GLOW;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(@NotNull SimpleParticleType pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ,
                                       double pXSpeed, double pYSpeed, double pZSpeed) {
            return new FlameParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
        }
    }
}
