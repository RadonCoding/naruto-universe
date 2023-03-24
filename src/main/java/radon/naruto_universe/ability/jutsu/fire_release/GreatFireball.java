package radon.naruto_universe.ability.jutsu.fire_release;

import net.minecraft.world.entity.LivingEntity;
import radon.naruto_universe.ability.Ability;
import radon.naruto_universe.ability.AbilityRegistry;
import radon.naruto_universe.capability.NinjaPlayerHandler;
import radon.naruto_universe.capability.NinjaRank;
import radon.naruto_universe.capability.NinjaTrait;
import radon.naruto_universe.client.gui.widget.AbilityDisplayInfo;
import radon.naruto_universe.entity.FireballJutsuEntity;
import radon.naruto_universe.sound.SoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GreatFireball extends Ability {
    @Override
    public List<NinjaTrait> getRequirements() {
        return List.of(NinjaTrait.FIRE_RELEASE);
    }

    @Override
    public NinjaRank getRank() {
        return NinjaRank.CHUNIN;
    }

    @Override
    public long getCombo() {
        return 123;
    }

    @Override
    public NinjaTrait getRelease() {
        return NinjaTrait.FIRE_RELEASE;
    }

    @Override
    public AbilityDisplayInfo getDisplay() {
        String iconPath = this.getId().getPath();
        return new AbilityDisplayInfo(iconPath, 2.0F, 2.0F);
    }

    @Override
    public Ability getParent() {
        return AbilityRegistry.CHAKRA_CONTROL.get();
    }

    @Override
    public float getMinPower() {
        return 0.1F;
    }

    public ChatFormatting getChatColor() {
        return ChatFormatting.YELLOW;
    }

    @Override
    public float getCost() {
        return 25.0F;
    }

    @Override
    public void runClient(LivingEntity owner) {

    }

    @Override
    public void runServer(LivingEntity owner) {
        owner.getCapability(NinjaPlayerHandler.INSTANCE).ifPresent(cap -> {
            owner.level.playSound(null, owner.blockPosition(), SoundRegistry.GREAT_FIREBALL.get(),
                    SoundSource.PLAYERS, 1.0F, 1.0F);

            cap.delayTickEvent((playerClone) -> {
                Vec3 look = playerClone.getLookAngle();
                FireballJutsuEntity fireball = new FireballJutsuEntity(playerClone, look.x(), look.y(), look.z(), 0.5F, this.getPower(), 1.5F, 3.0F);
                playerClone.level.addFreshEntity(fireball);
                playerClone.level.playSound(null, playerClone.blockPosition(), SoundEvents.FIRECHARGE_USE,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
            }, 20);
        });
    }
}
