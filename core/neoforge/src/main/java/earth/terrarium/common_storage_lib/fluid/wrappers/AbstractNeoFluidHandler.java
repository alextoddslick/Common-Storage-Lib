package earth.terrarium.common_storage_lib.fluid.wrappers;

import earth.terrarium.common_storage_lib.fluid.util.ConversionUtils;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public interface AbstractNeoFluidHandler extends ResourceHandler<net.neoforged.neoforge.transfer.fluid.FluidResource> {
    CommonStorage<FluidResource> container();

    @Override
    default int size() {
        return container().size();
    }

    @Override
    default net.neoforged.neoforge.transfer.fluid.FluidResource getResource(int index) {
        StorageSlot<FluidResource> slot = container().get(index);
        FluidStack stack = ConversionUtils.convert(slot.getResource(), slot.getAmount());
        return net.neoforged.neoforge.transfer.fluid.FluidResource.of(stack);
    }

    @Override
    default long getAmountAsLong(int index) {
        return container().get(index).getAmount();
    }

    @Override
    default long getCapacityAsLong(int index, net.neoforged.neoforge.transfer.fluid.FluidResource resource) {
        return container().get(index).getLimit(FluidResource.BLANK);
    }

    @Override
    default boolean isValid(int index, net.neoforged.neoforge.transfer.fluid.FluidResource resource) {
        FluidStack stack = resource.toStack(1);
        return container().get(index).isResourceValid(FluidResource.of(stack.getFluid(), stack.getComponentsPatch()));
    }

    @Override
    default int insert(int index, net.neoforged.neoforge.transfer.fluid.FluidResource resource, int amount, TransactionContext transaction) {
        // CSL's CommonStorage doesn't support NeoForge transactions natively.
        FluidStack stack = resource.toStack(amount);
        FluidResource cslResource = ConversionUtils.convert(stack);
        long inserted = container().get(index).insert(cslResource, amount, false);
        UpdateManager.batch(container());
        return (int) inserted;
    }

    @Override
    default int insert(net.neoforged.neoforge.transfer.fluid.FluidResource resource, int amount, TransactionContext transaction) {
        // CSL's CommonStorage doesn't support NeoForge transactions natively.
        FluidStack stack = resource.toStack(amount);
        FluidResource cslResource = ConversionUtils.convert(stack);
        long inserted = container().insert(cslResource, amount, false);
        UpdateManager.batch(container());
        return (int) inserted;
    }

    @Override
    default int extract(int index, net.neoforged.neoforge.transfer.fluid.FluidResource resource, int amount, TransactionContext transaction) {
        // CSL's CommonStorage doesn't support NeoForge transactions natively.
        FluidStack stack = resource.toStack(amount);
        FluidResource cslResource = ConversionUtils.convert(stack);
        long extracted = container().get(index).extract(cslResource, amount, false);
        UpdateManager.batch(container());
        return (int) extracted;
    }

    @Override
    default int extract(net.neoforged.neoforge.transfer.fluid.FluidResource resource, int amount, TransactionContext transaction) {
        // CSL's CommonStorage doesn't support NeoForge transactions natively.
        FluidStack stack = resource.toStack(amount);
        FluidResource cslResource = ConversionUtils.convert(stack);
        long extracted = container().extract(cslResource, amount, false);
        UpdateManager.batch(container());
        return (int) extracted;
    }
}
