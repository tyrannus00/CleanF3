package de.tyrannus.cleandebug.mixins;

import de.tyrannus.cleandebug.CleanDebugConfig;
import eu.ha3.presencefootsteps.PFDebugHud;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(value = PFDebugHud.class, remap = false)
public class PFDebugHudMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(HitResult blockHit, HitResult fluidHit, List<String> list, CallbackInfo ci) {
        if (CleanDebugConfig.hidePresenceFootsteps) {
            ci.cancel();
        }
    }
}