package me.tyrannus.cleandebug.mixin;

import me.tyrannus.cleandebug.config.CleanDebugConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;

@Mixin(value = DebugHud.class, priority = 900)
public class DebugHudMixin {

	@Shadow @Final private MinecraftClient client;

	@Shadow private HitResult fluidHit;

	@ModifyVariable(method = "renderRightText", at = @At("STORE"))
	private <E> List<E> renderRightText(List<E> elements) {

		int max = elements.size();
		for(int i = 0; i < max; i++) {
			String string = (String) elements.get(i);
			if(string.contains("CPU: ")) {
				if(CleanDebugConfig.hardwareMode.hideSpecs()) {
					elements.subList(i - 1, i + 5).clear();
					i--;
					max -= 6;
				}
				if(CleanDebugConfig.hardwareMode == CleanDebugConfig.HardwareMode.NONE) {
					elements.subList(0, i).clear();
					max -= i;
					i = 0;
				}
			}
			if(CleanDebugConfig.hideIris && string.contains("[Iris] ")) {
				elements.remove(i);
				i--;
				max--;
			}
			if(CleanDebugConfig.hideSodium && string.contains("Sodium Renderer")) {
				elements.subList(i - 1, i + 6).clear();
				i -= 2;
				max -= 7;
			}
		}
		if(elements.size() > 0 && ((String) elements.get(0)).isEmpty()) {
			elements.remove(0);
		}
		return elements;
	}

	@ModifyVariable(method = "renderLeftText", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
	private <E> List<E> renderLeftText(List<E> elements) {
		int max = elements.size();
		for(int i = 0; i < max; i++) {
			String string = (String) elements.get(i);
			if(CleanDebugConfig.hideIris && (string.contains("[Iris] ") || string.contains("[Entity Batching] "))) {
				elements.remove(i);
				i--;
				max--;
			}
			if(CleanDebugConfig.hideActiveRenderer && string.contains("[Fabric] Active renderer: ")) {
				elements.remove(i);
				i--;
				max--;
			}
			if(CleanDebugConfig.hideDebugHints && string.contains("Debug: Pie")) {
				elements.subList(i, i + 2).clear();
				i--;
				max -= 2;
			}
		}
		return elements;
	}

	@Redirect(method = "getRightText", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 0))
	protected final boolean blockTags(Iterator iterator) {
		if(CleanDebugConfig.hideTags) {
			return false;
		}
		else return iterator.hasNext();
	}

	@Redirect(method = "getRightText", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 1))
	protected final boolean fluidTags(Iterator iterator) {
		if(CleanDebugConfig.hideTags) {
			return false;
		}
		return iterator.hasNext();
	}

	@Redirect(method = "getRightText", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/DebugHud;fluidHit:Lnet/minecraft/util/hit/HitResult;", ordinal = 0, opcode = Opcodes.GETFIELD))
	protected final HitResult fluidVisibility(DebugHud debugHud) {
		if(CleanDebugConfig.shyFluids && client.world.getFluidState(((BlockHitResult)this.fluidHit).getBlockPos()).isEmpty()) {
			return BlockHitResult.createMissed(null, null, null);
		}
		return fluidHit;
	}



}
