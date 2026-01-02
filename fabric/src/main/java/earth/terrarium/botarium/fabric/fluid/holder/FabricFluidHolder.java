package earth.terrarium.botarium.fabric.fluid.holder;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;

@SuppressWarnings("UnstableApiUsage")
public class FabricFluidHolder extends SnapshotParticipant<FabricFluidHolder>
        implements FluidHolder, StorageView<FluidVariant> {
    private FluidVariant fluidVariant;
    private long amount;

    private FabricFluidHolder(FluidVariant variant, long amount) {
        this.fluidVariant = variant;
        this.amount = amount;
    }

    public static FabricFluidHolder of(FluidVariant variant, long amount) {
        return new FabricFluidHolder(variant, amount);
    }

    public static FabricFluidHolder of(Fluid variant, long amount, CompoundTag compoundTag) {
        return new FabricFluidHolder(FluidVariant.of(variant, patchFromTag(compoundTag)), amount);
    }

    public static FabricFluidHolder of(FluidHolder fluidHolder) {
        return new FabricFluidHolder(FluidVariant.of(fluidHolder.getFluid(), patchFromTag(fluidHolder.getCompound())),
                fluidHolder.getFluidAmount());
    }

    public FluidVariant toVariant() {
        return FluidVariant.of(this.getFluid(), patchFromTag(this.getCompound()));
    }

    @Override
    public Fluid getFluid() {
        return fluidVariant.getFluid();
    }

    @Override
    public void setFluid(Fluid fluid) {
        fluidVariant = FluidVariant.of(fluid);
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (this.fluidVariant.equals(resource)) {
            long extracted = (long) Mth.clamp(maxAmount, 0, this.getFluidAmount());
            this.updateSnapshots(transaction);
            this.amount -= extracted;
            return extracted;
        }
        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        return fluidVariant.isBlank();
    }

    @Override
    public FluidVariant getResource() {
        return this.toVariant();
    }

    @Override
    public long getAmount() {
        return getFluidAmount();
    }

    @Override
    public long getFluidAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public CompoundTag getCompound() {
        java.util.Optional<? extends CustomData> optional = this.fluidVariant.getComponents()
                .get(DataComponents.CUSTOM_DATA);
        CustomData data = optional == null ? null : optional.orElse(null);
        return data == null ? new CompoundTag() : data.copyTag();
    }

    @Override
    public void setCompound(CompoundTag tag) {
        this.fluidVariant = FluidVariant.of(fluidVariant.getFluid(), patchFromTag(tag));
    }

    @Override
    public boolean isEmpty() {
        return this.fluidVariant.isBlank() || amount == 0;
    }

    @Override
    public boolean matches(FluidHolder fluidHolder) {
        return this.fluidVariant.isOf(fluidHolder.getFluid())
                && nbtMatches(this.getCompound(), fluidHolder.getCompound());
    }

    private boolean nbtMatches(CompoundTag tag1, CompoundTag tag2) {
        return (tag1 == null && tag2 == null) || (tag1 != null && tag1.equals(tag2));
    }

    @Override
    public FabricFluidHolder copyHolder() {
        return FabricFluidHolder.of(getFluid(), getFluidAmount(), getCompound() == null ? null : getCompound().copy());
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("Fluid", BuiltInRegistries.FLUID.getKey(getFluid()).toString());
        compoundTag.putLong("Amount", getFluidAmount());
        if (this.getCompound() != null) {
            compoundTag.put("Nbt", getCompound());
        }
        return compoundTag;
    }

    @Override
    public void deserialize(CompoundTag compound) {
        this.amount = compound.getLong("Amount");
        CompoundTag tag = null;
        if (compound.contains("Nbt")) {
            tag = compound.getCompound("Nbt");
        }
        this.fluidVariant = FluidVariant
                .of(BuiltInRegistries.FLUID.get(ResourceLocation.parse(compound.getString("Fluid"))),
                        patchFromTag(tag));
    }

    @Override
    protected FabricFluidHolder createSnapshot() {
        return this.copyHolder();
    }

    @Override
    protected void readSnapshot(FabricFluidHolder snapshot) {
        this.fluidVariant = FluidVariant.of(snapshot.getFluid(), patchFromTag(snapshot.getCompound()));
        this.setAmount(snapshot.getFluidAmount());
    }

    public static FabricFluidHolder empty() {
        return new FabricFluidHolder(FluidVariant.blank(), 0);
    }

    public static DataComponentPatch patchFromTag(CompoundTag tag) {
        if (tag == null || tag.isEmpty())
            return DataComponentPatch.EMPTY;
        return DataComponentPatch.builder().set(DataComponents.CUSTOM_DATA, CustomData.of(tag)).build();
    }
}
