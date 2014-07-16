package com.texasjake95.core.proxy.client.entity;

import net.minecraft.client.entity.AbstractClientPlayer;

import com.texasjake95.core.proxy.entity.player.EntityPlayerProxy;

/**
 * This class is used as a proxy to call code for Minecraft's {@link AbstractClientPlayer} due
 * to the nature of the changing names of methods and fields.<br>
 * The names of the methods in this class should almost never change, that being said the
 * parameters may change to match Minecraft's methods
 *
 * @author Texasjake95
 *
 */
public class AbstractClientPlayerProxy extends EntityPlayerProxy {
}
