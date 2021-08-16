package net.alphamode;

import net.minecraftforge.fml.Logging;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if(targetClassName.equals("net.minecraft.client.renderer.block.model")) {
            MethodVisitor methodVisitor = targetClass.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "Lnet/minecraft/client/renderer/block/model/ItemOverrides;<init>(Lnet/minecraft/client/resources/model/ModelBakery;Lnet/minecraft/client/resources/model/UnbakedModel;Ljava/util/function/Function;Ljava/util/function/Function;Ljava/util/List;", "Lnet/minecraft/client/renderer/block/model/ItemOverrides;<init>(Lnet/minecraft/client/resources/model/ModelBakery;Lnet/minecraft/client/resources/model/UnbakedModel;Ljava/util/function/Function;Ljava/util/function/Function;Ljava/util/List;)V", null);
            methodVisitor.visitInvokeDynamicInsn("<init>", "java/lang/Object.<init> ()V", null);
            methodVisitor.visitEnd();
        }
    }
}
