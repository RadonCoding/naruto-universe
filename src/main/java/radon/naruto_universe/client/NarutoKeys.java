package radon.naruto_universe.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import radon.naruto_universe.NarutoUniverse;
import net.minecraft.client.KeyMapping;

public class NarutoKeys {
    public static String KEY_CATEGORY_NARUTO_UNIVERSE = String.format("key.category.%s", NarutoUniverse.MOD_ID);
    public static KeyMapping KEY_HAND_SIGN_ONE = createKeyMapping("hand_sign_one",
            InputConstants.KEY_C);
    public static KeyMapping KEY_HAND_SIGN_TWO = createKeyMapping("hand_sign_two",
            InputConstants.KEY_V);
    public static KeyMapping KEY_HAND_SIGN_THREE = createKeyMapping("hand_sign_three",
            InputConstants.KEY_B);
    public static KeyMapping KEY_CHAKRA_JUMP = createKeyMapping("chakra_jump",
            InputConstants.KEY_X);
    public static KeyMapping OPEN_NINJA_SCREEN = createKeyMapping("open_ninja_screen",
            InputConstants.KEY_J);
    public static KeyMapping SHOW_DOJUTSU_MENU = createKeyMapping("show_dojutsu_menu",
            InputConstants.KEY_Z);
    public static KeyMapping KEY_ACTIVATE_SPECIAL = createKeyMapping("activate_special",
            InputConstants.KEY_G);

    private static KeyMapping createKeyMapping(String name, int keyCode) {
        return new KeyMapping(String.format("key.%s.%s", NarutoUniverse.MOD_ID, name), keyCode, NarutoKeys.KEY_CATEGORY_NARUTO_UNIVERSE);
    }

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_NINJA_SCREEN);
        event.register(SHOW_DOJUTSU_MENU);
        event.register(KEY_CHAKRA_JUMP);
    }
}
