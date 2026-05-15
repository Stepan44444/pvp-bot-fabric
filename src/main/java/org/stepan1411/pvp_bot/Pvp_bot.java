package org.stepan1411.pvp_bot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.stepan1411.pvp_bot.bot.BotDamageHandler;
import org.stepan1411.pvp_bot.bot.BotKits;
import org.stepan1411.pvp_bot.bot.BotManager;
import org.stepan1411.pvp_bot.bot.BotPath;
import org.stepan1411.pvp_bot.bot.BotTicker;
import org.stepan1411.pvp_bot.command.BotCommand;
import org.stepan1411.pvp_bot.config.WorldConfigHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pvp_bot implements ModInitializer {

    public static final String MOD_ID = "pvp_bot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("PVP Bot mod loaded!");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BotCommand.register(dispatcher);
        });


        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            WorldConfigHelper.init(server);
            

            WorldConfigHelper.setOnWorldChangeCallback(() -> {
                BotManager.switchWorld(server);
                BotPath.init();
            });
            
            BotManager.init(server);
            BotKits.init(server);
            BotPath.init();
        });
        

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            BotManager.reset(server);
        });

        BotTicker.register();
        BotDamageHandler.register();
    }
}
