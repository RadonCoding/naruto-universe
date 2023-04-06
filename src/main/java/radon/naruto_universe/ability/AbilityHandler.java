package radon.naruto_universe.ability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class AbilityHandler {
    public static void triggerAbility(LivingEntity owner, Ability ability) {
        if (!ability.isUnlocked(owner) || !ability.checkChakra(owner)) {
            return;
        }

        if (ability.getActivationType() == Ability.ActivationType.INSTANT) {
            ability.runServer(owner);

            SoundEvent sound = ability.getActivationSound();

            if (sound != null) {
                owner.level.playSound(null, owner.blockPosition(), sound, SoundSource.PLAYERS, 10.0F, 1.0F);
            }
        } else if (ability.getActivationType() == Ability.ActivationType.CHANNELED) {
            NarutoAbilities.setChanneledAbility(owner, ability);
        } else if (ability.getActivationType() == Ability.ActivationType.TOGGLED) {
            NarutoAbilities.setToggledAbility(owner, ability);
        }
    }
}