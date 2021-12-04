package me.tyrannus.cleandebug.mixin;

import com.google.common.collect.Lists;
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

	@ModifyVariable(method = "getRightText", at = @At("STORE"))
	private <E> List<E> hardwareInfo2(List<E> elements) {
		if(CleanDebugConfig.hideSodium) {
			for(int i = 0; i < elements.size(); i++) {
				String string = (String) elements.get(i);
				if(string.contains("Sodium Renderer")) {
					elements.subList(i - 1, i + 6).clear();
					break;
				}
			}
		}

		return switch(CleanDebugConfig.hardwareMode) {
			case ALL -> elements;
			case REDUCED -> {
				for(int i = 0; i < elements.size(); i++) {
					String string = (String) elements.get(i);
					if(string.contains("CPU: ")) {
						elements.subList(i - 1, i + 5).clear();
						break;
					}
				}
				yield elements;
			}
			case NONE -> Lists.newArrayList();
		};
	}

	@Redirect(method = "getRightText", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 1))
	protected final boolean blockTags(Iterator iterator) {
		if(CleanDebugConfig.hideTags) {
			return false;
		}
		else return iterator.hasNext();
	}

	@Redirect(method = "getRightText", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z", ordinal = 3))
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

	@Redirect(method = "renderLeftText", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
	protected final <E> boolean blockTags(List list, E e) {
		if(CleanDebugConfig.hideDebugHints) {
			return false;
		}
		return list.add(e);
	}



}
