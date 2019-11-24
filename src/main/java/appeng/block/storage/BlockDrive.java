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

package appeng.block.storage;


import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;

import appeng.api.util.AEPartLocation;
import appeng.block.AEBaseTileBlock;
import appeng.client.UnlistedProperty;
import appeng.core.sync.GuiBridge;
import appeng.tile.storage.TileDrive;
import appeng.util.Platform;


public class BlockDrive extends AEBaseTileBlock
{

	public BlockDrive()
	{
		super( Material.IRON );
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated( BlockState state, World w, BlockPos pos, PlayerEntity p, Hand hand, BlockRayTraceResult resulthitZ )
	{
		if( p.isSneaking() )
		{
			return false;
		}

//		final TileDrive tg = this.getTileEntity( w, pos );
//		if( tg != null )
//		{
//			if( Platform.isServer() )
//			{
//				Platform.openGUI( p, tg, AEPartLocation.fromFacing( side ), GuiBridge.GUI_DRIVE );
//			}
//			return true;
//		}
		return false;
	}
}
