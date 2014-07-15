package com.texasjake95.core.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.entity.player.EntityPlayer;

/**
 * This is an event that is fired by the auto switcher tick handler to see if there is anything
 * preventing the tool from switching<br>
 * This Event is fired in the {@link MinecraftForge#EVENT_BUS}
 *
 * @author Texasjake95
 *
 */
@Cancelable
public class AutoSwitchEvent extends PlayerEvent {

	public AutoSwitchEvent(EntityPlayer player)
	{
		super(player);
	}
}
