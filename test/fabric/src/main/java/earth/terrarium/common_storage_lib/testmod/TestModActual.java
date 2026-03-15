package earth.terrarium.common_storage_lib.testmod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.Set;
import java.util.function.Supplier;

public class TestModActual {
    @Actual
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier) {
        final BlockEntityType<T> register = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(TestMod.MOD_ID, name), new BlockEntityType<>(supplier, Set.of(FabricTestMod.TRANSFER_BLOCK)));
        return () -> register;
    }
}
