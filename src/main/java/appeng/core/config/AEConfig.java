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

package appeng.core.config;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import appeng.api.config.PowerUnits;
import appeng.api.config.Settings;
import appeng.core.AELog;
import appeng.core.AppEng;
import appeng.core.features.AEFeature;
import appeng.core.settings.TickRates;
import appeng.integration.IntegrationType;
import appeng.util.Platform;


public final class AEConfig //extends Configuration implements IConfigurableObject, IConfigManagerHost
{

	public static final String VERSION = "@version@";
	public static final String CHANNEL = "@aechannel@";
	public static final String PACKET_CHANNEL = "AE";

	// Config instance
	private static AEConfig instance;

	// Default Grindstone ores
	private static final String[] ORES_VANILLA = { "Obsidian", "Ender", "EnderPearl", "Coal", "Iron", "Gold", "Charcoal", "NetherQuartz" };
	private static final String[] ORES_AE = { "CertusQuartz", "Wheat", "Fluix" };
	private static final String[] ORES_COMMON = { "Copper", "Tin", "Silver", "Lead", "Bronze" };
	private static final String[] ORES_MISC = { "Brass", "Platinum", "Nickel", "Invar", "Aluminium", "Electrum", "Osmium", "Zinc" };

	// Default Energy Conversion Rates
	private static final double DEFAULT_IC2_EXCHANGE = 2.0;
	private static final double DEFAULT_RF_EXCHANGE = 0.5;

//	private final IConfigManager settings = new ConfigManager( this );

	private final ForgeConfigSpec spec;
	private final CommentedFileConfig config;
	private final ForgeConfigSpec specClient;
	private final CommentedFileConfig configClient;
	private boolean updatable = false;

	// Misc
	private boolean removeCrashingItemsOnLoad = false;
	private int formationPlaneEntityLimit = 128;
	private boolean enableEffects = true;
	private boolean useLargeFonts = false;
	private boolean useColoredCraftingStatus;
	private boolean disableColoredCableRecipesInJEI = true;
	private int craftingCalculationTimePerTick = 5;
	private PowerUnits selectedPowerUnit = PowerUnits.AE;

	// GUI Buttons
	private final int[] craftByStacks = { 1, 10, 100, 1000 };
	private final int[] priorityByStacks = { 1, 10, 100, 1000 };
	private final int[] levelByStacks = { 1, 10, 100, 1000 };
	private final int[] levelByMillibuckets = { 10, 100, 1000, 10000 };

	// Spatial IO/Dimension
	private int storageProviderID = -1;
	private int storageDimensionID = -1;
	private double spatialPowerExponent = 1.35;
	private double spatialPowerMultiplier = 1250.0;

	// Grindstone
	private String[] grinderOres = Stream.of( ORES_VANILLA, ORES_AE, ORES_COMMON, ORES_MISC ).flatMap( Stream::of ).toArray( String[]::new );
	private List<String> grinderBlackList = new ArrayList<>();
	private double oreDoublePercentage = 90.0;

	// Batteries
	private int wirelessTerminalBattery = 1600000;
	private int entropyManipulatorBattery = 200000;
	private int matterCannonBattery = 200000;
	private int portableCellBattery = 20000;
	private int colorApplicatorBattery = 20000;
	private int chargedStaffBattery = 8000;

	// Certus quartz
	private float spawnChargedChance = 0.92f;
	private int quartzOresPerCluster = 4;
	private int quartzOresClusterAmount = 15;
	private int chargedChange = 4;

	// Meteors
	private int minMeteoriteDistance = 707;
	private int minMeteoriteDistanceSq = this.minMeteoriteDistance * this.minMeteoriteDistance;
	private double meteoriteClusterChance = 0.1;
	private int meteoriteMaximumSpawnHeight = 180;
	private int[] meteoriteDimensionWhitelist = { 0 };

	// Wireless
	private double wirelessBaseCost = 8;
	private double wirelessCostMultiplier = 1;
	private double wirelessTerminalDrainMultiplier = 1;
	private double wirelessBaseRange = 16;
	private double wirelessBoosterRangeMultiplier = 1;
	private double wirelessBoosterExp = 1.5;
	private double wirelessHighWirelessCount = 64;

	// Tunnels
	public static final double TUNNEL_POWER_LOSS = 0.05;

	private AEConfig()
	{
		this.spec = this.spec(new ForgeConfigSpec.Builder());
//		this.config = this.config("AppliedEnergistics2");
		this.config = this.config(AppEng.MOD_ID);
		this.config.load();
		this.spec.setConfig(this.config);

		this.specClient = this.specClient(new ForgeConfigSpec.Builder());
//		this.configClient = this.config("AppliedEnergistics2-Client");
		this.configClient = this.config(AppEng.MOD_ID + "-client");
		this.configClient.load();
		this.specClient.setConfig(this.configClient);

		AELog.info("Selected PowerUnit %s", this.selectedPowerUnit());
		this.nextPowerUnit(false);
		AELog.info("New PowerUnit %s", this.selectedPowerUnit());
		this.nextPowerUnit(true);
		AELog.info("Initial PowerUnit %s", this.selectedPowerUnit());
	}

	private CommentedFileConfig config(String name) {
		return CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(/*"AppliedEnergistics2/" + */name + ".toml"))
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();
	}

	private ForgeConfigSpec spec(ForgeConfigSpec.Builder builder) {
		builder.push("PowerRatios");
		builder.define("IC2", DEFAULT_IC2_EXCHANGE);
		builder.define("ForgeEnergy", DEFAULT_RF_EXCHANGE);
		builder.define("UsageMultiplier", 1.0);
		builder.pop();

		builder.push("Condenser");
		builder.define("MatterBalls", 256);
		builder.define("Singularity", 256000);
		builder.pop();

		builder.push("general");
		builder.comment("Will auto-remove items that crash when being loaded from storage. This will destroy those items instead of crashing the game!")
				.define("removeCrashingItemsOnLoad", false);
		builder.pop();

		builder.comment("Creates recipe of the following pattern automatically: '1 oreTYPE => 2 dustTYPE' and '(1 ingotTYPE or 1 crystalTYPE or 1 gemTYPE) => 1 dustTYPE'");
		builder.push("GrindStone");
		builder.comment("The list of types to handle. Specify without a prefix like ore or dust.");
		builder.defineList("grinderOres", Arrays.asList(this.grinderOres), o -> o instanceof String);
		builder.comment("Blacklists the exact oredict name from being handled by any recipe.");
		builder.defineList("blacklist", this.grinderBlackList, o -> o instanceof String);
		builder.comment("Chance to actually get an output with stacksize > 1.");
		builder.define("oreDoublePercentage", this.oreDoublePercentage);
		builder.pop();

		builder.push("worldgen");
		builder.define("spawnChargedChance", 1.0 - this.spawnChargedChance);
		builder.define("minMeteoriteDistance", this.minMeteoriteDistance);
		builder.define("meteoriteClusterChance", this.meteoriteClusterChance);
		builder.define("meteoriteMaximumSpawnHeight", this.meteoriteMaximumSpawnHeight);
		builder.defineList("meteoriteDimensionWhitelist", Arrays.stream(this.meteoriteDimensionWhitelist).boxed().collect(Collectors.toList()), o -> o instanceof Integer); // FIXME
		builder.define("quartzOresPerCluster", this.quartzOresPerCluster);
		builder.define("quartzOresClusterAmount", this.quartzOresClusterAmount);
		builder.pop();

		builder.comment("Range= wirelessBaseRange + wirelessBoosterRangeMultiplier * Math.pow( boosters, wirelessBoosterExp )\nPowerDrain= wirelessBaseCost + wirelessCostMultiplier * Math.pow( boosters, 1 + boosters / wirelessHighWirelessCount )");
		builder.push("wireless");
		builder.define("wirelessBaseCost", this.wirelessBaseCost);
		builder.define("wirelessCostMultiplier", this.wirelessCostMultiplier);
		builder.define("wirelessBaseRange", this.wirelessBaseRange);
		builder.define("wirelessBoosterRangeMultiplier", this.wirelessBoosterRangeMultiplier);
		builder.define("wirelessBoosterExp", this.wirelessBoosterExp);
		builder.define("wirelessTerminalDrainMultiplier", this.wirelessTerminalDrainMultiplier);
		builder.pop();

		builder.push("automation");
		builder.define("formationPlaneEntityLimit", this.formationPlaneEntityLimit);
		builder.pop();

		builder.push("battery");
		builder.define("wirelessTerminal", this.wirelessTerminalBattery);
		builder.define("chargedStaff", this.chargedStaffBattery);
		builder.define("entropyManipulator", this.entropyManipulatorBattery);
		builder.define("portableCell", this.portableCellBattery);
		builder.define("colorApplicator", this.colorApplicatorBattery);
		builder.define("matterCannon", this.matterCannonBattery);
		builder.pop();

		builder.comment("Warning: Disabling a feature may disable other features depending on it.");
		builder.push("features");
		Map<String, List<String>> categoriesFeatures = new HashMap<>();
		for(final AEFeature feature : AEFeature.values()) // In case they aren't grouped by category
		{
			if(feature.isVisible())
			{
				categoriesFeatures
						.computeIfAbsent(feature.category(), s -> new ArrayList<>())
						.add(feature.key());
			}
		}

		categoriesFeatures.forEach((category, features) -> {
			builder.push(category);
			features.forEach(s -> builder.define(s, true));
			builder.pop();
		});
		builder.pop();

		builder.push("spatialio");
		builder.define("storageProviderID", this.storageProviderID);
		builder.define("storageDimensionID", this.storageDimensionID);
		builder.define("spatialPowerMultiplier", this.spatialPowerMultiplier);
		builder.define("spatialPowerExponent", this.spatialPowerExponent);
		builder.pop();

		builder.push("craftingCPU");
		builder.define("craftingCalculationTimePerTick", this.craftingCalculationTimePerTick);
		builder.pop();

		builder.comment(" Min / Max Tickrates for dynamic ticking, most of these components also use sleeping, to prevent constant ticking, adjust with care, non standard rates are not supported or tested.");
		builder.push("tickrates");
		for( final TickRates tr : TickRates.values() )
		{
			builder.define(tr.name() + ".min", tr.getMin());
			builder.define(tr.name() + ".max", tr.getMax());
		}
		builder.pop();

		builder.comment("Valid Values are 'AUTO', 'ON', or 'OFF' - defaults to 'AUTO' ; Suggested that you leave this alone unless your experiencing an issue, or wish to disable the integration for a reason.");
		builder.push("modintegration");
		for (IntegrationType integrationType : IntegrationType.values()) {
			builder.defineEnum(integrationType.dspName.replace( " ", "" ), IntegrationStatus.AUTO, IntegrationStatus.values());
		}
		builder.pop();

		this.updatable = true;
		return builder.build();
	}

	private ForgeConfigSpec specClient(ForgeConfigSpec.Builder builder)
	{
		builder.define("disableColoredCableRecipesInJEI", true);
		builder.define("enableEffects", true);
		builder.define("useTerminalUseLargeFont",false );
		builder.define("useColoredCraftingStatus", true);
		builder.defineEnum("PowerUnit", PowerUnits.AE, PowerUnits.values());

		// load buttons..
		for( int btnNum = 0; btnNum < 4; btnNum++ )
		{
			final int buttonCap = (int) ( Math.pow( 10, btnNum + 1 ) - 1 );
			builder.comment( "Controls buttons on Crafting Screen : Capped at " + buttonCap );
			builder.define("craftAmtButton" + ( btnNum + 1 ), this.craftByStacks[btnNum] );
			builder.comment( "Controls buttons on Priority Screen : Capped at " + buttonCap );
			builder.define("priorityAmtButton" + ( btnNum + 1 ), this.priorityByStacks[btnNum]  );
			builder.comment( "Controls buttons on Level Emitter Screen : Capped at " + buttonCap );
			builder.define( "levelAmtButton" + ( btnNum + 1 ), this.levelByStacks[btnNum]  );
		}

//		for( final Settings e : this.settings.getSettings() )
//		{
//			final String Category = "Client"; // e.getClass().getSimpleName();
//			Enum<?> value = this.settings.getSetting( e );
//
//			final Property p = this.get( Category, e.name(), value.name(), this.getListComment( value ) );
//
//			try
//			{
//				value = Enum.valueOf( value.getClass(), p.getString() );
//			}
//			catch( final IllegalArgumentException er )
//			{
//				AELog.info( "Invalid value '" + p.getString() + "' for " + e.name() + " using '" + value.name() + "' instead" );
//			}
//
//			this.settings.putSetting( e, value );
//		}
		return builder.build();
	}

	public static void init()
	{
		AEConfig.instance = new AEConfig();
	}

	public static AEConfig instance()
	{
		return instance;
	}

	// Since its not easy to get primitives from the config spec

	private byte getByte(CommentedFileConfig config, String path) {
		return this.getNumber(config, path).byteValue();
	}

	private short getShort(CommentedFileConfig config, String path) {
		return this.getNumber(config, path).shortValue();
	}

	private int getInt(CommentedFileConfig config, String path) {
		return this.getNumber(config, path).intValue();
	}

	private long getLong(CommentedFileConfig config, String path) {
		return this.getNumber(config, path).longValue();
	}

	private float getFloat(CommentedFileConfig config, String path) {
		return this.getNumber(config, path).floatValue();
	}

	private double getDouble(CommentedFileConfig config, String path) {
		return this.getNumber(config, path).doubleValue();
	}

	private Number getNumber(CommentedFileConfig config, String path) {
		return config.get(path);
	}

//	private String getListComment( final Enum value )
//	{
//		String comment = null;
//
//		if( value != null )
//		{
//			final EnumSet set = EnumSet.allOf( value.getClass() );
//
//			for( final Object Oeg : set )
//			{
//				final Enum eg = (Enum) Oeg;
//				if( comment == null )
//				{
//					comment = "Possible Values: " + eg.name();
//				}
//				else
//				{
//					comment += ", " + eg.name();
//				}
//			}
//		}
//
//		return comment;
//	}

	public boolean isFeatureEnabled( final AEFeature f )
	{
		if (f == AEFeature.CORE)
			return true;
		return this.config.get("features." + f.category() + "." + f.key());
	}

	public boolean areFeaturesEnabled( Collection<AEFeature> features )
	{
		return features.stream().allMatch(this::isFeatureEnabled);
	}

	public double wireless_getDrainRate( final double range )
	{
		return this.getDouble(this.config, "wireless.wirelessTerminalDrainMultiplier") * range;
	}

	public double wireless_getMaxRange( final int boosters )
	{
		return this.getDouble(this.config, "wireless.wirelessBaseRange") + this.getDouble(this.config, "wireless.wirelessBoosterRangeMultiplier") * Math.pow( boosters, this.getDouble(this.config,"wireless.wirelessBoosterExp") );
	}

	public double wireless_getPowerDrain( final int boosters )
	{
		return this.getDouble(this.config, "wireless.wirelessBaseCost") + this.getDouble(this.config, "wireless.wirelessCostMultiplier") * Math.pow( boosters, 1 + boosters / this.getDouble(this.config, "wireless.wirelessHighWirelessCount") );
	}

	public boolean disableColoredCableRecipesInJEI()
	{
		return this.configClient.get("disableColoredCableRecipesInJEI");
	}

//	@Override
//	public void updateSetting( final IConfigManager manager, final Enum setting, final Enum newValue )
//	{
//		for( final Settings e : this.settings.getSettings() )
//		{
//			if( e == setting )
//			{
//				final String Category = "Client";
//				final Property p = this.get( Category, e.name(), this.settings.getSetting( e ).name(), this.getListComment( newValue ) );
//				p.set( newValue.name() );
//			}
//		}
//
//		if( this.updatable )
//		{
//			this.save();
//		}
//	}

//	@Override
//	public IConfigManager getConfigManager()
//	{
//		return this.settings;
//	}

	public boolean useTerminalUseLargeFont()
	{
		return this.configClient.get("useTerminalUseLargeFont");
	}

	public int craftItemsByStackAmounts( final int i )
	{
		return this.configClient.get("craftAmtButton" + (i + 1));
	}

	public int priorityByStacksAmounts( final int i )
	{
		return this.configClient.get("priorityAmtButton" + (i + 1));
	}

	public int levelByStackAmounts( final int i )
	{
		return this.configClient.get("levelAmtButton" + (i + 1));
	}

	public int levelByMillyBuckets( final int i )
	{
		return this.levelByMillibuckets[i];
	}

//	public Enum getSetting( final String category, final Class<? extends Enum> class1, final Enum myDefault )
//	{
//		final String name = class1.getSimpleName();
//		final Property p = this.get( category, name, myDefault.name() );
//
//		try
//		{
//			return (Enum) class1.getField( p.toString() ).get( class1 );
//		}
//		catch( final Throwable t )
//		{
//			// :{
//		}
//
//		return myDefault;
//	}
//
//	public void setSetting( final String category, final Enum s )
//	{
//		final String name = s.getClass().getSimpleName();
//		this.get( category, name, s.name() ).set( s.name() );
//		this.save();
//	}

	public PowerUnits selectedPowerUnit()
	{
		return this.configClient.getEnum("PowerUnit", PowerUnits.class);
	}

	public void nextPowerUnit( final boolean backwards )
	{
		PowerUnits powerUnit = Platform.rotateEnum( this.selectedPowerUnit(), backwards, Settings.POWER_UNITS.getPossibleValues() );
		this.configClient.set("PowerUnit", powerUnit);
	}

	// Getters
	public boolean isRemoveCrashingItemsOnLoad()
	{
		return this.config.get("general.removeCrashingItemsOnLoad");
	}

	public int getFormationPlaneEntityLimit()
	{
		return this.config.get("automation.formationPlaneEntityLimit");
	}

	public boolean isEnableEffects()
	{
		return this.configClient.get("enableEffects");
	}

	public boolean isUseLargeFonts()
	{
		return this.configClient.get("useTerminalUseLargeFont");
	}

	public boolean isUseColoredCraftingStatus()
	{
		return this.configClient.get("useColoredCraftingStatus");
	}

	public boolean isDisableColoredCableRecipesInJEI()
	{
		return this.configClient.get("disableColoredCableRecipesInJEI");
	}

	public int getCraftingCalculationTimePerTick()
	{
		return this.config.get("craftingCalculationTimePerTick");
	}

//	public PowerUnits getSelectedPowerUnit()
//	{
//		return this.selectedPowerUnit;
//	}

//	public int[] getCraftByStacks()
//	{
//		return this.craftByStacks;
//	}
//
//	public int[] getPriorityByStacks()
//	{
//		return this.priorityByStacks;
//	}
//
//	public int[] getLevelByStacks()
//	{
//		return this.levelByStacks;
//	}

	public int getStorageProviderID()
	{
		return this.config.get("spatialio.storageProviderID");
	}

	public int getStorageDimensionID()
	{
		return this.config.get("spatialio.storageDimensionID");
	}

	public double getSpatialPowerExponent()
	{
		return this.config.get("spatialio.spatialPowerExponent");
	}

	public double getSpatialPowerMultiplier()
	{
		return this.config.get("spatialio.spatialPowerMultiplier");
	}

	public String[] getGrinderOres()
	{
		return this.grinderOres; // TODO
	}

	public List<String> getGrinderBlackList()
	{
		return this.grinderBlackList; // TODO
	}

	public double getOreDoublePercentage()
	{
		return this.config.get("GrindStone.oreDoublePercentage");
	}

	public int getWirelessTerminalBattery()
	{
		return this.config.get("battery.wirelessTerminal");
	}

	public int getEntropyManipulatorBattery()
	{
		return this.config.get("battery.entropyManipulator");
	}

	public int getMatterCannonBattery()
	{
		return this.config.get("battery.matterCannon");
	}

	public int getPortableCellBattery()
	{
		return this.config.get("battery.portableCell");
	}

	public int getColorApplicatorBattery()
	{
		return this.config.get("battery.colorApplicator");
	}

	public int getChargedStaffBattery()
	{
		return this.config.get("battery.chargedStaff");
	}

	public float getSpawnChargedChance()
	{
		return this.config.get("worldgen.spawnChargedChance");
	}

	public int getQuartzOresPerCluster()
	{
		return this.config.get("worldgen.quartzOresPerCluster");
	}

	public int getQuartzOresClusterAmount()
	{
		return this.config.get("worldgen.quartzOresClusterAmount");
	}

//	public int getChargedChange()
//	{
//		return this.chargedChange;
//		return this.config.get("worldgen.spawnChargedChance");
//	}

	public int getMinMeteoriteDistance()
	{
		return this.config.get("worldgen.minMeteoriteDistance");
	}

	public int getMinMeteoriteDistanceSq()
	{
		return this.getMinMeteoriteDistance() * this.getMinMeteoriteDistance();
	}

	public double getMeteoriteClusterChance()
	{
		return this.config.get("worldgen.meteoriteClusterChance");
	}

	public int getMeteoriteMaximumSpawnHeight()
	{
		return this.config.get("worldgen.meteoriteMaximumSpawnHeight");
	}

	public int[] getMeteoriteDimensionWhitelist()
	{
		return this.meteoriteDimensionWhitelist;
	}

	public double getWirelessBaseCost()
	{
		return this.config.get("worldgen.wirelessBaseCost");
	}

	public double getWirelessCostMultiplier()
	{
		return this.config.get("worldgen.wirelessCostMultiplier");
	}

	public double getWirelessTerminalDrainMultiplier()
	{
		return this.config.get("worldgen.wirelessTerminalDrainMultiplier");
	}

	public double getWirelessBaseRange()
	{
		return this.config.get("worldgen.wirelessBaseRange");
	}

	public double getWirelessBoosterRangeMultiplier()
	{
		return this.config.get("worldgen.wirelessBoosterRangeMultiplier");
	}

	public double getWirelessBoosterExp()
	{
		return this.config.get("worldgen.wirelessBoosterExp");
	}

	public double getWirelessHighWirelessCount()
	{
		return this.wirelessHighWirelessCount;
	}

	public IntegrationStatus getIntegrationStatus(IntegrationType type) {
		return this.config.getEnum("modintegration." + type.dspName.replace( " ", "" ), IntegrationStatus.class);
	}

	// Setters keep visibility as low as possible.

	void setStorageProviderID( int id )
	{
		this.storageProviderID = id;
	}

	void setStorageDimensionID( int id )
	{
		this.storageDimensionID = id;
	}
}
