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

package net.minecraftforge.client.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;

import com.mojang.math.*;

public interface IForgeTransformation
{
    private Transformation self()
    {
        return (Transformation)(Object)this;
    }

    default boolean isIdentity()
    {
        return self().equals(Transformation.identity());
    }

    default void push(PoseStack stack)
    {
        stack.pushPose();

        Vector3f trans = self().getTranslation();
        stack.translate(trans.x(), trans.y(), trans.z());

        stack.mulPose(self().getLeftRotation());

        Vector3f scale = self().getScale();
        stack.scale(scale.x(), scale.y(), scale.z());

        stack.mulPose(self().getRightRotation());

    }

    default void transformPosition(Vector4f position)
    {
        position.transform(self().getMatrix());
    }

    default void transformNormal(Vector3f normal)
    {
        normal.transform(((IForgeTransformation) (Object)self()).getNormalMatrix());
        normal.normalize();
    }

    default Direction rotateTransform(Direction facing)
    {
        return Direction.rotate(self().getMatrix(), facing);
    }

    /**
     * convert transformation from assuming center-block system to opposing-corner-block system
     */
    default Transformation blockCenterToCorner()
    {
        return applyOrigin(new Vector3f(.5f, .5f, .5f));
    }

    /**
     * convert transformation from assuming opposing-corner-block system to center-block system
     */
    default Transformation blockCornerToCenter()
    {
        return applyOrigin(new Vector3f(-.5f, -.5f, -.5f));
    }

    /**
     * Apply this transformation to a different origin.
     * Can be used for switching between coordinate systems.
     * Parameter is relative to the current origin.
     */
    default Transformation applyOrigin(Vector3f origin) {
        Transformation transform = self();
        if (((IForgeTransformation) (Object)transform).isIdentity()) return Transformation.identity();

        Matrix4f ret = transform.getMatrix();
        Matrix4f tmp = Matrix4f.createTranslateMatrix(origin.x(), origin.y(), origin.z());

        Matrix4f copy = tmp.copy();
        copy.multiply(ret);
        ret.load(copy);
        tmp.setIdentity();
        tmp.m00 = 1.0F;
        tmp.m11 = 1.0F;
        tmp.m22 = 1.0F;
        tmp.m33 = 1.0F;
        tmp.m03 = -origin.x();
        tmp.m13 = -origin.y();
        tmp.m23 = -origin.z();
        ret.multiply(tmp);
        return new Transformation(ret);
    }

    Matrix3f getNormalMatrix();
}
