/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2018, AlgorithmX2, All rights reserved.
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

package appeng.util;


import java.util.Comparator;

import appeng.api.config.SortDir;
import appeng.api.storage.data.IAEFluidStack;
import appeng.util.item.AEFluidStack;


/**
 * @author BrockWS
 * @version rv6 - 22/05/2018
 * @since rv6 22/05/2018
 */
public class FluidSorters
{
	private static SortDir Direction = SortDir.ASCENDING;

	public static final Comparator<IAEFluidStack> CONFIG_BASED_SORT_BY_NAME = new Comparator<IAEFluidStack>()
	{

		@Override
		public int compare( final IAEFluidStack o1, final IAEFluidStack o2 )
		{
			if( getDirection() == SortDir.ASCENDING )
			{
				return Platform.getFluidDisplayName( o1 ).compareToIgnoreCase( Platform.getFluidDisplayName( o2 ) );
			}
			return Platform.getFluidDisplayName( o2 ).compareToIgnoreCase( Platform.getFluidDisplayName( o1 ) );
		}
	};

	public static final Comparator<IAEFluidStack> CONFIG_BASED_SORT_BY_MOD = new Comparator<IAEFluidStack>()
	{

		@Override
		public int compare( final IAEFluidStack o1, final IAEFluidStack o2 )
		{
			final AEFluidStack op1 = (AEFluidStack) o1;
			final AEFluidStack op2 = (AEFluidStack) o2;

			if( getDirection() == SortDir.ASCENDING )
			{
				return this.secondarySort( Platform.getModId( op1 ).compareToIgnoreCase( Platform.getModId( op2 ) ), o2, o1 );
			}
			return this.secondarySort( Platform.getModId( op2 ).compareToIgnoreCase( Platform.getModId( op1 ) ), o1, o2 );
		}

		private int secondarySort( final int compareToIgnoreCase, final IAEFluidStack o1, final IAEFluidStack o2 )
		{
			if( compareToIgnoreCase == 0 )
			{
				return Platform.getFluidDisplayName( o2 ).compareToIgnoreCase( Platform.getFluidDisplayName( o1 ) );
			}

			return compareToIgnoreCase;
		}
	};

	public static final Comparator<IAEFluidStack> CONFIG_BASED_SORT_BY_SIZE = new Comparator<IAEFluidStack>()
	{

		@Override
		public int compare( final IAEFluidStack o1, final IAEFluidStack o2 )
		{
			if( getDirection() == SortDir.ASCENDING )
			{
				return compareLong( o2.getStackSize(), o1.getStackSize() );
			}
			return compareLong( o1.getStackSize(), o2.getStackSize() );
		}
	};

	public static int compareInt( final int a, final int b )
	{
		if( a == b )
		{
			return 0;
		}
		if( a < b )
		{
			return -1;
		}
		return 1;
	}

	public static int compareLong( final long a, final long b )
	{
		if( a == b )
		{
			return 0;
		}
		if( a < b )
		{
			return -1;
		}
		return 1;
	}

	public static int compareDouble( final double a, final double b )
	{
		if( a == b )
		{
			return 0;
		}
		if( a < b )
		{
			return -1;
		}
		return 1;
	}

	private static SortDir getDirection()
	{
		return Direction;
	}

	public static void setDirection( final SortDir direction )
	{
		Direction = direction;
	}
}
