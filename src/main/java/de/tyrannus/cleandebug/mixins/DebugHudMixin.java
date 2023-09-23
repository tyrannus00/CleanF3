package de.tyrannus.cleandebug.mixins;

import de.tyrannus.cleandebug.CleanDebugConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = DebugHud.class, priority = 900)
public class DebugHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            method = "drawLeftText",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    shift = At.Shift.AFTER,
                    ordinal = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onRenderLeftText(DrawContext context, CallbackInfo ci, List<String> lines) {
        if (CleanDebugConfig.hideActiveRenderer) {
            lines.removeIf(s -> s.startsWith("[Fabric] Active renderer:"));
        }

        if (CleanDebugConfig.hideDebugHints) {
            lines.removeIf(s -> s.contains("Debug: Pie"));
        }

        if (CleanDebugConfig.hideHelpShortcut) {
            lines.removeIf(s -> s.equals("For help: press F3 + Q"));
        }

        if (CleanDebugConfig.hideIris) {
            lines.removeIf(s -> s.startsWith("[Iris]"));
            lines.removeIf(s -> s.startsWith("[Entity Batching]"));
        }

        if (CleanDebugConfig.hideLitematica) {
            lines.removeIf(s -> s.startsWith("§6[Litematica]§r"));
        }

        if (CleanDebugConfig.hideEntityCulling) {
            lines.removeIf(s -> s.startsWith("[Culling]"));
        }

        if (CleanDebugConfig.hideViaFabric) {
            lines.removeIf(s -> s.startsWith("[ViaFabric]"));
        }

        if (CleanDebugConfig.hideNoiseRouter) {
            lines.removeIf(s -> s.startsWith("NoiseRouter"));
        }
    }

    @ModifyVariable(method = "drawRightText", at = @At(value = "STORE"))
    private List<String> modifyLines(List<String> lines) {
        // using old switch without break on purpose
        switch (CleanDebugConfig.hardwareMode) {
            case NONE: {
                lines.removeIf(s -> s.startsWith("Java:"));
                lines.removeIf(s -> s.startsWith("Mem:"));
                lines.removeIf(s -> s.startsWith("Allocation rate:"));
                lines.removeIf(s -> s.startsWith("Allocated:"));
                lines.removeIf(s -> s.startsWith("Off-Heap:"));
                lines.removeIf(s -> s.startsWith("Direct Buffers:"));
            }
            case REDUCED: {
                var cpuIndex = -1;

                for (var i = 0; i < lines.size(); i++) {
                    if (lines.get(i).startsWith("CPU: ")) {
                        cpuIndex = i;
                        break;
                    }
                }

                if (cpuIndex != -1) {
                    lines.subList(cpuIndex, cpuIndex + 5).clear();
                }
            }
        }

        if (CleanDebugConfig.hideTags) {
            lines.removeIf(s -> s.startsWith("#"));
        }

        if (CleanDebugConfig.hideSodium) {
            var sodiumIndex = CleanDebug.indexOfStartingWith(lines, "§aSodium Renderer");

            if (sodiumIndex != -1) {
                lines.subList(sodiumIndex, sodiumIndex + 6).clear();
            }
        }

        if (CleanDebugConfig.hideIris) {
            lines.removeIf(s -> s.startsWith("[Iris]"));
        }

        if (CleanDebugConfig.hideDistantHorizons) {
            lines.removeIf(s -> s.startsWith("Distant Horizons"));
        }

        return lines;
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