package net.alphamode;

import net.minecraft.client.renderer.block.model.ItemOverrides;

public interface ItemOverridesEx {
    com.google.common.collect.ImmutableList<ItemOverrides.BakedOverride> getOverrides();
}
