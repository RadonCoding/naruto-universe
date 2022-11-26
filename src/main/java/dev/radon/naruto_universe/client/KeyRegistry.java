package dev.radon.naruto_universe.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.radon.naruto_universe.NarutoUniverse;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyRegistry {
    public static final String KEY_CATEGORY_GAMEPLAY = createCategory("gameplay");
    public static final KeyMapping KEY_HAND_SIGN_ONE = createKeyMapping("hand_sign_one", KEY_CATEGORY_GAMEPLAY,
            InputConstants.KEY_C);
    public static final KeyMapping KEY_HAND_SIGN_TWO = createKeyMapping("hand_sign_two", KEY_CATEGORY_GAMEPLAY,
            InputConstants.KEY_V);
    public static final KeyMapping KEY_HAND_SIGN_THREE = createKeyMapping("hand_sign_three", KEY_CATEGORY_GAMEPLAY,
            InputConstants.KEY_B);

    public static final KeyMapping OPEN_ABILITY_SCREEN = createKeyMapping("open_ability_screen", KEY_CATEGORY_GAMEPLAY,
            InputConstants.KEY_J);

    private static String createCategory(String name) {
        final String category = String.format("key.category.%s.%s", NarutoUniverse.MOD_ID, name);
        return category;
    }

    private static KeyMapping createKeyMapping(String name, String category, int keyCode) {
        final KeyMapping key = new KeyMapping(String.format("key.%s.%s", NarutoUniverse.MOD_ID, name), keyCode, category);
        return key;
    }
}