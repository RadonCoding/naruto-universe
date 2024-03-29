package radon.naruto_universe.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import radon.naruto_universe.NarutoUniverse;
import radon.naruto_universe.ability.special.Susanoo;
import radon.naruto_universe.block.NarutoBlocks;
import radon.naruto_universe.item.armor.AkatsukiCloakItem;
import radon.naruto_universe.item.armor.ModArmorMaterial;

public class NarutoItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NarutoUniverse.MOD_ID);

    public static RegistryObject<Item> KUNAI = ITEMS.register("kunai",
            () -> new KunaiItem(new Item.Properties()));
    public static RegistryObject<ArmorItem> AKATSUKI_CLOAK = ITEMS.register("akatsuki_cloak",
            () -> new AkatsukiCloakItem(ModArmorMaterials.AKATSUKI, EquipmentSlot.CHEST, new Item.Properties()));
    public static RegistryObject<Item> SUSANOO_SWORD = ITEMS.register("susanoo_sword",
            () -> new SusanooSwordItem(new Item.Properties()));

    public static class ModArmorMaterials {
        public static ModArmorMaterial AKATSUKI = new ModArmorMaterial("akatsuki", 37, new int[]{0, 0, 20, 0}, 15, SoundEvents.ARMOR_EQUIP_LEATHER,
                3.0F, 0.1F, () -> Ingredient.of(Items.NETHERITE_INGOT));
    }
}
