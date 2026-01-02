package earth.terrarium.botarium.common.fluid.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record FluidParticleOptions(FluidHolder fluid) implements ParticleOptions {

    @Override
    public @NotNull ParticleType<?> getType() {
        return Botarium.FLUID_PARTICLE.get();
    }

    public static final Codec<FluidParticleOptions> CODEC = FluidHolder.CODEC.xmap(FluidParticleOptions::new,
            FluidParticleOptions::fluid);

}
