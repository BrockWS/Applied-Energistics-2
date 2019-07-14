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

package appeng.decorative.solid;


import net.minecraft.block.material.Material;
import net.minecraft.state.IProperty;
import net.minecraft.state.PropertyEnum;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;

import appeng.api.util.IOrientable;
import appeng.api.util.IOrientableBlock;
import appeng.block.AEBaseBlock;
import appeng.helpers.MetaRotation;


public class BlockQuartzPillar extends AEBaseBlock implements IOrientableBlock
{
	public static final PropertyEnum<Direction.Axis> AXIS_ORIENTATION = PropertyEnum.create( "axis", Direction.Axis.class );

	public BlockQuartzPillar()
	{
		super( Material.ROCK );
		// The upwards facing pillar is the default (i.e. for the item model)
		this.setDefaultState( this.getDefaultState().withProperty( AXIS_ORIENTATION, Direction.Axis.Y ) );
	}

	@Override
	public int getMetaFromState( final BlockState state )
	{
		return state.getValue( AXIS_ORIENTATION ).ordinal();
	}

	@Override
	public BlockState getStateFromMeta( final int meta )
	{
		// Simply use the ordinal here
		Direction.Axis axis = Direction.Axis.values()[meta];
		return this.getDefaultState().withProperty( AXIS_ORIENTATION, axis );
	}

	@Override
	protected IProperty[] getAEStates()
	{
		return new IProperty[] { AXIS_ORIENTATION };
	}

	@Override
	public boolean usesMetadata()
	{
		return true;
	}

	@Override
	public IOrientable getOrientable( final IEnviromentBlockReader w, final BlockPos pos )
	{
		return new MetaRotation( w, pos, null );
	}

}
