package earth.terrarium.common_storage_lib.item.wrappers;

import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public record NeoItemHandler(CommonStorage<ItemResource> container) implements ResourceHandler<net.neoforged.neoforge.transfer.item.ItemResource> {
    @Override
    public int size() {
        return container.size();
    }

    @Override
    public net.neoforged.neoforge.transfer.item.ItemResource getResource(int index) {
        StorageSlot<ItemResource> slot = container.get(index);
        ItemStack stack = slot.getResource().toStack((int) slot.getAmount());
        return net.neoforged.neoforge.transfer.item.ItemResource.of(stack);
    }

    @Override
    public long getAmountAsLong(int index) {
        return container.get(index).getAmount();
    }

    @Override
    public long getCapacityAsLong(int index, net.neoforged.neoforge.transfer.item.ItemResource resource) {
        StorageSlot<ItemResource> slot = container.get(index);
        return slot.getLimit(slot.getResource());
    }

    @Override
    public boolean isValid(int index, net.neoforged.neoforge.transfer.item.ItemResource resource) {
        return container.get(index).isResourceValid(ItemResource.of(resource.toStack()));
    }

    @Override
    public int insert(int index, net.neoforged.neoforge.transfer.item.ItemResource resource, int amount, TransactionContext transaction) {
        // CSL's CommonStorage doesn't support NeoForge transactions natively.
        long inserted = container.get(index).insert(ItemResource.of(resource.toStack()), amount, false);
        UpdateManager.batch(container);
        return (int) inserted;
    }

    @Override
    public int extract(int index, net.neoforged.neoforge.transfer.item.ItemResource resource, int amount, TransactionContext transaction) {
        // CSL's CommonStorage doesn't support NeoForge transactions natively.
        long extracted = container.get(index).extract(ItemResource.of(resource.toStack()), amount, false);
        UpdateManager.batch(container);
        return (int) extracted;
    }
}
