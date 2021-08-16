package net.alphamode.mixin.client;

import net.alphamode.ModelBakeryEx;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Set;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin implements ModelBakeryEx {
    @Override
    public Set<ResourceLocation> getSpecialModels() {
        return java.util.Collections.emptySet();
    }
}
