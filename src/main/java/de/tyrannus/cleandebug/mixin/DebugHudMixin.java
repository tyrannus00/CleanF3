package de.tyrannus.cleandebug.mixin;

import de.tyrannus.cleandebug.CleanDebugConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = DebugHud.class, priority = 900)
public abstract class DebugHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            method = "renderLeftText",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 2
            ), locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onRenderLeftText(MatrixStack matrices, CallbackInfo ci, List<String> lines) {
        if (CleanDebugConfig.hideIris) {
            lines.removeIf(s -> s.startsWith("[Iris]"));
            lines.removeIf(s -> s.startsWith("[Entity Batching]"));
        }

        if (CleanDebugConfig.hideActiveRenderer) {
            lines.removeIf(s -> s.startsWith("[Fabric] Active renderer:"));
        }

        if (CleanDebugConfig.hideDebugHints) {
            lines.removeIf(s -> s.contains("Debug: Pie"));
        }
    }

    @Inject(
            method = "renderRightText",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;size()I"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onRenderRightText(MatrixStack matrices, CallbackInfo ci, List<String> lines, int i) {
        if (CleanDebugConfig.hideIris) {
            lines.removeIf(s -> s.startsWith("[Iris]"));
        }

        if (CleanDebugConfig.hardwareMode == CleanDebugConfig.HardwareMode.NONE) {
            lines.removeIf(s -> s.startsWith("Direct Buffers:"));
        }
    }

    @Inject(method = "getRightText", at = @At(value = "RETURN", ordinal = 1))
    private void blockTags(CallbackInfoReturnable<List<String>> cir) {
        var lines = cir.getReturnValue();

        if (CleanDebugConfig.hideSodium) {
            var sodiumIndex = lines.indexOf("Sodium Renderer");

            if (sodiumIndex != -1) {
                lines.remove(sodiumIndex);
                lines.remove(sodiumIndex);
            }
        }

        switch (CleanDebugConfig.hardwareMode) {
            case NONE: {
                lines.removeIf(s ->
                        s.startsWith("Java:") ||
						s.startsWith("Mem:") ||
						s.startsWith("Allocation rate:") ||
						s.startsWith("Direct Buffers:") ||
						s.startsWith("Allocated:") ||
						s.startsWith("Off-Heap:") ||
						s.startsWith("Chunk arena allocator:") ||
						s.startsWith("Device buffer objects:") ||
						s.startsWith("Device memory:") ||
						s.startsWith("Staging buffer:")
                );
            }
            case REDUCED: {
                lines.removeIf(s -> s.startsWith("CPU:"));

                int displayIndex = -1;
                for (var i = 0; i < lines.size(); i++) {
                    var string = lines.get(i);

                    if (!string.startsWith("Display:")) {
                        continue;
                    }

                    displayIndex = i - (CleanDebugConfig.hardwareMode == CleanDebugConfig.HardwareMode.REDUCED ? 1 : 2);
                    break;
                }

                if (displayIndex < 0) {
                    displayIndex = 0;
                }

                for (var i = 0; i < 5; i++) {
                    if (displayIndex >= lines.size()) {
                        break;
                    }

                    lines.remove(displayIndex);
                }
            }
        }

        if (CleanDebugConfig.hideTags) {
            lines.removeIf(s -> s.startsWith("#"));
        }
    }

    @Redirect(method = "getRightText", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;", ordinal = 1))
    protected final HitResult.Type fluidVisibility(HitResult result) {
        if (CleanDebugConfig.shyFluids && result instanceof BlockHitResult blockHitResult) {
            if (client.world != null && client.world.getFluidState(blockHitResult.getBlockPos()).isEmpty()) {
                return HitResult.Type.MISS;
            }
        }

        return result.getType();
    }
}