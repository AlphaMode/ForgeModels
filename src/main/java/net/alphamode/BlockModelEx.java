package net.alphamode;

import net.minecraftforge.client.model.BlockModelConfiguration;
import java.util.function.Function;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

public interface BlockModelEx {
    BlockModelConfiguration ForgeModels$getCustomData();

    @Deprecated
    BakedModel bakeVanilla(ModelBakery p_111450_, BlockModel p_111451_, Function<Material, TextureAtlasSprite> p_111452_, ModelState p_111453_, ResourceLocation p_111454_, boolean p_111455_);

    ItemOverrides getOverrides(ModelBakery p_111447_, BlockModel p_111448_, Function<Material, TextureAtlasSprite> textureGetter);
}
