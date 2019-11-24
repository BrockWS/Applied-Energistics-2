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

package appeng.core;


import appeng.api.IAppEngApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.features.IRegistryContainer;
import appeng.api.networking.IGridHelper;
import appeng.api.parts.IPartHelper;
import appeng.api.storage.IStorageHelper;
import appeng.api.util.IClientHelper;
import appeng.core.api.ApiClientHelper;
import appeng.core.api.ApiGrid;
import appeng.core.api.ApiPart;
import appeng.core.api.ApiStorage;
import appeng.core.features.registries.PartModels;
import appeng.core.features.registries.RegistryContainer;


public final class Api implements IAppEngApi
{
	public static final Api INSTANCE = new Api();

	private ApiPart partHelper;

	// private MovableTileRegistry MovableRegistry = new MovableTileRegistry();
	private IRegistryContainer registryContainer;
	private IStorageHelper storageHelper;
	private IGridHelper networkHelper;
	private ApiDefinitions definitions;
	private IClientHelper client;

	private Api()
	{
		AELog.info("Initialization Api");
//		this.storageHelper = new ApiStorage();
//		this.networkHelper = new ApiGrid();
		this.registryContainer = new RegistryContainer();
		this.partHelper = new ApiPart();
		this.definitions = new ApiDefinitions( (PartModels) this.registryContainer.partModels() );
//		this.definitions = new ApiDefinitions( null );
//		this.client = new ApiClientHelper();
	}

	public PartModels getPartModels()
	{
		return (PartModels) this.registryContainer.partModels();
	}

	@Override
	public IRegistryContainer registries()
	{
		return this.registryContainer;
	}

	@Override
	public IStorageHelper storage()
	{
		return this.storageHelper;
	}

	@Override
	public IGridHelper grid()
	{
		return this.networkHelper;
	}

	@Override
	public IPartHelper partHelper()
	{
		return this.partHelper;
	}

	@Override
	public IDefinitions definitions()
	{
		return this.definitions;
	}

	@Override
	public IClientHelper client()
	{
		return this.client;
	}
}
