package de.tyrannus.cleandebug.mixins;

import de.tyrannus.cleandebug.CleanDebug;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = DebugHud.class, priority = 900)
public abstract class DebugHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            method = "drawLeftText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void modifyDrawLeftText(DrawContext ignored, CallbackInfo ci, List<String> lines) {
        CleanDebug.modifyLeftLines(lines);
    }

    @Inject(
            method = "drawRightText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void modifyDrawRightText(DrawContext ignored, CallbackInfo ci, List<String> lines) {
        CleanDebug.modifyRightLines(lines);
    }

    @Redirect(method = "getRightText", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;", ordinal = 1))
    protected final HitResult.Type changeFluidHitType(HitResult result) {
        return CleanDebug.getFluidHitResultType(result, client.world);
    }
}