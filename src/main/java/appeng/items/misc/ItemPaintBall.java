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


import javax.annotation.Nonnull;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import appeng.api.util.AEColor;
import appeng.core.CreativeTab;
import appeng.core.localization.GuiText;

import net.minecraft.item.Item;

public class ItemPaintBall extends Item {

    private final AEColor color;
    private final boolean isLumen;

	public ItemPaintBall(AEColor color, boolean isLumen) {
	    super(new Item.Properties().group(CreativeTab.instance));
	    this.color = color;
	    this.isLumen = isLumen;
		//this.setHasSubtypes( true );
	}

    @Nonnull
    public AEColor getColor() {
        return this.color;
    }

    public boolean isLumen() {
        return this.isLumen;
    }

    @Nonnull
    public static AEColor getColor(ItemStack stack) {
	    if (!(stack.getItem() instanceof ItemPaintBall))
	        return AEColor.TRANSPARENT;
	    ItemPaintBall item = (ItemPaintBall) stack.getItem();
	    return item.getColor();
    }

    public static boolean isLumen(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemPaintBall))
            return false;
        ItemPaintBall item = (ItemPaintBall) stack.getItem();
        return item.isLumen();
    }

//
//	@Override
//	public String getItemStackDisplayName( final ItemStack is )
//	{
//		return super.getItemStackDisplayName( is ) + " - " + this.getExtraName( is );
//	}
//
//	private String getExtraName( final ItemStack is )
//	{
//		return ( is.getItemDamage() >= DAMAGE_THRESHOLD ? GuiText.Lumen.getLocal() + ' ' : "" ) + this.getColor( is );
//	}
}
