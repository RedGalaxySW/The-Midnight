package com.mushroom.midnight.client;

import com.mushroom.midnight.Midnight;
import com.mushroom.midnight.client.sound.MidnightCaveSound;
import com.mushroom.midnight.client.sound.MidnightIdleSound;
import com.mushroom.midnight.common.util.MidnightUtil;
import com.mushroom.midnight.common.util.SlidingToggle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Midnight.MODID, value = Dist.CLIENT)
public class IdleSoundController {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    private static final ISound IDLE_SOUND = new MidnightIdleSound();
    private static final ISound CAVE_IDLE_SOUND = new MidnightCaveSound();

    public static final SlidingToggle CAVE_ANIMATION = new SlidingToggle(20);

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (!CLIENT.isGamePaused()) {
            PlayerEntity player = CLIENT.player;
            if (player == null || event.phase == TickEvent.Phase.START) {
                return;
            }

            if (MidnightUtil.isMidnightDimension(player.world)) {
                CAVE_ANIMATION.set(player.getPosY() < 62 && !player.world.canBlockSeeSky(player.getPosition()));
                CAVE_ANIMATION.update();

                retainIdleSound(IDLE_SOUND);
                retainIdleSound(CAVE_IDLE_SOUND);
            }
        }
    }

    private static void retainIdleSound(ISound sound) {
        SoundHandler soundHandler = CLIENT.getSoundHandler();
        if (!soundHandler.isPlaying(sound)) {
            try {
                // Fix very odd bug where playSound would complain that the sound is already playing
                soundHandler.stop(sound);
                soundHandler.play(sound);
            } catch (IllegalArgumentException e) {
                // Ignore SoundHandler complaints
            }
        }
    }
}
