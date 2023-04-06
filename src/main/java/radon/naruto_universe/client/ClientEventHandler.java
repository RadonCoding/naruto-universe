package radon.naruto_universe.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import radon.naruto_universe.NarutoUniverse;
import radon.naruto_universe.ability.Ability;
import radon.naruto_universe.ability.NarutoAbilities;
import radon.naruto_universe.client.event.PlayerModelEvent;
import radon.naruto_universe.client.gui.DojutsuScreen;
import radon.naruto_universe.client.gui.NinjaScreen;
import radon.naruto_universe.client.layer.ModEyesLayer;
import radon.naruto_universe.client.gui.overlay.ChakraBarOverlay;
import radon.naruto_universe.client.model.ThrownKunaiModel;
import radon.naruto_universe.client.particle.FlameParticle;
import radon.naruto_universe.client.particle.NarutoParticles;
import radon.naruto_universe.client.render.FireballRenderer;
import radon.naruto_universe.client.render.EmptyRenderer;
import radon.naruto_universe.client.render.ThrownKunaiRenderer;
import radon.naruto_universe.entity.NarutoEntities;
import radon.naruto_universe.item.NarutoItems;
import radon.naruto_universe.network.PacketHandler;
import radon.naruto_universe.client.particle.VaporParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import radon.naruto_universe.network.packet.TriggerAbilityC2SPacket;

public class ClientEventHandler {
    @Mod.EventBusSubscriber(modid = NarutoUniverse.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onCreativeTabRegister(final CreativeModeTabEvent.Register event) {
            event.registerCreativeModeTab(new ResourceLocation(NarutoUniverse.MOD_ID, "tab"), builder -> {
                builder.title(Component.translatable(String.format("item_group.%s.%s", NarutoUniverse.MOD_ID, "tab")))
                        .icon(() -> new ItemStack(NarutoItems.KUNAI.get()))
                        .displayItems((pEnabledFeatures, pOutput, pDisplayOperatorCreativeTab) -> {
                            pOutput.accept(new ItemStack(NarutoItems.KUNAI.get()));
                            pOutput.accept(new ItemStack(NarutoItems.AKATSUKI_CLOAK.get()));
                        });
            });
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings(final RegisterKeyMappingsEvent event) {
            ClientAbilityHandler.registerKeyMapping(event, KeyRegistry.KEY_HAND_SIGN_ONE, () -> ClientAbilityHandler.handleAbilityKey(1));
            ClientAbilityHandler.registerKeyMapping(event, KeyRegistry.KEY_HAND_SIGN_TWO, () -> ClientAbilityHandler.handleAbilityKey(2));
            ClientAbilityHandler.registerKeyMapping(event, KeyRegistry.KEY_HAND_SIGN_THREE, () -> ClientAbilityHandler.handleAbilityKey(3));
            ClientAbilityHandler.registerKeyMapping(event, KeyRegistry.KEY_CHAKRA_JUMP, () -> {
                Ability ability = NarutoAbilities.CHAKRA_JUMP.get();
                PacketHandler.sendToServer(new TriggerAbilityC2SPacket(ability.getId()));
                ClientAbilityHandler.triggerAbility(ability);
            });
            ClientAbilityHandler.registerKeyMapping(event, KeyRegistry.OPEN_NINJA_SCREEN, () -> Minecraft.getInstance().setScreen(new NinjaScreen()));
            ClientAbilityHandler.registerKeyMapping(event, KeyRegistry.KEY_ACTIVATE_SPECIAL, SpecialAbilityHandler::triggerSelectedAbility);
            KeyRegistry.register(event);
        }

        @SubscribeEvent
        public static void onRegisterGuiOverlays(final RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("chakra_bar",  ChakraBarOverlay.HUD_CHAKRA_BAR);
            event.registerAboveAll("special_ability", SpecialAbilityHandler.SPECIAL_ABILITY);
        }

        @SubscribeEvent
        public static void onRegisterParticleProviders(final RegisterParticleProvidersEvent event) {
            event.register(NarutoParticles.VAPOR.get(), VaporParticle.Provider::new);
            event.register(NarutoParticles.FLAME.get(), FlameParticle.Provider::new);
        }

        @SubscribeEvent
        public static void onRegisterRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(NarutoEntities.FIREBALL_JUTSU.get(), FireballRenderer::new);
            event.registerEntityRenderer(NarutoEntities.THROWN_KUNAI.get(), ThrownKunaiRenderer::new);
            event.registerEntityRenderer(NarutoEntities.PARTICLE_SPAWNER.get(), EmptyRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ThrownKunaiModel.LAYER_LOCATION, ThrownKunaiModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void onAddLayers(final EntityRenderersEvent.AddLayers event) {
            event.getSkins().forEach((skin) -> {
                LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin(skin);
                assert renderer != null;
                renderer.addLayer(new ModEyesLayer<>(renderer));
            });
        }
    }

    @Mod.EventBusSubscriber(modid = NarutoUniverse.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(final TickEvent.ClientTickEvent event) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (event.phase != TickEvent.Phase.START || player == null) return;

            ClientAbilityHandler.tick(player);
            DoubleJumpHandler.tick(player);
        }

        @SubscribeEvent
        public static void onSetupPlayerAngles(PlayerModelEvent.SetupAngles.Post event) {
            AnimationHandler.animate(event.getEntity(), event.getModelPlayer());
        }

        @SubscribeEvent
        public static void onRenderLiving(final RenderLivingEvent.Pre<?, ?> event) {
            LivingEntity entity = event.getEntity();

            if (event.getRenderer().getModel() instanceof HumanoidModel<?> model) {
                AnimationHandler.animate(entity, model);
            }
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();

            if (event.getAction() == InputConstants.PRESS && event.getKey() == mc.options.keyJump.getKey().getValue()) {
                assert mc.player != null;
                DoubleJumpHandler.run(mc.player);
            }

            if (event.getAction() == InputConstants.PRESS) {
                if (event.getKey() == InputConstants.KEY_UP) {
                    SpecialAbilityHandler.scroll(1);
                } else if (event.getKey() == InputConstants.KEY_DOWN) {
                    SpecialAbilityHandler.scroll(-1);
                }

                if (KeyRegistry.SHOW_DOJUTSU_MENU.isDown()) {
                    mc.setScreen(new DojutsuScreen());
                }
            } else if (event.getAction() == InputConstants.RELEASE) {
                if (event.getKey() == KeyRegistry.SHOW_DOJUTSU_MENU.getKey().getValue() && mc.screen instanceof DojutsuScreen) {
                    mc.screen.onClose();
                }
            }
        }
    }
}