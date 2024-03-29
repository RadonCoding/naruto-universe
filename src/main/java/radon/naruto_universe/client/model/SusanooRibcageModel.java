package radon.naruto_universe.client.model;

import net.minecraft.resources.ResourceLocation;
import radon.naruto_universe.NarutoUniverse;
import radon.naruto_universe.entity.SusanooEntity;
import software.bernie.geckolib.model.GeoModel;

public class SusanooRibcageModel extends GeoModel<SusanooEntity> {
	private static final ResourceLocation MODEL = new ResourceLocation(NarutoUniverse.MOD_ID, "geo/entity/susanoo_ribcage.geo.json");
	private static final ResourceLocation TEXTURE = new ResourceLocation(NarutoUniverse.MOD_ID, "textures/entity/susanoo_ribcage.png");
	private static final ResourceLocation ANIMATION = new ResourceLocation(NarutoUniverse.MOD_ID, "animations/entity/susanoo_ribcage.animation.json");

	@Override
	public ResourceLocation getModelResource(SusanooEntity animatable) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(SusanooEntity animatable) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(SusanooEntity animatable) {
		return ANIMATION;
	}
}