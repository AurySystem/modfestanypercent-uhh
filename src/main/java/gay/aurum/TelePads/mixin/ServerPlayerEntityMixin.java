package gay.aurum.TelePads.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

	@Inject(method = "stopRiding", at= @At("HEAD")) //fix obscure vanilla bug that made telepads constantly activate, after launching out of a blast travel cannon or obscure methods of jumping off ridding entities the right way
	public void stopRiding(CallbackInfo ci) {
		((ServerPlayerEntity) (Object) this).setJumping(false);
	}
}
