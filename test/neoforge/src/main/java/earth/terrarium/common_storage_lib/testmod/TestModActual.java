package earth.terrarium.common_storage_lib.testmod;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.Set;
import java.util.function.Supplier;

public class TestModActual {
    @Actual
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier) {
        return NeoTestMod.BLOCK_ENTITY_TYPES.register(name, () -> new BlockEntityType<>(supplier, Set.of(NeoTestMod.TRANSFER_BLOCK.get())));
    }
}
