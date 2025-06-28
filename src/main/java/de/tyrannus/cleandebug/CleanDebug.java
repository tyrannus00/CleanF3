package de.tyrannus.cleandebug;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Formatting;
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

    public static void modifyLeftText(List<String> text) {
        text.removeIf(Objects::isNull);    // Some mods are goofy goobers and add null

        if (CleanDebugConfig.hideActiveRenderer) {
            text.removeIf(s -> s.startsWith("[Fabric] Active renderer:"));
        }

        if (CleanDebugConfig.hideDebugHints) {
            text.removeIf(s -> s.contains("Debug: Pie"));
        }

        if (CleanDebugConfig.hideHelpShortcut) {
            text.removeIf(s -> s.equals("For help: press F3 + Q"));
            text.removeIf(s -> s.startsWith("Debug charts:"));  // since 1.20.2
        }

        if (CleanDebugConfig.hideNoiseRouter) {
            text.removeIf(s -> s.startsWith("NoiseRouter"));
        }

        if (CleanDebugConfig.onlyShowNecessary) {
            text.removeIf(
                    s -> s.startsWith("Chunks[") ||
                    s.startsWith("Block: ") ||
                    s.startsWith("Chunk: ") ||
                    s.startsWith("CH S:") ||
                    s.startsWith("SH S:") ||
                    s.startsWith("SC: ") ||
                    s.startsWith("Sounds: ") ||
                    s.startsWith("Blending: ")  // 1.21
            );
        }

        if (CleanDebugConfig.hideIris) {
            text.removeIf(s -> s.startsWith("[Iris]"));
            text.removeIf(s -> s.startsWith("[Entity Batching]"));
        }

        if (CleanDebugConfig.hideLitematica) {
            text.removeIf(s -> s.startsWith("§6[Litematica]§r"));
        }

        if (CleanDebugConfig.hideEntityCulling) {
            text.removeIf(s -> s.startsWith("[Culling]"));
        }

        if (CleanDebugConfig.hideViaFabric) {
            text.removeIf(s -> s.startsWith("[ViaFabric]"));
        }

        if (CleanDebugConfig.hideJourneyMap) {
            text.removeIf(s -> s.startsWith("§b[JM]"));
        }

        if (CleanDebugConfig.hideLambDynamicLights) {
            text.removeIf(s -> s.startsWith("Dynamic Light Sources: ")  // Backwards compatibility
                    || s.startsWith("§d[LDL]")  // 1.21.4+
            );
        }

        if (CleanDebugConfig.hideDynamicFps) {
            text.removeIf(s -> s.startsWith("§c[Dynamic FPS] "));
        }

        if (CleanDebugConfig.hideCoordinates) {
            var coordIndex = indexOfStartingWith(text, "XYZ: ");

            if (coordIndex != -1) {
                text.remove(coordIndex);
                text.add(coordIndex, "XYZ: Hidden by CleanF3");

                text.removeIf(s -> s.startsWith("Block: "));
                text.removeIf(s -> s.startsWith("Chunk: "));
            }
        }
    }

    public static void modifyRightText(List<String> text) {
        if (CleanDebugConfig.hardwareMode == HardwareMode.NONE) {
            text.removeIf(s -> s.startsWith("Java:"));
            text.removeIf(s -> s.startsWith("Mem:"));
            text.removeIf(s -> s.startsWith("Allocation rate:"));
            text.removeIf(s -> s.startsWith("Allocated:"));
            text.removeIf(s -> s.startsWith("Off-Heap:"));
            text.removeIf(s -> s.startsWith("Direct Buffers:"));

            if (!text.isEmpty() && text.get(0).isEmpty()) {     // Sodium Extras re-uses the same list of texts,
                text.remove(0);                           // so we need to add a special check to not empty the list
            }
        }

        if (CleanDebugConfig.hardwareMode != HardwareMode.ALL) {
            var cpuIndex = indexOfStartingWith(text, "CPU: ");

            if (cpuIndex != -1) {
                text.subList(cpuIndex, Math.min(cpuIndex + 6, text.size())).clear();
            }
        }

        if (CleanDebugConfig.hideTags) {
            text.removeIf(s -> s.startsWith("#"));
        }

        // Mods

        if (CleanDebugConfig.hideSodium) {
            var sodiumIndex = indexOfStartingWith(text, "§aSodium Renderer");

            if (sodiumIndex != -1) {
                text.subList(sodiumIndex, Math.min(sodiumIndex + 7, text.size())).clear();

                if (sodiumIndex > 0 && text.get(sodiumIndex - 1).isEmpty()) {
                    text.remove(sodiumIndex - 1);
                }
            }
        }

        if (CleanDebugConfig.hideIris) {
            text.removeIf(s -> s.startsWith("[Iris]"));
        }

        if (CleanDebugConfig.hideModernFix) {
            var modernFixIndex = indexOfStartingWith(text, "ModernFix");

            if (modernFixIndex != -1) {
                text.subList(modernFixIndex, Math.min(modernFixIndex + 2, text.size())).clear();
            }
        }

        if (CleanDebugConfig.hideCaveDust) {
            var caveDustIndex = indexOfStartingWith(text, "Particle amount evaluated: ");

            if (caveDustIndex != -1) {
                text.remove(caveDustIndex);

                if (caveDustIndex > 0 && text.get(caveDustIndex - 1).isEmpty()) {
                    text.remove(caveDustIndex - 1); // Blank line before
                }

                if (caveDustIndex < text.size() && text.get(caveDustIndex).isEmpty()) {
                    text.remove(caveDustIndex); // Blank line after
                }
            }
        }

        if (CleanDebugConfig.hideImmediatelyFast) {
            var immediatelyFastIndex = indexOfStartingWith(text, "ImmediatelyFast");

            if (immediatelyFastIndex != -1) {
                text.subList(immediatelyFastIndex, Math.min(immediatelyFastIndex + 2, text.size())).clear();
            }
        }

        if (CleanDebugConfig.hideCoordinates) {
            text.removeIf(s -> s.startsWith(Formatting.UNDERLINE + "Targeted Block: "));
        }

        while (!text.isEmpty() && text.get(0).isEmpty()) {
            text.remove(0);
        }
    }

    public static HitResult.Type getFluidHitResultType(HitResult result, World world) {
        if (CleanDebugConfig.shyFluids && result instanceof BlockHitResult blockHitResult && world.getFluidState(blockHitResult.getBlockPos()).isEmpty()) {
            return HitResult.Type.MISS;
        }

        return result.getType();
    }

    /**
     * Returns -1 if it can"t find a string starting with the supplied parameter in the list.
     */
    private static int indexOfStartingWith(List<String> list, String startingWith) {
        for (var i = 0; i < list.size(); i++) {
            var string = list.get(i);

            if (startingWith.isEmpty()) {
                if (string.isEmpty()) {
                    return i;
                }
            } else if (string.startsWith(startingWith)) {
                return i;
            }
        }

        return -1;
    }
}