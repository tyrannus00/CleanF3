package de.tyrannus.cleandebug;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class CleanDebug implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MidnightConfig.init("clean-debug", CleanDebugConfig.class);
    }

    public static void modifyLeftLines(List<String> lines) {
        lines.removeIf(Objects::isNull);

        if (CleanDebugConfig.hideActiveRenderer) {
            lines.removeIf(s -> s.startsWith("[Fabric] Active renderer:"));
        }

        if (CleanDebugConfig.hideDebugHints) {
            lines.removeIf(s -> s.contains("Debug: Pie"));
        }

        if (CleanDebugConfig.hideHelpShortcut) {
            lines.removeIf(s -> s.equals("For help: press F3 + Q"));
            lines.removeIf(s -> s.startsWith("Debug charts:"));  // since 1.20.2
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

        if (CleanDebugConfig.hideLambDynamicLights) {
            lines.removeIf(s -> s.startsWith("Dynamic Light Sources: "));
        }
    }

    public static void modifyRightLines(List<String> lines) {
        if (CleanDebugConfig.hardwareMode == HardwareMode.NONE) {
            lines.removeIf(s -> s.startsWith("Java:"));
            lines.removeIf(s -> s.startsWith("Mem:"));
            lines.removeIf(s -> s.startsWith("Allocation rate:"));
            lines.removeIf(s -> s.startsWith("Allocated:"));
            lines.removeIf(s -> s.startsWith("Off-Heap:"));
            lines.remove(0);
            lines.removeIf(s -> s.startsWith("Direct Buffers:"));
        }

        if (CleanDebugConfig.hardwareMode != HardwareMode.ALL) {
            var cpuIndex = indexOfStartingWith(lines, "CPU: ", false);

            if (cpuIndex != -1) {
                lines.subList(cpuIndex, Math.min(cpuIndex + 6, lines.size())).clear();
            }
        }

        if (CleanDebugConfig.hideTags) {
            lines.removeIf(s -> s.startsWith("#"));
        }

        if (CleanDebugConfig.hideSodium) {
            var sodiumIndex = indexOfStartingWith(lines, "§aSodium Renderer", false);

            if (sodiumIndex != -1) {
                lines.subList(sodiumIndex, Math.min(sodiumIndex + 6, lines.size())).clear();
            }
        }

        if (CleanDebugConfig.hideIris) {
            lines.removeIf(s -> s.startsWith("[Iris]"));
        }

        if (CleanDebugConfig.hideDistantHorizons) {
            var lodModIndexStart = indexOfStartingWith(lines, "Distant Horizons version: ", false);
            var lodModIndexEnd = indexOfStartingWith(lines, "ON_LOADED:", true);

            if (lodModIndexStart != -1 && lodModIndexEnd != -1) {
                lines.subList(lodModIndexStart - 1, lodModIndexEnd + 1).clear();    // Adds empty line before
            }
        }

        if (CleanDebugConfig.hideModernFix) {
            var modernFixIndex = indexOfStartingWith(lines, "ModernFix", false);

            if (modernFixIndex != -1) {
                lines.subList(modernFixIndex, Math.min(modernFixIndex + 2, lines.size())).clear();
            }
        }

        while (!lines.isEmpty() && lines.get(0).isEmpty()) {
            lines.remove(0);
        }

    }

    public static HitResult.Type getFluidHitResultType(HitResult result, World world) {
        if (CleanDebugConfig.shyFluids && result instanceof BlockHitResult blockHitResult && world.getFluidState(blockHitResult.getBlockPos()).isEmpty()) {
            return HitResult.Type.MISS;
        }

        return result.getType();
    }

    private static int indexOfStartingWith(List<String> list, String startingWith, boolean trim) {
        for (var i = 0; i < list.size(); i++) {
            var string = list.get(i);

            if ((trim ? string.trim() : string).startsWith(startingWith)) {
                return i;
            }
        }

        return -1;
    }
}