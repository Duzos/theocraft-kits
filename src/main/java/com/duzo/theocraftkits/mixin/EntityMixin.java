package com.duzo.theocraftkits.mixin;

import com.duzo.theocraftkits.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityDataSaver {
    private NbtCompound persistentData;
    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }

        return persistentData;
    }

    @Inject(method="writeNbt",at = @At("HEAD"))
    protected void THEOCRAFT_injectWriteNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (persistentData != null) {
            nbt.put("theocraft.custom_data",persistentData);
        }
    }
    @Inject(method="readNbt",at = @At("HEAD"))
    protected void THEOCRAFT_injectReadNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("theocraft.custom_data")) {
            persistentData = nbt.getCompound("theocraft.custom_data");
        }
    }
}
