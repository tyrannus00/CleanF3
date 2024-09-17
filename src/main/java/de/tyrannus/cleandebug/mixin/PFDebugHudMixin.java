package de.tyrannus.cleandebug.mixin;

import de.tyrannus.cleandebug.CleanDebugConfig;
import eu.ha3.presencefootsteps.PFDebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PFDebugHud.class, remap = false)
public class PFDebugHudMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(CallbackInfo ci) {
        if (CleanDebugConfig.hidePresenceFootsteps) {
            ci.cancel();
        }
    }
}