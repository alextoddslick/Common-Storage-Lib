package earth.terrarium.botarium.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public interface Serializable {

    /**
     * Deserializes the information of an object from a {@link CompoundTag}.
     *
     * @param nbt      The {@link CompoundTag} to deserialize from.
     * @param provider The {@link HolderLookup.Provider} to look up registry
     *                 entries.
     */
    void deserialize(CompoundTag nbt, HolderLookup.Provider provider);

    /**
     * Serializes the information of an object to a {@link CompoundTag}.
     *
     * @param nbt      The {@link CompoundTag} to serialize to.
     * @param provider The {@link HolderLookup.Provider} to look up registry
     *                 entries.
     * @return The {@link CompoundTag} that was passed in but with the information
     *         of the object serialized added to it.
     */
    CompoundTag serialize(CompoundTag nbt, HolderLookup.Provider provider);
}
