package gay.aurum.TelePads;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface JumpingActionInterface {
	void onJump(World world, BlockState state, BlockPos pos, LivingEntity entity);
}
