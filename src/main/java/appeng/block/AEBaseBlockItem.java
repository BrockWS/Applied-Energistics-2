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

package appeng.block;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.util.IOrientable;
import appeng.api.util.IOrientableBlock;
import appeng.block.misc.BlockLightDetector;
import appeng.block.misc.BlockSkyCompass;
import appeng.block.networking.BlockWireless;
import appeng.core.CreativeTab;
import appeng.me.helpers.IGridProxyable;
import appeng.tile.AEBaseTile;


public class AEBaseBlockItem extends BlockItem
{

	private final AEBaseBlock blockType;

	public AEBaseBlockItem( final Block id )
	{
		super( id, new Properties().group(CreativeTab.instance) );
		this.blockType = (AEBaseBlock) id;
	}

//	@Override
//	public boolean isBookEnchantable( final ItemStack itemstack1, final ItemStack itemstack2 )
//	{
//		return false;
//	}
//
//	@Override
//	public boolean placeBlock( final BlockItemUseContext context, final BlockState state )
//	{
//		Direction up = null;
//		Direction forward = null;
//
//		if( this.blockType instanceof AEBaseTileBlock )
//		{
//			if( this.blockType instanceof BlockLightDetector )
//			{
//				up = context.getFace();
//				if( up == Direction.UP || up == Direction.DOWN )
//				{
//					forward = Direction.SOUTH;
//				}
//				else
//				{
//					forward = Direction.UP;
//				}
//			}
//			else if( this.blockType instanceof BlockWireless || this.blockType instanceof BlockSkyCompass )
//			{
//				forward = context.getFace();
//				if( forward == Direction.UP || forward == Direction.DOWN )
//				{
//					up = Direction.SOUTH;
//				}
//				else
//				{
//					up = Direction.UP;
//				}
//			}
//			else
//			{
//				up = Direction.UP;
//
//				final byte rotation = (byte) ( MathHelper.floor( ( context.getPlayer().rotationYaw * 4F ) / 360F + 2.5D ) & 3 );
//
//				switch( rotation )
//				{
//					default:
//					case 0:
//						forward = Direction.SOUTH;
//						break;
//					case 1:
//						forward = Direction.WEST;
//						break;
//					case 2:
//						forward = Direction.NORTH;
//						break;
//					case 3:
//						forward = Direction.EAST;
//						break;
//				}
//
//				if( context.getPlayer().rotationPitch > 65 )
//				{
//					up = forward.getOpposite();
//					forward = Direction.UP;
//				}
//				else if( context.getPlayer().rotationPitch < -65 )
//				{
//					up = forward.getOpposite();
//					forward = Direction.DOWN;
//				}
//			}
//		}
//
//		IOrientable ori = null;
//		if( this.blockType instanceof IOrientableBlock )
//		{
//			ori = ( (IOrientableBlock) this.blockType ).getOrientable( context.getWorld(), context.getPos() );
//			up = context.getFace();
//			forward = Direction.SOUTH;
//			if( up.getXOffset() == 0 )
//			{
//				forward = Direction.UP;
//			}
//		}
//
//		if( !this.blockType.isValidOrientation( context.getWorld(), context.getPos(), forward, up ) )
//		{
//			return false;
//		}
//
//		if( super.placeBlock( context, state) )
//		{
//			if( this.blockType instanceof AEBaseTileBlock && !( this.blockType instanceof BlockLightDetector ) )
//			{
//				final AEBaseTile tile = ( (AEBaseTileBlock) this.blockType ).getTileEntity( context.getWorld(), context.getPos() );
//				ori = tile;
//
//				if( tile == null )
//				{
//					return true;
//				}
//
//				if( ori.canBeRotated() && !this.blockType.hasCustomRotation() )
//				{
//					ori.setOrientation( forward, up );
//				}
//
//				if( tile instanceof IGridProxyable )
//				{
//					( (IGridProxyable) tile ).getProxy().setOwner( context.getPlayer() );
//				}
//
//				tile.onPlacement( context.getItem(), context.getPlayer(), context.getFace() );
//			}
//			else if( this.blockType instanceof IOrientableBlock )
//			{
//				ori.setOrientation( forward, up );
//			}
//
//			return true;
//		}
//		return false;
//	}
}
