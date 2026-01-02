package testmod;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;

public class ManaContainerItem extends ManaContainer {
    private final ItemStack stack;

    public ManaContainerItem(ItemStack stack) {
        this.deserialize(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag(), null);
        this.stack = stack;
    }

    @Override
    public long insert(long amount, boolean simulate) {
        long insert = super.insert(amount, simulate);
        if (!simulate) {
            CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            this.serialize(tag, null);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
        return insert;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        long extract = super.extract(amount, simulate);
        if (!simulate) {
            CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            this.serialize(tag, null);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
        return extract;
    }

    @Override
    public void clearContent() {
        super.clearContent();
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        this.serialize(tag, null);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}
