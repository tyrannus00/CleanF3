package me.tyrannus.cleandebug.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class CleanDebugConfig extends MidnightConfig {

    public enum HardwareMode {ALL, REDUCED, NONE}

    @Entry  //Whether to display hardware information or not
    public static HardwareMode hardwareMode = HardwareMode.REDUCED;

    @Entry  //Hides the tags of the block / fluid you are looking at
    public static boolean hideTags = true;

    @Entry  //Hides the hints for the debug pie / tps graph
    public static boolean hideDebugHints = true;

    @Entry  //Hides the hints for the debug pie / tps graph
    public static boolean shyFluids = true;

    @Entry  //Hides sodium debug features
    public static boolean hideSodium = true;

}
