package com.mushroom.midnight.common.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class NullStorage<T extends ICapabilityProvider> implements Capability.IStorage<T> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
        return null;
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {

    }
}
