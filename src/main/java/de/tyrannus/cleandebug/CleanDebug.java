package de.tyrannus.cleandebug;

import net.fabricmc.api.ClientModInitializer;

public class CleanDebug implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CleanDebugConfig.init("clean-debug", CleanDebugConfig.class);
    }
}