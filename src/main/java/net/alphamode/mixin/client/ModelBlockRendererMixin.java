package net.alphamode.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.alphamode.ModelBlockRendererEx;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.extensions.IForgeBlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.client.renderer.LevelRenderer.DIRECTIONS;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Mixin(ModelBlockRenderer.class)
public abstract class ModelBlockRendererMixin implements ModelBlockRendererEx {
    @Shadow public abstract boolean tesselateWithAO(BlockAndTintGetter blockAndTintGetter, BakedModel bakedModel, BlockState blockState, BlockPos blockPos, PoseStack poseStack, VertexConsumer vertexConsumer, boolean bl, Random random, long l, int i);

    @Shadow public abstract boolean tesselateWithoutAO(BlockAndTintGetter blockAndTintGetter, BakedModel bakedModel, BlockState blockState, BlockPos blockPos, PoseStack poseStack, VertexConsumer vertexConsumer, boolean bl, Random random, long l, int i);

    @Shadow @Final private BlockColors blockColors;

    @Shadow protected abstract void renderModelFaceFlat(BlockAndTintGetter blockAndTintGetter, BlockState blockState, BlockPos blockPos, int i, int j, boolean bl, PoseStack poseStack, VertexConsumer vertexConsumer, List<BakedQuad> list, BitSet bitSet);

    private void renderModelFaceAO(BlockAndTintGetter p_111013_, BlockState p_111014_, BlockPos p_111015_, PoseStack p_111016_, VertexConsumer p_111017_, List<BakedQuad> p_111018_, float[] p_111019_, BitSet p_111020_, ModelBlockRenderer.AmbientOcclusionFace p_111021_, int p_111022_) {
        for(BakedQuad bakedquad : p_111018_) {
            ((ModelBlockRendererEx)this).calculateShape(p_111013_, p_111014_, p_111015_, bakedquad.getVertices(), bakedquad.getDirection(), p_111019_, p_111020_);
            p_111021_.calculate(p_111013_, p_111014_, p_111015_, bakedquad.getDirection(), p_111019_, p_111020_, bakedquad.isShade());
            this.putQuadData(p_111013_, p_111014_, p_111015_, p_111017_, p_111016_.last(), bakedquad, p_111021_.brightness[0], p_111021_.brightness[1], p_111021_.brightness[2], p_111021_.brightness[3], p_111021_.lightmap[0], p_111021_.lightmap[1], p_111021_.lightmap[2], p_111021_.lightmap[3], p_111022_);
        }

    }

    @Override
    public void calculateShape(BlockAndTintGetter p_111040_, BlockState p_111041_, BlockPos p_111042_, int[] p_111043_, Direction p_111044_, @Nullable float[] p_111045_, BitSet p_111046_) {
        float f = 32.0F;
        float f1 = 32.0F;
        float f2 = 32.0F;
        float f3 = -32.0F;
        float f4 = -32.0F;
        float f5 = -32.0F;

        for(int i = 0; i < 4; ++i) {
            float f6 = Float.intBitsToFloat(p_111043_[i * 8]);
            float f7 = Float.intBitsToFloat(p_111043_[i * 8 + 1]);
            float f8 = Float.intBitsToFloat(p_111043_[i * 8 + 2]);
            f = Math.min(f, f6);
            f1 = Math.min(f1, f7);
            f2 = Math.min(f2, f8);
            f3 = Math.max(f3, f6);
            f4 = Math.max(f4, f7);
            f5 = Math.max(f5, f8);
        }

        if (p_111045_ != null) {
            p_111045_[Direction.WEST.get3DDataValue()] = f;
            p_111045_[Direction.EAST.get3DDataValue()] = f3;
            p_111045_[Direction.DOWN.get3DDataValue()] = f1;
            p_111045_[Direction.UP.get3DDataValue()] = f4;
            p_111045_[Direction.NORTH.get3DDataValue()] = f2;
            p_111045_[Direction.SOUTH.get3DDataValue()] = f5;
            int j = DIRECTIONS.length;
            p_111045_[Direction.WEST.get3DDataValue() + j] = 1.0F - f;
            p_111045_[Direction.EAST.get3DDataValue() + j] = 1.0F - f3;
            p_111045_[Direction.DOWN.get3DDataValue() + j] = 1.0F - f1;
            p_111045_[Direction.UP.get3DDataValue() + j] = 1.0F - f4;
            p_111045_[Direction.NORTH.get3DDataValue() + j] = 1.0F - f2;
            p_111045_[Direction.SOUTH.get3DDataValue() + j] = 1.0F - f5;
        }

        float f9 = 1.0E-4F;
        float f10 = 0.9999F;
        switch(p_111044_) {
            case DOWN:
                p_111046_.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
                p_111046_.set(0, f1 == f4 && (f1 < 1.0E-4F || p_111041_.isCollisionShapeFullBlock(p_111040_, p_111042_)));
                break;
            case UP:
                p_111046_.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
                p_111046_.set(0, f1 == f4 && (f4 > 0.9999F || p_111041_.isCollisionShapeFullBlock(p_111040_, p_111042_)));
                break;
            case NORTH:
                p_111046_.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
                p_111046_.set(0, f2 == f5 && (f2 < 1.0E-4F || p_111041_.isCollisionShapeFullBlock(p_111040_, p_111042_)));
                break;
            case SOUTH:
                p_111046_.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
                p_111046_.set(0, f2 == f5 && (f5 > 0.9999F || p_111041_.isCollisionShapeFullBlock(p_111040_, p_111042_)));
                break;
            case WEST:
                p_111046_.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
                p_111046_.set(0, f == f3 && (f < 1.0E-4F || p_111041_.isCollisionShapeFullBlock(p_111040_, p_111042_)));
                break;
            case EAST:
                p_111046_.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
                p_111046_.set(0, f == f3 && (f3 > 0.9999F || p_111041_.isCollisionShapeFullBlock(p_111040_, p_111042_)));
        }

    }

    private void putQuadData(BlockAndTintGetter p_111024_, BlockState p_111025_, BlockPos p_111026_, VertexConsumer p_111027_, PoseStack.Pose p_111028_, BakedQuad p_111029_, float p_111030_, float p_111031_, float p_111032_, float p_111033_, int p_111034_, int p_111035_, int p_111036_, int p_111037_, int p_111038_) {
        float f;
        float f1;
        float f2;
        if (p_111029_.isTinted()) {
            int i = this.blockColors.getColor(p_111025_, p_111024_, p_111026_, p_111029_.getTintIndex());
            f = (float)(i >> 16 & 255) / 255.0F;
            f1 = (float)(i >> 8 & 255) / 255.0F;
            f2 = (float)(i & 255) / 255.0F;
        } else {
            f = 1.0F;
            f1 = 1.0F;
            f2 = 1.0F;
        }

        p_111027_.putBulkData(p_111028_, p_111029_, new float[]{p_111030_, p_111031_, p_111032_, p_111033_}, f, f1, f2, new int[]{p_111034_, p_111035_, p_111036_, p_111037_}, p_111038_, true);
    }

    @Override
    public boolean tesselateBlock(BlockAndTintGetter blockAndTintGetter, BakedModel model, BlockState blockState, BlockPos blockPos, PoseStack p_111052_, VertexConsumer p_111053_, boolean bl, Random p_111055_, long l, int i, net.minecraftforge.client.model.data.IModelData modelData) {
        boolean flag = Minecraft.useAmbientOcclusion() && ((IForgeBlockState)blockState).getLightEmission(blockAndTintGetter, blockPos) == 0 && model.useAmbientOcclusion();
        Vec3 vec3 = blockState.getOffset(blockAndTintGetter, blockPos);
        p_111052_.translate(vec3.x, vec3.y, vec3.z);
        modelData = ((IForgeBakedModel)model).getModelData(blockAndTintGetter, blockPos, blockState, modelData);

        try {
            return flag ? this.tesselateWithAO(blockAndTintGetter, model, blockState, blockPos, p_111052_, p_111053_, bl, p_111055_, l, i, modelData) : ((ModelBlockRendererEx)this).tesselateWithoutAO(blockAndTintGetter, model, blockState, blockPos, p_111052_, p_111053_, bl, p_111055_, l, i, modelData);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Block model being tesselated");
            CrashReportCategory.populateBlockDetails(crashreportcategory, blockAndTintGetter, blockPos, blockState);
            crashreportcategory.setDetail("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }
    @Override
    public boolean tesselateWithoutAO(BlockAndTintGetter p_111091_, BakedModel p_111092_, BlockState p_111093_, BlockPos p_111094_, PoseStack p_111095_, VertexConsumer p_111096_, boolean p_111097_, Random p_111098_, long p_111099_, int p_111100_, net.minecraftforge.client.model.data.IModelData modelData) {
        boolean flag = false;
        BitSet bitset = new BitSet(3);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_111094_.mutable();

        for(Direction direction : DIRECTIONS) {
            p_111098_.setSeed(p_111099_);
            List<BakedQuad> list = ((IForgeBakedModel)p_111092_).getQuads(p_111093_, direction, p_111098_, modelData);
            if (!list.isEmpty()) {
                blockpos$mutableblockpos.setWithOffset(p_111094_, direction);
                if (!p_111097_ || Block.shouldRenderFace(p_111093_, p_111091_, p_111094_, direction, blockpos$mutableblockpos)) {
                    int i = LevelRenderer.getLightColor(p_111091_, p_111093_, blockpos$mutableblockpos);
                    this.renderModelFaceFlat(p_111091_, p_111093_, p_111094_, i, p_111100_, false, p_111095_, p_111096_, list, bitset);
                    flag = true;
                }
            }
        }

        p_111098_.setSeed(p_111099_);
        List<BakedQuad> list1 = ((IForgeBakedModel)p_111092_).getQuads(p_111093_, null, p_111098_, modelData);
        if (!list1.isEmpty()) {
            this.renderModelFaceFlat(p_111091_, p_111093_, p_111094_, -1, p_111100_, true, p_111095_, p_111096_, list1, bitset);
            flag = true;
        }

        return flag;
    }

    @Override
    public boolean tesselateWithAO(BlockAndTintGetter p_111079_, BakedModel p_111080_, BlockState p_111081_, BlockPos p_111082_, PoseStack p_111083_, VertexConsumer p_111084_, boolean p_111085_, Random p_111086_, long p_111087_, int p_111088_, IModelData modelData) {
        boolean flag = false;
        float[] afloat = new float[DIRECTIONS.length * 2];
        BitSet bitset = new BitSet(3);
        //ModelBlockRenderer.AmbientOcclusionFace modelblockrenderer$ambientocclusionface = new ModelBlockRenderer.AmbientOcclusionFace();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_111082_.mutable();

        for(Direction direction : DIRECTIONS) {
            p_111086_.setSeed(p_111087_);
            List<BakedQuad> list = ((IForgeBakedModel)p_111080_).getQuads(p_111081_, direction, p_111086_, modelData);
            if (!list.isEmpty()) {
                blockpos$mutableblockpos.setWithOffset(p_111082_, direction);
                if (!p_111085_ || Block.shouldRenderFace(p_111081_, p_111079_, p_111082_, direction, blockpos$mutableblockpos)) {
                    this.renderModelFaceAO(p_111079_, p_111081_, p_111082_, p_111083_, p_111084_, list, afloat, bitset, null, p_111088_);
                    flag = true;
                }
            }
        }

        p_111086_.setSeed(p_111087_);
        List<BakedQuad> list1 = ((IForgeBakedModel)p_111080_).getQuads(p_111081_, null, p_111086_, modelData);
        if (!list1.isEmpty()) {
            this.renderModelFaceAO(p_111079_, p_111081_, p_111082_, p_111083_, p_111084_, list1, afloat, bitset, null, p_111088_);
            flag = true;
        }

        return flag;
    }
}
