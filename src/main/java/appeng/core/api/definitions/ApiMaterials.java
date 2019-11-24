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

package appeng.core.api.definitions;


import java.util.Arrays;
import java.util.stream.Collectors;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.definitions.IItemDefinition;
import appeng.api.definitions.IMaterials;
import appeng.bootstrap.FeatureFactory;
import appeng.bootstrap.IItemRendering;
import appeng.bootstrap.ItemRenderingCustomizer;
import appeng.bootstrap.components.IEntityRegistrationComponent;
import appeng.core.AELog;
import appeng.core.CreativeTab;
import appeng.core.features.AEFeature;
import appeng.core.features.DamagedItemDefinition;
import appeng.core.features.ItemDefinition;
import appeng.entity.EntityChargedQuartz;
import appeng.entity.EntityIds;
import appeng.entity.EntitySingularity;
import appeng.items.materials.ItemMaterial;
import appeng.items.materials.MaterialType;


/**
 * Internal implementation for the API materials
 */
public final class ApiMaterials implements IMaterials
{
	private final IItemDefinition cell2SpatialPart;
	private final IItemDefinition cell16SpatialPart;
	private final IItemDefinition cell128SpatialPart;

    private final IItemDefinition silicon;
	private final IItemDefinition skyDust;

	private final IItemDefinition calcProcessorPress;
	private final IItemDefinition engProcessorPress;
	private final IItemDefinition logicProcessorPress;

	private final IItemDefinition calcProcessorPrint;
	private final IItemDefinition engProcessorPrint;
	private final IItemDefinition logicProcessorPrint;

	private final IItemDefinition siliconPress;
	private final IItemDefinition siliconPrint;

	private final IItemDefinition namePress;

	private final IItemDefinition logicProcessor;
	private final IItemDefinition calcProcessor;
	private final IItemDefinition engProcessor;

	private final IItemDefinition basicCard;
	private final IItemDefinition advCard;

	private final IItemDefinition purifiedCertusQuartzCrystal;
	private final IItemDefinition purifiedNetherQuartzCrystal;
	private final IItemDefinition purifiedFluixCrystal;

	private final IItemDefinition cell1kPart;
	private final IItemDefinition cell4kPart;
	private final IItemDefinition cell16kPart;
	private final IItemDefinition cell64kPart;
	private final IItemDefinition emptyStorageCell;

	private final IItemDefinition cardRedstone;
	private final IItemDefinition cardSpeed;
	private final IItemDefinition cardCapacity;
	private final IItemDefinition cardFuzzy;
	private final IItemDefinition cardInverter;
	private final IItemDefinition cardCrafting;

	private final IItemDefinition enderDust;
	private final IItemDefinition flour;
	private final IItemDefinition goldDust;
	private final IItemDefinition ironDust;
	private final IItemDefinition fluixDust;
	private final IItemDefinition certusQuartzDust;
	private final IItemDefinition netherQuartzDust;

	private final IItemDefinition matterBall;

	private final IItemDefinition certusQuartzCrystal;
	private final IItemDefinition certusQuartzCrystalCharged;
	private final IItemDefinition fluixCrystal;
	private final IItemDefinition fluixPearl;

	private final IItemDefinition woodenGear;

	private final IItemDefinition wirelessReceiver;
	private final IItemDefinition wirelessBooster;

	private final IItemDefinition annihilationCore;
	private final IItemDefinition formationCore;

	private final IItemDefinition singularity;
	private final IItemDefinition qESingularity;
	private final IItemDefinition blankPattern;

	private final IItemDefinition fluidCell1kPart;
	private final IItemDefinition fluidCell4kPart;
	private final IItemDefinition fluidCell16kPart;
	private final IItemDefinition fluidCell64kPart;

	public ApiMaterials( FeatureFactory registry )
	{
//		final ItemMaterial materials = new ItemMaterial();
//		registry.item( "material", () -> materials )
//				.rendering( new ItemRenderingCustomizer()
//				{
//					@Override
//					@OnlyIn( Dist.CLIENT )
//					public void customize( IItemRendering rendering )
//					{
//						rendering.meshDefinition( is -> materials.getTypeByStack( is ).getModel() );
//						// Register a resource location for every material type
//						rendering.variants( Arrays.stream( MaterialType.values() )
//								.map( MaterialType::getModel )
//								.collect( Collectors.toList() ) );
//					}
//				} )
//				.bootstrap( item -> (IEntityRegistrationComponent) r ->
//				{
//					r.register( EntityEntryBuilder.create()
//							.entity( EntitySingularity.class )
//							.id( new ResourceLocation( "appliedenergistics2", EntitySingularity.class.getName() ), EntityIds.get( EntitySingularity.class ) )
//							.name( EntitySingularity.class.getSimpleName() )
//							.tracker( 16, 4, true )
//							.build() );
//					r.register( EntityEntryBuilder.create()
//							.entity( EntityChargedQuartz.class )
//							.id( new ResourceLocation( "appliedenergistics2", EntityChargedQuartz.class.getName() ),
//									EntityIds.get( EntityChargedQuartz.class ) )
//							.name( EntityChargedQuartz.class.getSimpleName() )
//							.tracker( 16, 4, true )
//							.build() );
//				} )
//				.build();
//
		this.cell2SpatialPart = registry.features()
				.item( "cell2_spatial_part", ItemMaterial::new)
				.build();
		this.cell16SpatialPart = registry.features()
				.item( "cell16_spatial_part", ItemMaterial::new)
				.build();
		this.cell128SpatialPart = registry.features()
				.item( "cell128_spatial_part", ItemMaterial::new)
				.build();

		this.silicon = registry.features()
				.item( "silicon", ItemMaterial::new)
				.build();
		this.skyDust = registry.features()
				.item( "sky_dust", ItemMaterial::new)
				.build();

		this.calcProcessorPress = registry.features()
				.item( "calculation_processor_press", ItemMaterial::new)
				.build();
		this.engProcessorPress = registry.features()
				.item( "engineering_processor_press", ItemMaterial::new)
				.build();
		this.logicProcessorPress = registry.features()
				.item( "logic_processor_press", ItemMaterial::new)
				.build();
		this.siliconPress = registry.features()
				.item( "silicon_press", ItemMaterial::new)
				.build();
		this.namePress = registry.features()
				.item( "name_press", ItemMaterial::new)
				.build();

		this.calcProcessorPrint = registry.features()
				.item( "calculation_processor_print", ItemMaterial::new)
				.build();
		this.engProcessorPrint = registry.features()
				.item( "engineering_processor_print", ItemMaterial::new)
				.build();
		this.logicProcessorPrint = registry.features()
				.item( "logic_processor_print", ItemMaterial::new)
				.build();
		this.siliconPrint = registry.features()
				.item( "silicon_print", ItemMaterial::new)
				.build();

		this.logicProcessor = registry.features()
				.item( "logic_processor", ItemMaterial::new)
				.build();
		this.calcProcessor = registry.features()
				.item( "calculation_processor", ItemMaterial::new)
				.build();
		this.engProcessor = registry.features()
				.item( "engineering_processor", ItemMaterial::new)
				.build();

		this.basicCard = registry.features()
				.item( "basic_card", ItemMaterial::new)
				.build();
		this.advCard = registry.features()
				.item( "advanced_card", ItemMaterial::new)
				.build();

		this.purifiedCertusQuartzCrystal = registry.features()
				.item( "purified_certus_quartz_crystal", ItemMaterial::new)
				.build();
		this.purifiedNetherQuartzCrystal = registry.features()
				.item( "purified_nether_quartz_crystal", ItemMaterial::new)
				.build();
		this.purifiedFluixCrystal = registry.features()
				.item( "purified_fluix_crystal", ItemMaterial::new)
				.build();

		this.cell1kPart = registry.features()
				.item( "cell1k_part", ItemMaterial::new)
				.build();
		this.cell4kPart = registry.features()
				.item( "cell4k_part", ItemMaterial::new)
				.build();
		this.cell16kPart = registry.features()
				.item( "cell16k_part", ItemMaterial::new)
				.build();
		this.cell64kPart = registry.features()
				.item( "cell64k_part", ItemMaterial::new)
				.build();
		this.emptyStorageCell = registry.features()
				.item( "empty_storage_cell", ItemMaterial::new)
				.build();

		this.cardRedstone = registry.features()
				.item( "card_redstone", ItemMaterial::new)
				.build();
		this.cardSpeed = registry.features()
				.item( "card_speed", ItemMaterial::new)
				.build();
		this.cardCapacity = registry.features()
				.item( "card_capacity", ItemMaterial::new)
				.build();
		this.cardFuzzy = registry.features()
				.item( "card_fuzzy", ItemMaterial::new)
				.build();
		this.cardInverter = registry.features()
				.item( "card_inverter", ItemMaterial::new)
				.build();
		this.cardCrafting = registry.features()
				.item( "card_crafting", ItemMaterial::new)
				.build();


		this.enderDust = registry.features()
				.item( "ender_dust", ItemMaterial::new)
				.build();
		this.flour = registry.features()
				.item( "flour", ItemMaterial::new)
				.build();
		this.goldDust = registry.features()
				.item( "gold_dust", ItemMaterial::new)
				.build();
		this.ironDust = registry.features()
				.item("iron_dust", ItemMaterial::new)
				.build();
		this.fluixDust = registry.features()
				.item( "fluix_dust", ItemMaterial::new)
				.build();
		this.certusQuartzDust = registry.features()
				.item( "certus_quartz_dust", ItemMaterial::new)
				.build();
		this.netherQuartzDust = registry.features()
				.item( "nether_quartz_dust", ItemMaterial::new)
				.build();

		this.matterBall = registry.features()
				.item( "matter_ball", ItemMaterial::new)
				.build();

		this.certusQuartzCrystal = registry.features()
				.item( "certus_quartz_crystal", ItemMaterial::new)
				.build();
		this.certusQuartzCrystalCharged = registry.features()
				.item( "certus_quartz_crystal_charged", ItemMaterial::new)
				.build();
		this.fluixCrystal = registry.features()
				.item( "fluix_crystal", ItemMaterial::new)
				.build();
		this.fluixPearl = registry.features()
				.item( "fluix_pearl", ItemMaterial::new)
				.build();

		this.woodenGear = registry.features()
				.item( "wooden_gear", ItemMaterial::new)
				.build();

		this.wirelessReceiver = registry.features()
				.item( "wireless", ItemMaterial::new)
				.build();
		this.wirelessBooster = registry.features()
				.item( "wireless_booster", ItemMaterial::new)
				.build();

		this.annihilationCore = registry.features()
				.item( "annihilation_core", ItemMaterial::new)
				.build();
		this.formationCore = registry.features()
				.item( "formation_core", ItemMaterial::new)
				.build();

		this.singularity = registry.features()
				.item( "singularity", ItemMaterial::new)
				.build();
		this.qESingularity = registry.features()
				.item( "quantum_entangled_singularity", ItemMaterial::new)
				.build();
		this.blankPattern = registry.features()
				.item( "blank_pattern", ItemMaterial::new)
				.build();

		this.fluidCell1kPart = registry.features()
				.item( "fluid_cell1k_part", ItemMaterial::new)
				.build();
		this.fluidCell4kPart = registry.features()
				.item( "fluid_cell4k_part", ItemMaterial::new)
				.build();
		this.fluidCell16kPart = registry.features()
				.item( "fluid_cell16k_part", ItemMaterial::new)
				.build();
		this.fluidCell64kPart = registry.features()
				.item( "fluid_cell64k_part", ItemMaterial::new)
				.build();
	}

	@Override
	public IItemDefinition cell2SpatialPart()
	{
		return this.cell2SpatialPart;
	}

	@Override
	public IItemDefinition cell16SpatialPart()
	{
		return this.cell16SpatialPart;
	}

	@Override
	public IItemDefinition cell128SpatialPart()
	{
		return this.cell128SpatialPart;
	}

	@Override
	public IItemDefinition silicon()
	{
		return this.silicon;
	}

	@Override
	public IItemDefinition skyDust()
	{
		return this.skyDust;
	}

	@Override
	public IItemDefinition calcProcessorPress()
	{
		return this.calcProcessorPress;
	}

	@Override
	public IItemDefinition engProcessorPress()
	{
		return this.engProcessorPress;
	}

	@Override
	public IItemDefinition logicProcessorPress()
	{
		return this.logicProcessorPress;
	}

	@Override
	public IItemDefinition calcProcessorPrint()
	{
		return this.calcProcessorPrint;
	}

	@Override
	public IItemDefinition engProcessorPrint()
	{
		return this.engProcessorPrint;
	}

	@Override
	public IItemDefinition logicProcessorPrint()
	{
		return this.logicProcessorPrint;
	}

	@Override
	public IItemDefinition siliconPress()
	{
		return this.siliconPress;
	}

	@Override
	public IItemDefinition siliconPrint()
	{
		return this.siliconPrint;
	}

	@Override
	public IItemDefinition namePress()
	{
		return this.namePress;
	}

	@Override
	public IItemDefinition logicProcessor()
	{
		return this.logicProcessor;
	}

	@Override
	public IItemDefinition calcProcessor()
	{
		return this.calcProcessor;
	}

	@Override
	public IItemDefinition engProcessor()
	{
		return this.engProcessor;
	}

	@Override
	public IItemDefinition basicCard()
	{
		return this.basicCard;
	}

	@Override
	public IItemDefinition advCard()
	{
		return this.advCard;
	}

	@Override
	public IItemDefinition purifiedCertusQuartzCrystal()
	{
		return this.purifiedCertusQuartzCrystal;
	}

	@Override
	public IItemDefinition purifiedNetherQuartzCrystal()
	{
		return this.purifiedNetherQuartzCrystal;
	}

	@Override
	public IItemDefinition purifiedFluixCrystal()
	{
		return this.purifiedFluixCrystal;
	}

	@Override
	public IItemDefinition cell1kPart()
	{
		return this.cell1kPart;
	}

	@Override
	public IItemDefinition cell4kPart()
	{
		return this.cell4kPart;
	}

	@Override
	public IItemDefinition cell16kPart()
	{
		return this.cell16kPart;
	}

	@Override
	public IItemDefinition cell64kPart()
	{
		return this.cell64kPart;
	}

	@Override
	public IItemDefinition emptyStorageCell()
	{
		return this.emptyStorageCell;
	}

	@Override
	public IItemDefinition cardRedstone()
	{
		return this.cardRedstone;
	}

	@Override
	public IItemDefinition cardSpeed()
	{
		return this.cardSpeed;
	}

	@Override
	public IItemDefinition cardCapacity()
	{
		return this.cardCapacity;
	}

	@Override
	public IItemDefinition cardFuzzy()
	{
		return this.cardFuzzy;
	}

	@Override
	public IItemDefinition cardInverter()
	{
		return this.cardInverter;
	}

	@Override
	public IItemDefinition cardCrafting()
	{
		return this.cardCrafting;
	}

	@Override
	public IItemDefinition enderDust()
	{
		return this.enderDust;
	}

	@Override
	public IItemDefinition flour()
	{
		return this.flour;
	}

	@Override
	public IItemDefinition goldDust()
	{
		return this.goldDust;
	}

	@Override
	public IItemDefinition ironDust()
	{
		return this.ironDust;
	}

	@Override
	public IItemDefinition fluixDust()
	{
		return this.fluixDust;
	}

	@Override
	public IItemDefinition certusQuartzDust()
	{
		return this.certusQuartzDust;
	}

	@Override
	public IItemDefinition netherQuartzDust()
	{
		return this.netherQuartzDust;
	}

	@Override
	public IItemDefinition matterBall()
	{
		return this.matterBall;
	}

	@Override
	public IItemDefinition certusQuartzCrystal()
	{
		return this.certusQuartzCrystal;
	}

	@Override
	public IItemDefinition certusQuartzCrystalCharged()
	{
		return this.certusQuartzCrystalCharged;
	}

	@Override
	public IItemDefinition fluixCrystal()
	{
		return this.fluixCrystal;
	}

	@Override
	public IItemDefinition fluixPearl()
	{
		return this.fluixPearl;
	}

	@Override
	public IItemDefinition woodenGear()
	{
		return this.woodenGear;
	}

	@Override
	public IItemDefinition wirelessReceiver()
	{
		return this.wirelessReceiver;
	}

	@Override
	public IItemDefinition wirelessBooster()
	{
		return this.wirelessBooster;
	}

	@Override
	public IItemDefinition annihilationCore()
	{
		return this.annihilationCore;
	}

	@Override
	public IItemDefinition formationCore()
	{
		return this.formationCore;
	}

	@Override
	public IItemDefinition singularity()
	{
		return this.singularity;
	}

	@Override
	public IItemDefinition qESingularity()
	{
		return this.qESingularity;
	}

	@Override
	public IItemDefinition blankPattern()
	{
		return this.blankPattern;
	}

	@Override
	public IItemDefinition fluidCell1kPart()
	{
		return this.fluidCell1kPart;
	}

	@Override
	public IItemDefinition fluidCell4kPart()
	{
		return this.fluidCell4kPart;
	}

	@Override
	public IItemDefinition fluidCell16kPart()
	{
		return this.fluidCell16kPart;
	}

	@Override
	public IItemDefinition fluidCell64kPart()
	{
		return this.fluidCell64kPart;
	}
}
