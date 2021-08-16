package net.alphamode.mixin.client;

import net.alphamode.ItemOverridesEx;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.renderer.block.model.ItemOverrides;

@Mixin(ItemOverrides.class)
public class ItemOverridesMixin implements ItemOverridesEx {
    @Shadow @Final private ItemOverrides.BakedOverride[] overrides;

    @Override
    public com.google.common.collect.ImmutableList<ItemOverrides.BakedOverride> getOverrides() {
        return com.google.common.collect.ImmutableList.copyOf(overrides);
    }
}
