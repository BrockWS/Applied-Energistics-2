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

package appeng.items.misc;


import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.AEApi;
import appeng.api.definitions.IMaterials;
import appeng.api.implementations.items.IGrowableCrystal;
import appeng.api.recipes.ResolverResult;
import appeng.core.localization.ButtonToolTips;
import appeng.entity.EntityGrowingCrystal;
import appeng.items.AEBaseItem;
import appeng.util.Platform;


public class ItemCrystalSeed extends AEBaseItem implements IGrowableCrystal
{

	static final int LEVEL_OFFSET = 200;
	static final int SINGLE_OFFSET = LEVEL_OFFSET * 3;

	public static final int CERTUS = 0;
	public static final int NETHER = SINGLE_OFFSET;
	public static final int FLUIX = SINGLE_OFFSET * 2;
	public static final int FINAL_STAGE = SINGLE_OFFSET * 3;

	public ItemCrystalSeed()
	{
		super(new Properties());
	}

	@Nullable
	public static ResolverResult getResolver( final int certus2 )
	{

		return AEApi.instance()
				.definitions()
				.items()
				.crystalSeed()
				.maybeStack( 1 )
				.map( crystalSeedStack ->
				{
					crystalSeedStack = newStyle( crystalSeedStack );
					String itemName = crystalSeedStack.getItem().getRegistryName().getPath();
					return new ResolverResult( itemName, crystalSeedStack.getTag() );
				} )
				.orElse( null );

	}

	private static ItemStack newStyle( final ItemStack itemStack )
	{
		getProgress( itemStack );
		return itemStack;
	}

	static int getProgress( final ItemStack is )
	{
		if( is.hasTag() )
		{
			return is.getTag().getInt( "progress" );
		}
		else
		{
			final CompoundNBT comp = Platform.openNbtData( is );
			comp.putInt( "progress", 0 );
			return 0;
		}
	}

	@Nullable
	@Override
	public ItemStack triggerGrowth( final ItemStack is )
	{
		final int newDamage = getProgress( is ) + 1;
		final IMaterials materials = AEApi.instance().definitions().materials();
		final int size = is.getCount();

		if( newDamage == CERTUS + SINGLE_OFFSET )
		{
			Optional<ItemStack> quartzStack = materials.purifiedCertusQuartzCrystal().maybeStack( size );
			if( quartzStack.isPresent() )
			{
				return quartzStack.get();
			}
		}
		if( newDamage == NETHER + SINGLE_OFFSET )
		{
			Optional<ItemStack> quartzStack = materials.purifiedNetherQuartzCrystal().maybeStack( size );
			if( quartzStack.isPresent() )
			{
				return quartzStack.get();
			}
		}
		if( newDamage == FLUIX + SINGLE_OFFSET )
		{
			Optional<ItemStack> quartzStack = materials.purifiedFluixCrystal().maybeStack( size );
			if( quartzStack.isPresent() )
			{
				return quartzStack.get();
			}
		}
		if( newDamage > FINAL_STAGE )
		{
			return ItemStack.EMPTY;
		}

		this.setProgress( is, newDamage );
		return is;
	}

	private void setProgress( final ItemStack is, final int newDamage )
	{
		final CompoundNBT comp = Platform.openNbtData( is );
		comp.putInt( "progress", newDamage );
	}

	@Override
	public float getMultiplier( final Block blk, final Material mat )
	{
		return 0.5f;
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public void addCheckedInformation( final ItemStack stack, final World world, final List<ITextComponent> lines, final ITooltipFlag advancedTooltips )
	{
		lines.add( new TranslationTextComponent(ButtonToolTips.DoesntDespawn.getUnlocalized() ));
		final int progress = getProgress( stack ) % SINGLE_OFFSET;
		lines.add( new StringTextComponent(Math.floor( (float) progress / (float) ( SINGLE_OFFSET / 100 ) ) + "%" ) );

		super.addCheckedInformation( stack, world, lines, advancedTooltips );
	}

	@Override
	public int getEntityLifespan( final ItemStack itemStack, final World world )
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public String getTranslationKey( final ItemStack is )
	{
		final int damage = getProgress( is );

		if( damage < CERTUS + SINGLE_OFFSET )
		{
			return this.getTranslationKey() + ".certus";
		}

		if( damage < NETHER + SINGLE_OFFSET )
		{
			return this.getTranslationKey() + ".nether";
		}

		if( damage < FLUIX + SINGLE_OFFSET )
		{
			return this.getTranslationKey() + ".fluix";
		}

		return this.getTranslationKey();
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}

	@Override
	public boolean isDamaged( final ItemStack stack )
	{
		return false;
	}

	@Override
	public int getMaxDamage( final ItemStack stack )
	{
		return FINAL_STAGE;
	}

	@Override
	public boolean hasCustomEntity( final ItemStack stack )
	{
		return true;
	}

	@Override
	public Entity createEntity( final World world, final Entity location, final ItemStack itemstack )
	{
		final EntityGrowingCrystal egc = new EntityGrowingCrystal( world, location.posX, location.posY, location.posZ, itemstack );

		egc.setMotion( location.getMotion() );

		// Cannot read the pickup delay of the original item, so we
		// use the pickup delay used for items dropped by a player instead
		egc.setPickupDelay( 40 );

		return egc;
	}

	@Override
	protected void getCheckedSubItems( final ItemGroup creativeTab, final NonNullList<ItemStack> itemStacks )
	{
		itemStacks.add( new ItemStack(this) );
		// lvl 0
//		itemStacks.add( newStyle( new ItemStack( this, 1, CERTUS ) ) );
//		itemStacks.add( newStyle( new ItemStack( this, 1, NETHER ) ) );
//		itemStacks.add( newStyle( new ItemStack( this, 1, FLUIX ) ) );
//
//		// lvl 1
//		itemStacks.add( newStyle( new ItemStack( this, 1, LEVEL_OFFSET + CERTUS ) ) );
//		itemStacks.add( newStyle( new ItemStack( this, 1, LEVEL_OFFSET + NETHER ) ) );
//		itemStacks.add( newStyle( new ItemStack( this, 1, LEVEL_OFFSET + FLUIX ) ) );
//
//		// lvl 2
//		itemStacks.add( newStyle( new ItemStack( this, 1, LEVEL_OFFSET * 2 + CERTUS ) ) );
//		itemStacks.add( newStyle( new ItemStack( this, 1, LEVEL_OFFSET * 2 + NETHER ) ) );
//		itemStacks.add( newStyle( new ItemStack( this, 1, LEVEL_OFFSET * 2 + FLUIX ) ) );
	}

}
