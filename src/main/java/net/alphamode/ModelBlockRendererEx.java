package net.alphamode;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraftforge.client.model.data.IModelData;
import java.util.BitSet;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface ModelBlockRendererEx {
    boolean tesselateBlock(BlockAndTintGetter p_111048_, BakedModel p_111049_, BlockState p_111050_, BlockPos p_111051_, PoseStack p_111052_, VertexConsumer p_111053_, boolean p_111054_, Random p_111055_, long p_111056_, int p_111057_, net.minecraftforge.client.model.data.IModelData modelData);
    boolean tesselateWithAO(BlockAndTintGetter p_111079_, BakedModel p_111080_, BlockState p_111081_, BlockPos p_111082_, PoseStack p_111083_, VertexConsumer p_111084_, boolean p_111085_, Random p_111086_, long p_111087_, int p_111088_, net.minecraftforge.client.model.data.IModelData modelData);
    boolean tesselateWithoutAO(BlockAndTintGetter blockAndTintGetter, BakedModel model, BlockState blockState, BlockPos blockPos, PoseStack matrixStack, VertexConsumer vertexConsumer, boolean bl, Random random, long l, int i, net.minecraftforge.client.model.data.IModelData modelData);
    void calculateShape(BlockAndTintGetter p_111040_, BlockState p_111041_, BlockPos p_111042_, int[] p_111043_, Direction p_111044_, @Nullable float[] p_111045_, BitSet p_111046_);
}
