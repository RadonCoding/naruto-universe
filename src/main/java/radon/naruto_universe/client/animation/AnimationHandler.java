package radon.naruto_universe.client.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class AnimationHandler {
    public static void animate(LivingEntity entity, HumanoidModel<?> model) {
        /*if (entity.isSprinting() && !entity.isSwimming()) {
            model.body.xRot = 0.5F;

            boolean rotateLeftArm = true, rotateRightArm = true;

            if (entity.isUsingItem()) {
                rotateRightArm = entity.getUsedItemHand() != InteractionHand.MAIN_HAND;
                rotateLeftArm = entity.getUsedItemHand() != InteractionHand.OFF_HAND;
            } else if (entity.swinging) {
                rotateRightArm = entity.swingingArm != InteractionHand.MAIN_HAND;
                rotateLeftArm = entity.swingingArm != InteractionHand.OFF_HAND;
            }

            if (rotateRightArm) {
                model.rightArm.xRot = 1.6F;
            }

            if (rotateLeftArm) {
                model.leftArm.xRot = 1.6F;
            }

            model.head.y = 4.2F;
            model.body.y = 3.2F;
            model.rightArm.y = 5.2F;
            model.leftArm.y = 5.2F;
            model.rightLeg.y = 12.2F;
            model.leftLeg.y = 12.2F;
            model.rightLeg.z = 4.0F;
            model.leftLeg.z = 4.0F;
        }*/
    }
}
