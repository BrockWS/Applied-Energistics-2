/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.block.networking;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import net.minecraft.block.material.Material;

import appeng.block.AEBaseTileBlock;
import appeng.tile.networking.TileController;


public class BlockController extends AEBaseTileBlock
{

	public enum ControllerBlockState implements IStringSerializable
	{
		offline, online, conflicted;

		@Override
		public String getName()
		{
			return this.name();
		}

	}

	/**
	 * Controls the rendering of the controller block (connected texture style).
	 * inside_a and inside_b are alternating patterns for a controller that is enclosed by other controllers,
	 * and since they are always offline, they do not have the usual sub-states.
	 */
	public enum ControllerRenderType implements IStringSerializable
	{
		block, column_x, column_y, column_z, inside_a, inside_b;

		@Override
		public String getName()
		{
			return this.name();
		}

	}

	public static final EnumProperty<ControllerBlockState> CONTROLLER_STATE = EnumProperty.create( "state", ControllerBlockState.class );

	public static final EnumProperty<ControllerRenderType> CONTROLLER_TYPE = EnumProperty.create( "type", ControllerRenderType.class );

	public BlockController()
	{
		super( Properties.create(Material.IRON).hardnessAndResistance(6) );
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(CONTROLLER_STATE, CONTROLLER_TYPE);
	}

	/**
	 * This will compute the AE_BLOCK_FORWARD, AE_BLOCK_UP and CONTROLLER_TYPE block states based on adjacent
	 * controllers and the network state of this controller (offline, online, conflicted). This is used to
	 * get a rudimentary connected texture feel for the controller based on how it is placed.
	 */
	@Override
	public BlockState getStateForPlacement( BlockItemUseContext context )
	{
		return this.getStateForPlacement(context.getPos(), context.getWorld(), this.getDefaultState());
	}

	@Override
	public BlockState updatePostPlacement(BlockState currentState, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
	{
		return this.getStateForPlacement(currentPos, world, currentState); // Javadoc says that the moethod should only consider the facing block
	}

	private BlockState getStateForPlacement(BlockPos pos, IWorld world, BlockState state) {
		// Only used for columns, really
		ControllerRenderType type = ControllerRenderType.block;

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		// Detect whether controllers are on both sides of the x, y, and z axes

		final boolean xx = world.getBlockState(pos.east()).getBlock() == this && world.getBlockState(pos.west()).getBlock() == this;
		final boolean yy = world.getBlockState(pos.up()).getBlock() == this && world.getBlockState(pos.down()).getBlock() == this;
		final boolean zz = world.getBlockState(pos.north()).getBlock() == this && world.getBlockState(pos.south()).getBlock() == this;

		if( xx && !yy && !zz )
		{
			type = ControllerRenderType.column_x;
		}
		else if( !xx && yy && !zz )
		{
			type = ControllerRenderType.column_y;
		}
		else if( !xx && !yy && zz )
		{
			type = ControllerRenderType.column_z;
		}
		else if( ( xx ? 1 : 0 ) + ( yy ? 1 : 0 ) + ( zz ? 1 : 0 ) >= 2 )
		{
			final int v = ( Math.abs( x ) + Math.abs( y ) + Math.abs( z ) ) % 2;

			// While i'd like this to be based on the blockstate randomization feature, this generates
			// an alternating pattern based on world position, so this is not 100% doable with blockstates.
			if( v == 0 )
			{
				type = ControllerRenderType.inside_a;
			}
			else
			{
				type = ControllerRenderType.inside_b;
			}
		}

		return state.with(CONTROLLER_TYPE, type).with(CONTROLLER_STATE, ControllerBlockState.online);
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

//	@Override
//	public void neighborChanged( BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos )
//	{
//		final TileController tc = this.getTileEntity( world, pos );
//		if( tc != null )
//		{
//			tc.onNeighborChange( false );
//		}
//	}
}
