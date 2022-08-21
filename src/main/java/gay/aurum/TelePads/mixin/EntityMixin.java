package gay.aurum.TelePads.mixin;

import gay.aurum.TelePads.CoolDownDuck;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements CoolDownDuck {
	int goldtelepads$cooldownTicks = 0;

	@Override
	public int goldtelepads$getCooldown() {
		return this.goldtelepads$cooldownTicks;
	}

	@Override
	public void goldtelepads$setCooldown(int ticks) {
		this.goldtelepads$cooldownTicks = ticks;
	}

	@Inject(method = "baseTick", at = @At("HEAD"))
	public void goldtelepads$tickCooldown(CallbackInfo ci) {
		if(this.goldtelepads$cooldownTicks > 0){
			this.goldtelepads$cooldownTicks--;
		}
	}
}
