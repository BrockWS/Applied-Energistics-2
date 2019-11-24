/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2015, AlgorithmX2, All rights reserved.
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

package appeng.items.parts;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import appeng.api.AEApi;
import appeng.api.implementations.items.IItemGroup;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import appeng.api.util.AEColor;
import appeng.core.features.ActivityState;
import appeng.core.features.ItemStackSrc;
import appeng.core.localization.GuiText;
import appeng.items.AEBaseItem;


public final class ItemPart extends AEBaseItem implements IPartItem, IItemGroup
{
	public final PartType type;
	public final AEColor color;

	public ItemPart(PartType type, AEColor color)
	{
		super(new Properties());
		this.type = type != null ? type : PartType.INVALID_TYPE;
		this.color = color;
	}

	@Override
	public ActionResultType onItemUse( final ItemUseContext context )
	{
		if( this.type == PartType.INVALID_TYPE )
		{
			return ActionResultType.FAIL;
		}

		return AEApi.instance().partHelper().placeBus( context.getItem(), context.getPos(), context.getFace(), context.getPlayer(), context.getHand(), context.getWorld() );
	}

//	@Override
//	public String getUnlocalizedName( final ItemStack is )
//	{
//		Preconditions.checkNotNull( is );
//		return "item.appliedenergistics2.multi_part." + this.getTypeByStack( is ).getUnlocalizedName().toLowerCase();
//	}

//	@Override
//	public String getItemStackDisplayName( final ItemStack is )
//	{
//		final PartType pt = this.getTypeByStack( is );
//
//		if( pt.isCable() )
//		{
//			final AEColor[] variants = AEColor.values();
//
//			final int itemDamage = is.getItemDamage();
//			final PartTypeWithVariant registeredPartType = this.registered.get( itemDamage );
//			if( registeredPartType != null )
//			{
//				return super.getItemStackDisplayName( is ) + " - " + variants[registeredPartType.variant].toString();
//			}
//		}
//
//		if( pt.getExtraName() != null )
//		{
//			return super.getItemStackDisplayName( is ) + " - " + pt.getExtraName().getLocal();
//		}
//
//		return super.getItemStackDisplayName( is );
//	}

	@Override
	protected void getCheckedSubItems( final ItemGroup creativeTab, final NonNullList<ItemStack> itemStacks )
	{
//		final List<Entry<Integer, PartTypeWithVariant>> types = new ArrayList<>( this.registered.entrySet() );
//
//		for( final Entry<Integer, PartTypeWithVariant> part : types )
//		{
//			itemStacks.add( new ItemStack( this, 1, part.getKey() ) );
//		}
		itemStacks.add(new ItemStack(this));
	}

	@Nullable
	@Override
	public IPart createPartFromItemStack( final ItemStack is )
	{
		final Class<? extends IPart> part = this.type.getPart();
		if( part == null )
		{
			return null;
		}

		try
		{
			if( this.type.getConstructor() == null )
			{
				this.type.setConstructor( part.getConstructor( ItemStack.class ) );
			}

			return this.type.getConstructor().newInstance( is );
		}
		catch( final InstantiationException e )
		{
			throw new IllegalStateException( "Unable to construct IBusPart from IBusItem : " + part
					.getName() + " ; Possibly didn't have correct constructor( ItemStack )", e );
		}
		catch( final IllegalAccessException e )
		{
			throw new IllegalStateException( "Unable to construct IBusPart from IBusItem : " + part
					.getName() + " ; Possibly didn't have correct constructor( ItemStack )", e );
		}
		catch( final InvocationTargetException e )
		{
			throw new IllegalStateException( "Unable to construct IBusPart from IBusItem : " + part
					.getName() + " ; Possibly didn't have correct constructor( ItemStack )", e );
		}
		catch( final NoSuchMethodException e )
		{
			throw new IllegalStateException( "Unable to construct IBusPart from IBusItem : " + part
					.getName() + " ; Possibly didn't have correct constructor( ItemStack )", e );
		}
	}

	@Nullable
	@Override
	public String getUnlocalizedGroupName( final Set<ItemStack> others, final ItemStack is )
	{
		boolean importBus = false;
		boolean importBusFluids = false;
		boolean exportBus = false;
		boolean exportBusFluids = false;
		boolean group = false;

		for( final ItemStack stack : others )
		{
			if( stack.getItem() instanceof ItemPart )
			{
				final PartType pt = ((ItemPart) stack.getItem()).type;
				switch( pt )
				{
					case IMPORT_BUS:
						importBus = true;
						if( this.type == pt )
						{
							group = true;
						}
						break;
					case FLUID_IMPORT_BUS:
						importBusFluids = true;
						if( this.type == pt )
						{
							group = true;
						}
						break;
					case EXPORT_BUS:
						exportBus = true;
						if( this.type == pt )
						{
							group = true;
						}
						break;
					case FLUID_EXPORT_BUS:
						exportBusFluids = true;
						if( this.type == pt )
						{
							group = true;
						}
						break;
					default:
				}
			}
		}

		if( group && importBus && exportBus && ( this.type == PartType.IMPORT_BUS || this.type == PartType.EXPORT_BUS ) )
		{
			return GuiText.IOBuses.getUnlocalized();
		}
		if( group && importBusFluids && exportBusFluids && ( this.type == PartType.FLUID_IMPORT_BUS || this.type == PartType.FLUID_EXPORT_BUS ) )
		{
			return GuiText.IOBusesFluids.getUnlocalized();
		}

		return null;
	}

}
