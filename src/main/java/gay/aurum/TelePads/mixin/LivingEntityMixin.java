package gay.aurum.TelePads.mixin;


import gay.aurum.TelePads.JumpingActionInterface;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
 abstract public class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "jump", at = @At("HEAD"))
	protected void goldtelepads$jump(CallbackInfo ci) {
		World world = this.getWorld();
		if(!world.isClient()){
			Vec3d pos = this.getPos();
			BlockPos blockpos = new BlockPos(pos);
			BlockState state = world.getBlockState(blockpos);
			if(state.getBlock() instanceof JumpingActionInterface jmp){
				jmp.onJump(world, state, blockpos, ((LivingEntity)(Object)(this)));
			}
		}
	}
}
