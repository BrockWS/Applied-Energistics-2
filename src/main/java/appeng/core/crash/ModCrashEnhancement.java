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

package appeng.core.crash;


import net.minecraftforge.versions.forge.ForgeVersion;

import appeng.core.config.AEConfig;


public class ModCrashEnhancement extends BaseCrashEnhancement
{
	private static final String MOD_VERSION = AEConfig.CHANNEL + ' ' + AEConfig.VERSION + " for Forge " + // WHAT?
			ForgeVersion.getGroup() + '|' // majorVersion
			+ ForgeVersion.getSpec() + '|' // minorVersion
			+ ForgeVersion.getTarget() + '|' // revisionVersion
			+ ForgeVersion.getVersion() + '|'
			+ ForgeVersion.getStatus();

	public ModCrashEnhancement( final CrashInfo output )
	{
		super( "AE2 Version", MOD_VERSION );
	}
}
