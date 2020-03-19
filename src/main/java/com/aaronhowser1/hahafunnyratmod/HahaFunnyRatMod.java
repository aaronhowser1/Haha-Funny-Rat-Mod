package com.aaronhowser1.hahafunnyratmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
@Mod(modid = HahaFunnyRatMod.MODID, name = HahaFunnyRatMod.NAME, version = HahaFunnyRatMod.VERSION)
public class HahaFunnyRatMod {
    public static final String MODID = "hahafunnyratmod";
    public static final String NAME = "Haha Funny Rat Mod";
    public static final String VERSION = "1.0.0";
    private static final HashMap<String, String> MODS = new HashMap<>();
    private static final HashMap<String, String> CACHE = new HashMap<>();
    public static final String MOD = "hahafunnyratmod:mod";
    private static final String BLOCK = "hahafunnyratmod:block";
    private static final String ITEM = "hahafunnyratmod:item";

    private static Logger logger;

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Map<String, ModContainer> modList = Loader.instance().getIndexedModList();

        for (String modid : modList.keySet()) {
            String authorList = modList.get(modid).getMetadata().getAuthorList();

            if (authorList.contains("Alexthe666")) {
                MODS.put(modid, modList.get(modid).getName());
            }

            if (modList.get(modid) instanceof FMLModContainer) {
                if (authorList.contains("Alexthe666")) {
                    FMLModContainer container = (FMLModContainer) modList.get(modid);
                    ModMetadata metadata = ReflectionHelper.getPrivateValue(FMLModContainer.class, container, "modMetadata");

                    metadata.name = I18n.format(MOD, metadata.name);
                    ReflectionHelper.setPrivateValue(FMLModContainer.class, container, metadata, "modMetadata");
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getToolTip().size() < 1)
            return;

        List<String> tooltips = event.getToolTip();
        String name = tooltips.get(0);
        ItemStack stack = event.getItemStack();

        if (!stack.isEmpty() && MODS.containsKey(stack.getItem().getRegistryName().toString())) {
            String newName;

            if (!CACHE.containsKey(name)) {
                String addition = "";
                String cachedName = name;

                if (Minecraft.getMinecraft().gameSettings.advancedItemTooltips && name.contains(" ")) {
                    addition = name.substring(name.lastIndexOf(" "), name.length());
                    name = name.replace(addition, "");
                }

                newName = I18n.format(stack.getItem() instanceof ItemBlock ? BLOCK : ITEM, name) + addition;
                CACHE.put(cachedName, newName);
            } else
                newName = CACHE.get(name);

            tooltips.set(0, newName);
        }
    }
}
