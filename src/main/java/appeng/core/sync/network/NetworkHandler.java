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

package appeng.core.sync.network;


import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.*;
import net.minecraftforge.fml.network.NetworkEvent.ClientCustomPayloadEvent;
import net.minecraftforge.fml.network.NetworkEvent.ServerCustomPayloadEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.event.EventNetworkChannel;

import appeng.core.AELog;
import appeng.core.AppEng;
import appeng.core.config.AEConfig;
import appeng.core.features.AEFeature;
import appeng.core.sync.AppEngPacket;

import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.tuple.Pair;


public class NetworkHandler
{
	private static final String PROTOCOL_VERSION = "1";
	private static NetworkHandler instance;

	private final EventNetworkChannel ec;
	private final ResourceLocation myChannelName;

	private final IPacketHandler clientHandler;
	private final IPacketHandler serveHandler;

	public NetworkHandler( final String channelName )
	{
		this.ec = NetworkRegistry.newEventChannel(
				this.myChannelName = new ResourceLocation(AppEng.MOD_ID, channelName),
				() -> PROTOCOL_VERSION,
				PROTOCOL_VERSION::equals,
				PROTOCOL_VERSION::equals);
		this.ec.registerObject(this);

		this.clientHandler = this.createClientSide();
		this.serveHandler = this.createServerSide();
	}

	public static void init( final String channelName )
	{
		instance = new NetworkHandler( channelName );
	}

	public static NetworkHandler instance()
	{
		return instance;
	}

	private IPacketHandler createClientSide()
	{
		try
		{
			return new AppEngClientPacketHandler();
		}
		catch( final Throwable t )
		{
			return null;
		}
	}

	private IPacketHandler createServerSide()
	{
		try
		{
			return new AppEngServerPacketHandler();
		}
		catch( final Throwable t )
		{
			return null;
		}
	}

	@SubscribeEvent
	public void serverPacket( final ServerCustomPayloadEvent ev )
	{
		final Context context = ev.getSource().get();
		if( this.serveHandler != null )
		{
			try
			{
				this.serveHandler.onPacketData( null, context.getNetworkManager().getNetHandler(), ev.getPayload(), context.getSender() );
			}
			catch( final ThreadQuickExitException ignored )
			{

			}
		}
	}

	@SubscribeEvent
	public void clientPacket( final ClientCustomPayloadEvent ev )
	{
		final Context context = ev.getSource().get();
		if( this.clientHandler != null )
		{
			try
			{
				this.clientHandler.onPacketData( null, context.getNetworkManager().getNetHandler(), ev.getPayload(), null );
			}
			catch( final ThreadQuickExitException ignored )
			{

			}
		}
	}

	public ResourceLocation getChannel()
	{
		return this.myChannelName;
	}

	public void sendToAll( final AppEngPacket message )
	{
		PacketDistributor.ALL.noArg().send(this.buildPacket(NetworkDirection.PLAY_TO_SERVER, message));
	}

	public void sendTo( final AppEngPacket message, final ServerPlayerEntity player )
	{
		PacketDistributor.PLAYER.with(() -> player).send(this.buildPacket(NetworkDirection.PLAY_TO_SERVER, message));
	}

	public void sendToAllAround( final AppEngPacket message, final PacketDistributor.TargetPoint point )
	{
		PacketDistributor.NEAR.with(() -> point).send(this.buildPacket(NetworkDirection.PLAY_TO_SERVER, message));
	}

	public void sendToDimension( final AppEngPacket message, final int dimensionId )
	{
		PacketDistributor.DIMENSION.with(() -> DimensionType.getById(dimensionId)).send(this.buildPacket(NetworkDirection.PLAY_TO_SERVER, message));
	}

	public void sendToServer( final AppEngPacket message )
	{
		PacketDistributor.SERVER.noArg().send(this.buildPacket(NetworkDirection.PLAY_TO_SERVER, message));
	}

	private IPacket<?> buildPacket(NetworkDirection direction, AppEngPacket packet) {
		PacketBuffer packetBuffer = packet.getPacketBuffer();
		if( packetBuffer.array().length > 2 * 1024 * 1024 ) // 2k walking room :)
		{
			throw new IllegalArgumentException( "Sorry AE2 made a " + packetBuffer.array().length + " byte packet by accident!" );
		}

		Pair<PacketBuffer, Integer> packetData = Pair.of(packetBuffer, packet.getPacketID());
		if( AEConfig.instance().isFeatureEnabled( AEFeature.PACKET_LOGGING ) )
		{
			AELog.info( this.getClass().getName() + " : " + packetBuffer.readableBytes() );
		}
		return direction.buildPacket(packetData, this.getChannel()).getThis();
	}
}
