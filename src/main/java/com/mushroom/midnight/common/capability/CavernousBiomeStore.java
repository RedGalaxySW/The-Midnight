package com.mushroom.midnight.common.capability;

import com.mushroom.midnight.Midnight;
import com.mushroom.midnight.common.biome.cavern.CavernousBiome;
import com.mushroom.midnight.common.registry.ModCavernousBiomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CavernousBiomeStore implements ICapabilitySerializable<NBTTagCompound> {
    private final CavernousBiome[] biomes = new CavernousBiome[256];

    @Nonnull
    public CavernousBiome getBiome(int x, int z) {
        CavernousBiome biome = this.biomes[(x & 15) + (z & 15) << 4];
        if (biome == null) {
            return ModCavernousBiomes.NONE;
        }
        return biome;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        ForgeRegistry<CavernousBiome> registry = ModCavernousBiomes.getRegistry();

        byte[] bytes = new byte[this.biomes.length];
        for (int i = 0; i < this.biomes.length; i++) {
            CavernousBiome biome = this.biomes[i];
            if (biome != null) {
                bytes[i] = (byte) (registry.getID(biome) & 0xFF);
            }
        }

        compound.setByteArray("biomes", bytes);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        ForgeRegistry<CavernousBiome> registry = ModCavernousBiomes.getRegistry();

        byte[] bytes = compound.getByteArray("biomes");
        for (int i = 0; i < bytes.length; i++) {
            this.biomes[i] = registry.getValue(bytes[i] & 0xFF);
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == Midnight.CAVERNOUS_BIOME_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == Midnight.CAVERNOUS_BIOME_CAP) {
            return Midnight.CAVERNOUS_BIOME_CAP.cast(this);
        }
        return null;
    }
}