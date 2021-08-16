package net.alphamode.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.resources.model.BakedModel;

@Mixin(BakedModel.class)
public interface BakedModelMixin extends net.minecraftforge.client.extensions.IForgeBakedModel {
}
