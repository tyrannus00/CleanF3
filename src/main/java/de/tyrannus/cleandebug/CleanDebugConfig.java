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

    @Entry  // Hides 'Press F3 + Q for help'
    public static boolean hideHelpShortcut = true;

    @Entry  // Hides the long noise router info
    public static boolean hideNoiseRouter = true;

    @Entry
    public static boolean onlyShowNecessary = false;

    @Comment(centered = true)
    public static final String modsCategory = "Mods";

    @Entry
    public static boolean hideSodium = true;

    @Entry
    public static boolean hideIris = true;

    @Entry
    public static boolean hideLitematica = true;

    @Entry
    public static boolean hideEntityCulling = true;

    @Entry
    public static boolean hideViaFabric = true;

    @Entry
    public static boolean hidePresenceFootsteps = true;

    @Entry
    public static boolean hideDistantHorizons = true;

    @Entry
    public static boolean hideModernFix = true;

    @Entry
    public static boolean hideJourneyMap = true;

    @Entry
    public static boolean hideLambDynamicLights = true;

    @Entry
    public static boolean hideDynamicFps = true;

    @Entry
    public static boolean hideCaveDust = true;
}