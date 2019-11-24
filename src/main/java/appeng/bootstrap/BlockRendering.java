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


import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import appeng.block.AEBaseTileBlock;
import appeng.bootstrap.components.BlockColorComponent;
import appeng.bootstrap.components.IModelBakeComponent;
import appeng.bootstrap.components.IModelRegistrationComponent;
import appeng.bootstrap.components.TesrComponent;
import appeng.client.render.model.AutoRotatingModel;
import appeng.client.render.model.GlassBakedModel;
import appeng.client.render.model.GlassModel;
import appeng.core.AELog;


class BlockRendering implements IBlockRendering
{

	@OnlyIn( Dist.CLIENT )
	private BiFunction<ModelResourceLocation, IBakedModel, IBakedModel> modelCustomizer;

	@OnlyIn( Dist.CLIENT )
	private IBlockColor blockColor;

	@OnlyIn( Dist.CLIENT )
	private TileEntityRenderer<?> tesr;

//	@OnlyIn( Dist.CLIENT )
//	private IStateMapper stateMapper;

	@OnlyIn( Dist.CLIENT )
	private Map<String, IUnbakedModel> builtInModels = new HashMap<>();

	@Override
	@OnlyIn( Dist.CLIENT )
	public IBlockRendering modelCustomizer( BiFunction<ModelResourceLocation, IBakedModel, IBakedModel> customizer )
	{
		this.modelCustomizer = customizer;
		return this;
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public IBlockRendering blockColor( IBlockColor blockColor )
	{
		this.blockColor = blockColor;
		return this;
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public IBlockRendering tesr( TileEntityRenderer<?> tesr )
	{
		this.tesr = tesr;
		return this;
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public IBlockRendering builtInModel( String name, IUnbakedModel model )
	{
		this.builtInModels.put( name, model );
		return this;
	}

//	@Override
//	public IBlockRendering stateMapper( IStateMapper mapper )
//	{
//		this.stateMapper = mapper;
//		return this;
//	}

	void apply( FeatureFactory factory, Block block, Class<?> tileEntityClass )
	{
		if( this.tesr != null )
		{
			if( tileEntityClass == null )
			{
				throw new IllegalStateException( "Tried to register a TESR for " + block + " even though no tile entity has been specified." );
			}
			factory.addBootstrapComponent( new TesrComponent( tileEntityClass, this.tesr ) );
		}

		if( this.modelCustomizer != null )
		{
			factory.addModelOverride( block.getRegistryName().getPath(), this.modelCustomizer );
		}
		else if( block instanceof AEBaseTileBlock )
		{
			// This is a default rotating model if the base-block uses an AE tile entity which exposes UP/FRONT as
			// extended props
//			factory.addModelOverride( block.getRegistryName().getPath(), ( l, m ) -> new AutoRotatingModel( m ) );
		}

		// TODO : 1.12
		this.builtInModels.forEach( factory::addBuiltInModel );
//		factory.addBootstrapComponent( (IModelRegistrationComponent) () -> this.itemModels.forEach(ModelLoader::addSpecialModel));
		factory.addBootstrapComponent((IModelBakeComponent) (modelManager, modelLoader, modelRegistry) -> {
//			this.builtInModels.forEach((s, iModel) -> {
//				modelRegistry.put(new ModelResourceLocation("appliedenergistics2:"), iModel.bake(modelLoader.getBakedModel()));
//			});
//			modelRegistry.keySet()
//					.stream()
//					.filter(resourceLocation -> resourceLocation.getNamespace().startsWith("appliedenergistics2"))
//					.forEach(resourceLocation -> {
//						AELog.info("%s", resourceLocation);
//					});
			IBakedModel model = new GlassBakedModel(DefaultVertexFormats.BLOCK, resourceLocation -> {
				return Minecraft.getInstance().getTextureMap().getSprite(resourceLocation);
			});
			modelRegistry.put(new ModelResourceLocation("appliedenergistics2:quartz_glass", ""), model);
			modelRegistry.put(new ModelResourceLocation("appliedenergistics2:quartz_vibrant_glass", ""), model);
		});

		if( this.blockColor != null )
		{
			factory.addBootstrapComponent( new BlockColorComponent( block, this.blockColor ) );
		}

//		if( this.stateMapper != null )
//		{
//			factory.addBootstrapComponent( new StateMapperComponent( block, this.stateMapper ) );
//		}
	}
}
