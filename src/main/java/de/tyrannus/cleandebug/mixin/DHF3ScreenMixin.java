package de.tyrannus.cleandebug.mixin;

import com.seibel.distanthorizons.core.logging.f3.F3Screen;
import de.tyrannus.cleandebug.CleanDebugConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(F3Screen.class)
public class DHF3ScreenMixin {

    @Inject(method = "addStringToDisplay", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onAddString(List<String> messageList, CallbackInfo ci) {
        if (CleanDebugConfig.hideDistantHorizons) {
            ci.cancel();
        }
    }
}
