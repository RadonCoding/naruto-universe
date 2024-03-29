package radon.naruto_universe.ability.utility;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import radon.naruto_universe.ability.NarutoAbilities;
import radon.naruto_universe.capability.ninja.NinjaRank;
import radon.naruto_universe.client.gui.widget.AbilityDisplayInfo;
import radon.naruto_universe.sound.NarutoSounds;
import radon.naruto_universe.ability.Ability;
import radon.naruto_universe.capability.ninja.NinjaTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvent;

import java.util.List;

public class Rinnegan extends Ability implements Ability.IToggled, Ability.ISpecial {
    @Override
    public boolean isDojutsu() {
        return true;
    }

    @Override
    public NinjaRank getRank() {
        return NinjaRank.UNRANKED;
    }

    @Override
    public List<NinjaTrait> getRequirements() {
        return List.of(NinjaTrait.UNLOCKED_RINNEGAN);
    }

    @Override
    public ActivationType getActivationType() {
        return ActivationType.TOGGLED;
    }

    @Override
    public AbilityDisplayInfo getDisplay(LivingEntity owner) {
        return new AbilityDisplayInfo(this.getId().getPath(), 8.0F, 2.0F);
    }

    @Override
    public ChatFormatting getChatColor() {
        return ChatFormatting.DARK_PURPLE;
    }

    @Override
    public Ability getParent() {
        return NarutoAbilities.MANGEKYO.get();
    }

    @Override
    public float getCost(LivingEntity owner) {
        return 0.05F;
    }

    @Override
    public SoundEvent getActivationSound() {
        return NarutoSounds.RINNEGAN_ACTIVATE.get();
    }

    @Override
    public List<Ability> getSpecialAbilities(LivingEntity owner) {
        return List.of(NarutoAbilities.TENGAI_SHINSEI.get(), NarutoAbilities.CHIBAKU_TENSEI.get());
    }
}

