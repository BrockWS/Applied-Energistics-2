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

package appeng.bootstrap;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import appeng.api.util.AEColor;
import appeng.bootstrap.components.BuiltInModelComponent;
import appeng.bootstrap.components.IModelBakeComponent;
import appeng.bootstrap.components.IModelRegistrationComponent;
import appeng.core.features.AEFeature;
import appeng.util.Platform;


public class FeatureFactory
{

	private final AEFeature[] defaultFeatures;

	private final Map<Class<? extends IBootstrapComponent>, List<IBootstrapComponent>> bootstrapComponents;

//	@OnlyIn( Dist.CLIENT )
//	private ModelOverrideComponent modelOverrideComponent;

    @OnlyIn( Dist.CLIENT )
	private BuiltInModelComponent builtInModelComponent;

	public FeatureFactory()
	{
		this.defaultFeatures = new AEFeature[] { AEFeature.CORE };
		this.bootstrapComponents = new HashMap<>();

		if( Platform.isClient() )
		{
//			this.modelOverrideComponent = new ModelOverrideComponent();
//			this.addBootstrapComponent( this.modelOverrideComponent );

			this.builtInModelComponent = new BuiltInModelComponent();
			this.addBootstrapComponent( this.builtInModelComponent );
		}
	}

	private FeatureFactory( FeatureFactory parent, AEFeature... defaultFeatures )
	{
		this.defaultFeatures = defaultFeatures.clone();
		this.bootstrapComponents = parent.bootstrapComponents;
		if( Platform.isClient() )
		{
//			this.modelOverrideComponent = parent.modelOverrideComponent;
			this.builtInModelComponent = parent.builtInModelComponent;
		}
	}

	public IBlockBuilder block( String id, Supplier<Block> block )
	{
		return new BlockDefinitionBuilder( this, id, block ).features( this.defaultFeatures );
	}

	public IItemBuilder item( String id, Supplier<Item> item )
	{
		return new ItemDefinitionBuilder( this, id, item ).features( this.defaultFeatures );
	}

	public IAEColoredItemBuilder colored(String id, List<AEColor> validColors, Function<AEColor, Item> item)
	{
//		ColoredItemDefinition definition = new ColoredItemDefinition();
//
//		for( final AEColor color : AEColor.VALID_COLORS )
//		{
//
//			final ActivityState state = ActivityState.from( target.isEnabled() );
//
//			definition.add( color, new ItemStackSrc( item.apply(color), ActivityState.Enabled ) );
//		}
//
//		return definition;

		return new AEColoredItemDefinitionBuilder(this, id, validColors, item).features(this.defaultFeatures);
	}

	public FeatureFactory features( AEFeature... features )
	{
		return new FeatureFactory( this, features );
	}

	public void addBootstrapComponent( IBootstrapComponent component )
	{
		Arrays.stream( component.getClass().getInterfaces() )
				.filter( i -> IBootstrapComponent.class.isAssignableFrom( i ) )
				.forEach( i -> this.addBootstrapComponent( (Class<? extends IBootstrapComponent>) i, component ) );
	}

	private <T extends IBootstrapComponent> void addBootstrapComponent( Class<? extends IBootstrapComponent> eventType, T component )
	{
		this.bootstrapComponents.computeIfAbsent( eventType, c -> new ArrayList<IBootstrapComponent>() ).add( component );
	}

    @OnlyIn( Dist.CLIENT )
	void addBuiltInModel( String path, IUnbakedModel model )
	{
		this.builtInModelComponent.addModel( path, model );
	}

    @OnlyIn( Dist.CLIENT )
	void addModelOverride( String resourcePath, BiFunction<ModelResourceLocation, IBakedModel, IBakedModel> customizer )
	{
//		this.modelOverrideComponent.addOverride( resourcePath, customizer );
	}

	public <T extends IBootstrapComponent> Iterator<T> getBootstrapComponents( Class<T> eventType )
	{
		return (Iterator<T>) this.bootstrapComponents.getOrDefault( eventType, Collections.emptyList() ).iterator();
	}
}
