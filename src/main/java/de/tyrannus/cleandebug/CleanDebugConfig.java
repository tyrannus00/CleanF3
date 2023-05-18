package de.tyrannus.cleandebug;

import eu.midnightdust.lib.config.MidnightConfig;

public class CleanDebugConfig extends MidnightConfig {

    @Comment(centered = true)
    public static final String vanillaCategory = "Vanilla";

    @Entry  // Whether to display hardware information or not
    public static HardwareMode hardwareMode = HardwareMode.REDUCED;

    @Entry  // Hides the hints for the debug pie / tps graph
    public static boolean hideActiveRenderer = true;

    @Entry  // Hides the tags of the block / fluid you are looking at
    public static boolean hideTags = true;

    @Entry  // Hides the hints for the debug pie / tps graph
    public static boolean hideDebugHints = true;

    @Entry  // Only shows fluid infos when looking at a block containing a fluid
    public static boolean shyFluids = true;

    @Comment(centered = true)
    public static final String modsCategory = "Mods";

    @Entry  // Hides sodium debug features
    public static boolean hideSodium = true;

    @Entry  // Hides iris debug features
    public static boolean hideIris = false;

    @Entry
    public static boolean hideLitematica = false;

    @Entry
    public static boolean hideEntityCulling = false;

    @Entry
    public static boolean hideViaFabric = false;

    @Entry
    public static boolean hidePresenceFootsteps = false;

    public enum HardwareMode {
        ALL, REDUCED, NONE
    }
}