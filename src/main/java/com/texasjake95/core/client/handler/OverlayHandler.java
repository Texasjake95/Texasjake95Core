package com.texasjake95.core.client.handler;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.texasjake95.core.OreStack;
import com.texasjake95.core.WrappedStack;
import com.texasjake95.core.WrappedStack.WrappedType;
import com.texasjake95.core.api.CoreInfo;
import com.texasjake95.core.data.DataMapWrapper;
import com.texasjake95.core.data.FloatValue;

public class OverlayHandler {

	private static final boolean displayMessage = CoreInfo.modVersion.contains("${") || CoreInfo.modVersion.contains("dev");

	@SubscribeEvent
	public void addToolTip(ItemTooltipEvent event)
	{
		if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak))
		{
			FloatValue value = DataMapWrapper.getValueNoScale(event.itemStack);
			if (value != null && value.isValid())
			{
				FloatValue scaledValue = DataMapWrapper.getValue(event.itemStack);
				event.toolTip.add("Value: " + scaledValue);
				if (event.itemStack.isStackable() && event.itemStack.stackSize > 1)
					event.toolTip.add("Stack Value: " + new FloatValue(scaledValue.value * event.itemStack.stackSize));
				Set<WrappedStack> stacks = Sets.newTreeSet(DataMapWrapper.getNodesFromValue(value));
				HashSet<WrappedStack> remove = Sets.newHashSet();
				for (WrappedStack stack : stacks)
				{
					if (stack.type == WrappedType.ORESTACK || stack.type == WrappedType.FLUIDSTACK)
						remove.add(stack);
					if (stack.type == WrappedType.ITEMSTACK)
					{
						ItemStack itemStack = (ItemStack) stack.getEffectiveStack();
						if (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
							remove.add(stack);
						if (OreDictionary.itemMatches(event.itemStack, itemStack, true))
							remove.add(stack);
					}
				}
				for (WrappedStack stack : remove)
					stacks.remove(stack);
				if (!stacks.isEmpty())
				{
					event.toolTip.add("Items with Same Value:");
					for (WrappedStack stack : stacks)
						event.toolTip.add("     " + stack.translate());
				}
			}
			else
				event.toolTip.add("Could not find a value");
			HashSet<String> names = new OreStack(event.itemStack).getNames();
			if (names.size() > 0)
			{
				event.toolTip.add("OreDict Names:");
				for (String name : names)
					event.toolTip.add("     " + name);
			}
		}
	}

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Text event)
	{
		if (displayMessage)
		{
			String s = EnumChatFormatting.RED + "THIS IS A BETA BUILD" + EnumChatFormatting.RESET;
			event.right.add(s);
		}
	}
}
