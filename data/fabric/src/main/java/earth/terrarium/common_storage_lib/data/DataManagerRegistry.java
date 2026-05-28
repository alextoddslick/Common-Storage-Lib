package earth.terrarium.common_storage_lib.data;

import earth.terrarium.common_storage_lib.data.impl.DataManagerBuilderImpl;

import java.util.function.Supplier;

public final class DataManagerRegistry {
    private final String modid;

    public DataManagerRegistry(String modid) {
        this.modid = modid;
    }

    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new DataManagerBuilderImpl<>(modid, factory);
    }

    public void init() {
    }
}
