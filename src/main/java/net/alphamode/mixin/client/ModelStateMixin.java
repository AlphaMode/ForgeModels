package net.alphamode.mixin.client;

import net.minecraftforge.client.extensions.IForgeModelState;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.resources.model.ModelState;

@Mixin(ModelState.class)
public class ModelStateMixin implements IForgeModelState {

}
