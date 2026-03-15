package earth.terrarium.common_storage_lib.energy.wrappers;

import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public record NeoEnergyContainer(ValueStorage container) implements EnergyHandler {
    @Override
    public long getAmountAsLong() {
        return container.getStoredAmount();
    }

    @Override
    public long getCapacityAsLong() {
        return container.getCapacity();
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        // CSL's ValueStorage doesn't support NeoForge transactions natively.
        // We execute the operation directly; rollback is not supported.
        long inserted = container.insert(amount, false);
        UpdateManager.batch(container);
        return (int) inserted;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        // CSL's ValueStorage doesn't support NeoForge transactions natively.
        // We execute the operation directly; rollback is not supported.
        long extracted = container.extract(amount, false);
        UpdateManager.batch(container);
        return (int) extracted;
    }
}
