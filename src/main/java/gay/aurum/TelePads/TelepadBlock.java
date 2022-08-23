package gay.aurum.TelePads;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class TelepadBlock extends PillarBlock implements JumpingActionInterface {

	public static final int MAX_DIST = 40; // make this a config when modfest is over
	public static final VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 3.0, 15.0);

	public TelepadBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onJump(World world, BlockState state, BlockPos pos, LivingEntity entity) {
		if(entity instanceof PlayerEntity player ) {
			Direction dir;
			Direction.Axis axis = state.get(AXIS);
				if(player.getHorizontalFacing().getAxis() == axis){
					dir = player.getHorizontalFacing();
				}else{
					dir = Direction.from(axis, Direction.AxisDirection.POSITIVE);
				}
			BlockPos.Mutable mutpos = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
				doTeleport(world,mutpos,state, entity, dir);
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if(!world.isClient()){ //this shouldn't be called on the client side anyways but just encase
			if(((CoolDownDuck)entity).goldtelepads$getCooldown() <= 0){
				Direction.Axis axis = state.get(AXIS);
				BlockPos.Mutable mutpos = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
				if(entity instanceof PlayerEntity player ){
					Direction dir;
					if(player.isSneaking()){
						if(player.getHorizontalFacing().getAxis() == axis){
							dir = player.getHorizontalFacing().getOpposite();
						}else{
							dir = Direction.from(axis, Direction.AxisDirection.NEGATIVE);
						}
						doTeleport(world,mutpos,state, entity, dir);
					}
				}else{
					if(entity.getVelocity().lengthSquared() > 0.1f){
						Direction dir;
						switch (axis){
							case X :{
								double x = entity.getVelocity().getX();
								dir = Direction.from(axis, x>0? Direction.AxisDirection.POSITIVE: Direction.AxisDirection.NEGATIVE);
								break;
							}
							default:
							case Y: {
								float pitch = entity.getPitch();
								dir = Direction.from(axis, pitch<0? Direction.AxisDirection.POSITIVE: Direction.AxisDirection.NEGATIVE);
								break;
							}
							case Z:{
								double z =entity.getVelocity().getZ();
								dir = Direction.from(axis, z>0? Direction.AxisDirection.POSITIVE: Direction.AxisDirection.NEGATIVE);

								break;
							}
						}
						doTeleport(world, mutpos, state, entity, dir);
					}
				}
			}
		}
		super.onSteppedOn(world, pos, state, entity);
	}

	public void doTeleport(World world, BlockPos.Mutable pos, BlockState state, Entity entity, Direction dir) {
		double x = entity.getX() - pos.getX();
		double y = entity.getY() - pos.getY();
		double z = entity.getZ() - pos.getZ();
		for (int i = 1; i <= MAX_DIST; i++) {
			pos.move(dir);
			if(world.getBlockState(pos).isOf(state.getBlock())){
				if(world.getBlockState(pos).get(AXIS) == dir.getAxis()) { //this is just frazzeled design decisions, not a bug, but we'll change it after modfest is over
					if (entity instanceof PlayerEntity player) {
						player.teleport(x + (double) pos.getX(), y + (double) pos.getY(), z + (double) pos.getZ());
					} else {
						entity.setPosition(x + (double) pos.getX(), y + (double) pos.getY(), z + (double) pos.getZ());
					}
					((CoolDownDuck)entity).goldtelepads$setCooldown(12);// also not a bug but the cooldown feels slightly too long
					break;
				}
			}
		}
	}// we forgot to make it teleport you to max dist if it can't find a pad, well it's not really a bug so it stays in


	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForNeighborUpdate(
			BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
	) {
		return !state.canPlaceAt(world, pos)
				? Blocks.AIR.getDefaultState()
				: super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}


	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return hasTopRim(world, pos.down()); //fixed placement bug that let you see the untextured underside
	}
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getSide();
		return this.getDefaultState().with(AXIS, direction == Direction.DOWN || (direction == Direction.UP ) ? Direction.Axis.Y : ctx.getPlayerFacing().getAxis()); //this is unforantly just obnoxious and not a bug
	}
}
