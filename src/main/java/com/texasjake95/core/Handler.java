package com.texasjake95.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.common.ForgeChunkManager;

public class Handler {

	@SubscribeEvent
	public void force(ForgeChunkManager.ForceChunkEvent event)
	{
		System.out.println("Forcing: " + event.ticket.getModId() + " => Location: " + event.location);
	}

	@SubscribeEvent
	public void unforce(ForgeChunkManager.UnforceChunkEvent event)
	{
		System.out.println("Unforcing: " + event.ticket.getModId() + " => Location: " + event.location);
	}
}
