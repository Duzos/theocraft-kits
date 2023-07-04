package com.duzo.theocraftkits.util;

import net.minecraft.nbt.NbtCompound;

public class PlayerDataUtil {
    public static boolean canUseKits(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        if (!nbt.contains("kits_enabled")) {
            nbt.putBoolean("kits_enabled",true);
        }

        return nbt.getBoolean("kits_enabled");
    }
    public static void canUseKits(IEntityDataSaver player, boolean val) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putBoolean("kits_enabled",val);
    }
}
