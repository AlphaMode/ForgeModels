/*
 * Minecraft Forge
 * Copyright (c) 2016-2021.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.common.model.animation;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class CapabilityAnimation
{
//    @CapabilityInject(IAnimationStateMachine.class)
//    public static Capability<IAnimationStateMachine> ANIMATION_CAPABILITY = null;
    public static BlockApiLookup<IAnimationStateMachine, Void> ANIMATION_CAPABILITY = null;

    public static void register()
    {
        ANIMATION_CAPABILITY = BlockApiLookup.get(new ResourceLocation("forge", "animation_state_capability"), IAnimationStateMachine.class, Void.class);
        //CapabilityManager.INSTANCE.register(IAnimationStateMachine.class);
    }

    public static class DefaultItemAnimationCapabilityProvider implements BlockApiLookup.BlockEntityApiProvider<IAnimationStateMachine, Void>
    {
        @Nonnull
        private final LazyOptional<IAnimationStateMachine> asm;

        public DefaultItemAnimationCapabilityProvider(@Nonnull LazyOptional<IAnimationStateMachine> asm)
        {
            this.asm = asm;
        }

        @Override
        public @Nullable IAnimationStateMachine find(BlockEntity blockEntity, Void context) {
            return ANIMATION_CAPABILITY.find(blockEntity.getLevel(), blockEntity.getBlockPos(), context);
        }
    }
}
