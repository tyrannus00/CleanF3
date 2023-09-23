package de.tyrannus.cleandebug;

import net.fabricmc.api.ClientModInitializer;

import java.util.List;

public class CleanDebug implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CleanDebugConfig.init("clean-debug", CleanDebugConfig.class);
    }

    public static int indexOfStartingWith(List<String> list, String startingWith) {
        for (var i = 0; i < list.size(); i++) {
            if (list.get(i).startsWith(startingWith)) {
                return i;
            }
        }

        return -1;
    }
}