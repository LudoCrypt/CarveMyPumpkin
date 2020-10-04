package net.ludocrypt.carvepump.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.carvepump.CarveMyPumpkin;
import net.minecraft.block.Blocks;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(EndermanEntity.class)
public class EndermanMixin {

	@Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
	private void isPlayerStaring(PlayerEntity player, CallbackInfoReturnable<Boolean> ci) {
		ItemStack itemStack = (ItemStack) player.inventory.armor.get(3);
		if (itemStack.getItem() == Blocks.CARVED_PUMPKIN.asItem()
				|| itemStack.getItem() == CarveMyPumpkin.CARVED_PUMPKIN.asItem()) {
			ci.setReturnValue(false);
		}
	}
}
