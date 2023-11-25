package de.tyrannus.cleandebug;

import eu.midnightdust.lib.config.MidnightConfig;

public class CleanDebugConfig extends MidnightConfig {

    @Comment(centered = true)
    public static final String vanillaCategory = "Vanilla";

    @Entry  // Whether to display hardware information or not
    public static final HardwareMode hardwareMode = HardwareMode.REDUCED;

    @Entry  // Hides the hints for the debug pie / tps graph
    public static final boolean hideActiveRenderer = true;

    @Entry  // Hides the tags of the block / fluid you are looking at
    public static final boolean hideTags = true;

    @Entry  // Hides the hints for the debug pie / tps graph
    public static final boolean hideDebugHints = true;

    @Entry  // Only shows fluid infos when looking at a block containing a fluid
    public static final boolean shyFluids = true;

    @Entry  // Hides 'Press F3 + Q for help'
    public static final boolean hideHelpShortcut = true;

    @Entry  // Hides the long noise router info
    public static final boolean hideNoiseRouter = true;

    @Comment(centered = true)
    public static final String modsCategory = "Mods";

    @Entry
    public static final boolean hideSodium = true;

    @Entry
    public static final boolean hideIris = true;

    @Entry
    public static final boolean hideLitematica = true;

    @Entry
    public static final boolean hideEntityCulling = true;

    @Entry
    public static final boolean hideViaFabric = true;

    @Entry
    public static final boolean hidePresenceFootsteps = true;

    @Entry
    public static final boolean hideDistantHorizons = true;

    @Entry
    public static final boolean hideModernFix = true;
}