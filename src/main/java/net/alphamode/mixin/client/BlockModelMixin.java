package net.alphamode.mixin.client;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.alphamode.BlockModelEx;
import net.minecraftforge.client.model.BlockModelConfiguration;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

@Mixin(BlockModel.class)
public abstract class BlockModelMixin implements BlockModelEx {
    @Shadow @Nullable protected ResourceLocation parentLocation;
    @Shadow @Final private static Logger LOGGER;

    @Shadow public abstract Material getMaterial(String string);

    @Shadow public abstract List<BlockElement> getElements();

    @Shadow public String name;
    @Shadow @Final private List<ItemOverride> overrides;

    @Shadow public abstract BlockModel getRootModel();

    @Shadow public abstract ItemTransforms getTransforms();

    @Shadow protected abstract ItemOverrides getItemOverrides(ModelBakery modelBakery, BlockModel blockModel);

    @Shadow
    protected static BakedQuad bakeFace(BlockElement blockElement, BlockElementFace blockElementFace, TextureAtlasSprite textureAtlasSprite, Direction direction, ModelState modelState, ResourceLocation resourceLocation) {
        return null;
    }

    @Unique
    public final net.minecraftforge.client.model.BlockModelConfiguration customData = new net.minecraftforge.client.model.BlockModelConfiguration((BlockModel) (Object) this);

    @Override
    public BlockModelConfiguration ForgeModels$getCustomData() {
        return customData;
    }

    @Inject(method = "getElements", at = @At("HEAD"), cancellable = true)
    public void ForgeModelsPatch$getElements(CallbackInfoReturnable<List<BlockElement>> cir) {
        if (customData.hasCustomGeometry()) cir.setReturnValue(java.util.Collections.emptyList());
    }

    /**
     * @author
     */
    @Overwrite
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> p_111469_, Set<Pair<String, String>> p_111470_) {
        Set<UnbakedModel> set = Sets.newLinkedHashSet();

        for(BlockModel blockmodel = (BlockModel) (Object) this; blockmodel.parentLocation != null && blockmodel.parent == null; blockmodel = blockmodel.parent) {
            set.add(blockmodel);
            UnbakedModel unbakedmodel = p_111469_.apply(blockmodel.parentLocation);
            if (unbakedmodel == null) {
                LOGGER.warn("No parent '{}' while loading model '{}'", this.parentLocation, blockmodel);
            }

            if (set.contains(unbakedmodel)) {
                LOGGER.warn("Found 'parent' loop while loading model '{}' in chain: {} -> {}", blockmodel, set.stream().map(Object::toString).collect(Collectors.joining(" -> ")), this.parentLocation);
                unbakedmodel = null;
            }

            if (unbakedmodel == null) {
                blockmodel.parentLocation = ModelBakery.MISSING_MODEL_LOCATION;
                unbakedmodel = p_111469_.apply(blockmodel.parentLocation);
            }

            if (!(unbakedmodel instanceof BlockModel)) {
                throw new IllegalStateException("BlockModel parent has to be a block model.");
            }

            blockmodel.parent = (BlockModel)unbakedmodel;
        }

        Set<Material> set1 = Sets.newHashSet(this.getMaterial("particle"));

        if (customData.hasCustomGeometry())
            set1.addAll(customData.getTextureDependencies(p_111469_, p_111470_));
        else
            for(BlockElement blockelement : this.getElements()) {
                for(BlockElementFace blockelementface : blockelement.faces.values()) {
                    Material material = this.getMaterial(blockelementface.texture);
                    if (Objects.equals(material.texture(), MissingTextureAtlasSprite.getLocation())) {
                        p_111470_.add(Pair.of(blockelementface.texture, this.name));
                    }

                    set1.add(material);
                }
            }

        this.overrides.forEach((p_111475_) -> {
            UnbakedModel unbakedmodel1 = p_111469_.apply(p_111475_.getModel());
            if (!Objects.equals(unbakedmodel1, this)) {
                set1.addAll(unbakedmodel1.getMaterials(p_111469_, p_111470_));
            }
        });
        if (this.getRootModel() == ModelBakery.GENERATION_MARKER) {
            ItemModelGenerator.LAYERS.forEach((p_111467_) -> {
                set1.add(this.getMaterial(p_111467_));
            });
        }

        return set1;
    }

    @Override
    public BakedModel bakeVanilla(ModelBakery p_111450_, BlockModel p_111451_, Function<Material, TextureAtlasSprite> p_111452_, ModelState p_111453_, ResourceLocation p_111454_, boolean p_111455_) {
        TextureAtlasSprite textureatlassprite = p_111452_.apply(this.getMaterial("particle"));
        if (this.getRootModel() == ModelBakery.BLOCK_ENTITY_MARKER) {
            return new BuiltInModel(this.getTransforms(), this.getItemOverrides(p_111450_, p_111451_), textureatlassprite, ((BlockModel)(Object)this).getGuiLight().lightLikeBlock());
        } else {
            SimpleBakedModel.Builder simplebakedmodel$builder = (new SimpleBakedModel.Builder(((BlockModel) (Object) this), this.getItemOverrides(p_111450_, p_111451_), p_111455_)).particle(textureatlassprite);

            for (BlockElement blockelement : this.getElements()) {
                for (Direction direction : blockelement.faces.keySet()) {
                    BlockElementFace blockelementface = blockelement.faces.get(direction);
                    TextureAtlasSprite textureatlassprite1 = p_111452_.apply(this.getMaterial(blockelementface.texture));
                    if (blockelementface.cullForDirection == null) {
                        simplebakedmodel$builder.addUnculledFace(bakeFace(blockelement, blockelementface, textureatlassprite1, direction, p_111453_, p_111454_));
                    } else {
                        simplebakedmodel$builder.addCulledFace(Direction.rotate(p_111453_.getRotation().getMatrix(), blockelementface.cullForDirection), bakeFace(blockelement, blockelementface, textureatlassprite1, direction, p_111453_, p_111454_));
                    }
                }
            }

            return simplebakedmodel$builder.build();
        }
    }

    @Override
    public ItemOverrides getOverrides(ModelBakery p_111447_, BlockModel p_111448_, Function<Material, TextureAtlasSprite> textureGetter) {
        return ItemOverrides.EMPTY;//this.overrides.isEmpty() ? ItemOverrides.EMPTY : new ItemOverrides(p_111447_, p_111448_, p_111447_::getModel, textureGetter, this.overrides);
    }
}
