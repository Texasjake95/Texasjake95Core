package com.texasjake95.core.lib.handler.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public abstract class TickHandler {
	
	/**
	 * A "wrapper" method to handle ClientTickEvents
	 * 
	 * @param event
	 *            - event to handle
	 */
	protected abstract void handleClientTick(ClientTickEvent event);
	
	/**
	 * A "wrapper" method to handle PlayerTickEvents
	 * 
	 * @param event
	 *            - event to handle
	 */
	protected abstract void handlePlayerTick(PlayerTickEvent event);
	
	/**
	 * A "wrapper" method to handle RenderTickEvents
	 * 
	 * @param event
	 *            - event to handle
	 */
	protected abstract void handleRenderTick(RenderTickEvent event);
	
	/**
	 * A "wrapper" method to handle ServerTickEvents
	 * 
	 * @param event
	 *            - event to handle
	 */
	protected abstract void handleServerTick(ServerTickEvent event);
	
	/**
	 * A "wrapper" method to handle WorldTickEvents
	 * 
	 * @param event
	 *            - event to handle
	 */
	protected abstract void handleWorldTick(WorldTickEvent event);
	
	@SubscribeEvent
	public final void onPlayerTick(PlayerTickEvent event)
	{
		this.handlePlayerTick(event);
	}
	
	@SubscribeEvent
	public final void onRenderTick(RenderTickEvent event)
	{
		this.handleRenderTick(event);
	}
	
	@SubscribeEvent
	public final void onServerTick(ClientTickEvent event)
	{
		this.handleClientTick(event);
	}
	
	@SubscribeEvent
	public final void onServerTick(ServerTickEvent event)
	{
		this.handleServerTick(event);
	}
	
	@SubscribeEvent
	public final void onWorldTick(WorldTickEvent event)
	{
		this.handleWorldTick(event);
	}
}
