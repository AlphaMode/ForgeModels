package net.alphamode.mixin;

import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements IForgeBlockEntity {

    @Unique
    private CompoundTag customTileData;

    @Inject(method = "load", at = @At("HEAD"))
    public void forge$load(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("ForgeData")) this.customTileData = compoundTag.getCompound("ForgeData");
    }

    @Inject(method = "saveMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putInt(Ljava/lang/String;I)V", shift = At.Shift.BY, by = 3))
    public void forgepatch$saveMetadata(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
        if (this.customTileData != null) compoundTag.put("ForgeData", this.customTileData);
    }

    @Override
    public CompoundTag getTileData() {
        if (this.customTileData == null)
            this.customTileData = new CompoundTag();
        return this.customTileData;
    }
}
