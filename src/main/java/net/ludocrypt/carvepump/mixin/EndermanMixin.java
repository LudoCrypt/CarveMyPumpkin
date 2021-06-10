package net.ludocrypt.carvepump.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.carvepump.blocks.CarvableBlock;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

@Mixin(EndermanEntity.class)
public class EndermanMixin {

	@Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
	private void carvemypumpkin_isPlayerStaring(PlayerEntity player, CallbackInfoReturnable<Boolean> ci) {
		ItemStack itemStack = (ItemStack) player.getInventory().armor.get(3);
		if (itemStack.getItem() instanceof BlockItem && ((BlockItem) itemStack.getItem()).getBlock() instanceof CarvableBlock) {
			ci.setReturnValue(false);
		}
	}
}
