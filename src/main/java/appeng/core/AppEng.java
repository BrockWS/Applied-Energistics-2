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

package appeng.core;


import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.biome.Biome;

import net.minecraftforge.fml.CrashReportExtender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

import appeng.api.AEApi;
import appeng.core.crash.CrashInfo;
import appeng.core.crash.IntegrationCrashEnhancement;
import appeng.core.crash.ModCrashEnhancement;
import appeng.core.features.AEFeature;
import appeng.core.stats.AdvancementTriggers;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.worlddata.WorldData;
import appeng.hooks.TickHandler;
import appeng.integration.IntegrationRegistry;
import appeng.integration.IntegrationType;
import appeng.server.AECommand;
import appeng.services.VersionChecker;
import appeng.services.export.ExportConfig;
import appeng.services.export.ExportProcess;
import appeng.services.export.ForgeExportConfig;
import appeng.services.version.VersionCheckerConfig;
import appeng.util.Platform;


@Mod( AppEng.MOD_ID )
public final class AppEng
{
	//FIXME: @SidedProxy( clientSide = "appeng.client.ClientHelper", serverSide = "appeng.server.ServerHelper", modId = AppEng.MOD_ID )
	public static CommonHelper proxy;

	public static final String MOD_ID = "appliedenergistics2";
	public static final String MOD_NAME = "Applied Energistics 2";

	public static final String ASSETS = "appliedenergistics2:";

	@Nonnull
	private static AppEng INSTANCE;

	private final Registration registration;

	private File configDirectory;

	/**
	 * determined in pre-init but used in init
	 */
	private ExportConfig exportConfig;

	public AppEng()
	{
		INSTANCE = this;

		CrashReportExtender.registerCrashCallable( new ModCrashEnhancement( CrashInfo.MOD_VERSION ) );

		this.registration = new Registration();
		MinecraftForge.EVENT_BUS.register( this.registration );
	}

	@Nonnull
	public static AppEng instance()
	{
		return INSTANCE;
	}

	public Biome getStorageBiome()
	{
		return this.registration.storageBiome;
	}

	public DimensionType getStorageDimensionType()
	{
		return this.registration.storageDimensionType;
	}

	public int getStorageDimensionID()
	{
		return this.registration.storageDimensionID;
	}

	public AdvancementTriggers getAdvancementTriggers()
	{
		return this.registration.advancementTriggers;
	}

	private void preInit()
	{
		final Stopwatch watch = Stopwatch.createStarted();
		/* TODO: Config
		this.configDirectory = new File( event.getModConfigurationDirectory().getPath(), "AppliedEnergistics2" );

		final File configFile = new File( this.configDirectory, "AppliedEnergistics2.cfg" );
		final File facadeFile = new File( this.configDirectory, "Facades.cfg" );
		final File versionFile = new File( this.configDirectory, "VersionChecker.cfg" );
		final File recipeFile = new File( this.configDirectory, "CustomRecipes.cfg" );
		final Configuration recipeConfiguration = new Configuration( recipeFile );

		AEConfig.init( configFile );
		FacadeConfig.init( facadeFile );

		final VersionCheckerConfig versionCheckerConfig = new VersionCheckerConfig( versionFile );
		this.exportConfig = new ForgeExportConfig( recipeConfiguration ); */

		AELog.info( "Pre Initialization ( started )" );

		CreativeTab.init();
		if( AEConfig.instance().isFeatureEnabled( AEFeature.FACADES ) )
		{
			CreativeTabFacade.init();
		}

		for( final IntegrationType type : IntegrationType.values() )
		{
			IntegrationRegistry.INSTANCE.add( type );
		}

		// TODO preInit Registration
		//this.registration.preInitialize( event );

		if( Platform.isClient() )
		{
			AppEng.proxy.preinit();
		}

		IntegrationRegistry.INSTANCE.preInit();

		/* TODO: Version Checker
		if( versionCheckerConfig.isVersionCheckingEnabled() )
		{
			final VersionChecker versionChecker = new VersionChecker( versionCheckerConfig );
			final Thread versionCheckerThread = new Thread( versionChecker );

			this.startService( "AE2 VersionChecker", versionCheckerThread );
		}*/

		AELog.info( "Pre Initialization ( ended after " + watch.elapsed( TimeUnit.MILLISECONDS ) + "ms )" );

		// Instantiate all Plugins
		List<Object> injectables = Lists.newArrayList(
				AEApi.instance() );
		// TODO: PluginLoader
		//new PluginLoader().loadPlugins( injectables, event.getAsmData() );
	}

	private void startService( final String serviceName, final Thread thread )
	{
		thread.setName( serviceName );
		thread.setPriority( Thread.MIN_PRIORITY );

		AELog.info( "Starting " + serviceName );
		thread.start();
	}

	private void init( final FMLCommonSetupEvent event )
	{
		final Stopwatch start = Stopwatch.createStarted();
		AELog.info( "Initialization ( started )" );

		AppEng.proxy.init();

		/* TODO: Item.csv Exporting
		if( this.exportConfig.isExportingItemNamesEnabled() )
		{
			if( FMLCommonHandler.instance().getSide().isClient() )
			{
				final ExportProcess process = new ExportProcess( this.configDirectory, this.exportConfig );
				final Thread exportProcessThread = new Thread( process );

				this.startService( "AE2 CSV Export", exportProcessThread );
			}
			else
			{
				AELog.info( "Disabling item.csv export for custom recipes, since creative tab information is only available on the client." );
			}
		}*/

		// TODO init Registration
		//this.registration.initialize( event, this.configDirectory );
		IntegrationRegistry.INSTANCE.init();

		AELog.info( "Initialization ( ended after " + start.elapsed( TimeUnit.MILLISECONDS ) + "ms )" );
	}

	private void postInit( final FMLLoadCompleteEvent event )
	{
		final Stopwatch start = Stopwatch.createStarted();
		AELog.info( "Post Initialization ( started )" );

		// TODO postInit Registration
		//this.registration.postInit( event );
		IntegrationRegistry.INSTANCE.postInit();
		CrashReportExtender.registerCrashCallable( new IntegrationCrashEnhancement() );

		AppEng.proxy.postInit();
		AEConfig.instance().save();

		//NetworkRegistry.INSTANCE.registerGuiHandler( this, GuiBridge.GUI_Handler );
		NetworkHandler.init( "AE2" );

		AELog.info( "Post Initialization ( ended after " + start.elapsed( TimeUnit.MILLISECONDS ) + "ms )" );
	}

	private void handleIMCEvent( final InterModProcessEvent event )
	{
		//final IMCHandler imcHandler = new IMCHandler();
		// TODO IMC Events
		//imcHandler.handleIMCEvent( event );
	}

	/* TODO Server Events & Commands
	@EventHandler
	private void serverAboutToStart( final FMLServerAboutToStartEvent evt )
	{
		WorldData.onServerAboutToStart( evt.getServer() );
	}

	@EventHandler
	private void serverStopping( final FMLServerStoppingEvent event )
	{
		WorldData.instance().onServerStopping();
	}

	@EventHandler
	private void serverStopped( final FMLServerStoppedEvent event )
	{
		WorldData.instance().onServerStoppped();
		TickHandler.INSTANCE.shutdown();
	}

	@EventHandler
	private void serverStarting( final FMLServerStartingEvent evt )
	{
		evt.registerServerCommand( new AECommand( evt.getServer() ) );
	}*/
}
