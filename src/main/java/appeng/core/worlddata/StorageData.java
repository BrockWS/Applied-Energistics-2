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

package appeng.core.worlddata;


import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.base.Preconditions;

import appeng.core.AELog;
import appeng.me.GridStorage;
import appeng.me.GridStorageSearch;


/**
 * @author thatsIch
 * @version rv3 - 30.05.2015
 * @since rv3 30.05.2015
 */
final class StorageData implements IWorldGridStorageData, IOnWorldStartable, IOnWorldStoppable
{
	private static final String LAST_GRID_STORAGE_CATEGORY = "Counters";
	private static final String LAST_GRID_STORAGE_KEY = "lastGridStorage";
	private static final int LAST_GRID_STORAGE_DEFAULT = 0;

	private static final String GRID_STORAGE_CATEGORY = "gridstorage";

	private final Map<GridStorageSearch, WeakReference<GridStorageSearch>> loadedStorage = new WeakHashMap<>( 10 );
	private final CommentedFileConfig config;

	private long lastGridStorage;

	public StorageData( @Nonnull final CommentedFileConfig settingsFile )
	{
		Preconditions.checkNotNull( settingsFile );

		this.config = settingsFile;
	}

	/**
	 * lazy loading, can load any id, even ones that don't exist anymore.
	 *
	 * @param storageID ID of grid storage
	 *
	 * @return corresponding grid storage
	 */
	@Nullable
	@Override
	public GridStorage getGridStorage( final long storageID )
	{
		final GridStorageSearch gss = new GridStorageSearch( storageID );
		final WeakReference<GridStorageSearch> result = this.loadedStorage.get( gss );

		if( result == null || result.get() == null )
		{
			final String id = String.valueOf( storageID );
			final String data = this.config.get( "gridstorage" + "." + id );
			final GridStorage thisStorage = new GridStorage( data, storageID, gss );
			gss.setGridStorage( new WeakReference<>( thisStorage ) );
			this.loadedStorage.put( gss, new WeakReference<>( gss ) );
			return thisStorage;
		}

		return result.get().getGridStorage().get();
	}

	/**
	 * create a new storage
	 */
	@Nonnull
	@Override
	public GridStorage getNewGridStorage()
	{
		final long storageID = this.nextGridStorage();
		final GridStorageSearch gss = new GridStorageSearch( storageID );
		final GridStorage newStorage = new GridStorage( storageID, gss );
		gss.setGridStorage( new WeakReference<>( newStorage ) );
		this.loadedStorage.put( gss, new WeakReference<>( gss ) );

		return newStorage;
	}

	@Override
	public long nextGridStorage()
	{
		final long r = this.lastGridStorage;
		this.lastGridStorage++;
		this.config.set( "Counters" + "." + "lastGridStorage", this.lastGridStorage );
		return r;
	}

	@Override
	public void destroyGridStorage( final long id )
	{
		final String stringID = String.valueOf( id );
		this.config.remove( "gridstorage" + "." + stringID );
	}

	@Override
	public int getNextOrderedValue( final String name )
	{
		final int myValue = this.config.get( "orderedValues" +"."+ name );
		this.config.set("orderedValues" +"."+ name, myValue + 1);
		return myValue;
	}

	@Override
	public void onWorldStart()
	{
		final String lastString = this.config.get( LAST_GRID_STORAGE_CATEGORY +"."+ LAST_GRID_STORAGE_KEY );

		try
		{
			this.lastGridStorage = Long.parseLong( lastString );
		}
		catch( final NumberFormatException err )
		{
			AELog.warn( "The config contained a value which was not represented as a Long: %s", lastString );

			this.lastGridStorage = 0;
		}
	}

	@Override
	public void onWorldStop()
	{
		// populate new data
		for( final GridStorageSearch gs : this.loadedStorage.keySet() )
		{
			final GridStorage thisStorage = gs.getGridStorage().get();
			if( thisStorage != null && thisStorage.getGrid() != null && !thisStorage.getGrid().isEmpty() )
			{
				final String value = thisStorage.getValue();
				this.config.set( GRID_STORAGE_CATEGORY +"."+  String.valueOf( thisStorage.getID() ), value );
			}
		}

		this.config.save();
	}
}
