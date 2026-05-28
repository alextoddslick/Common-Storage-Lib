package earth.terrarium.common_storage_lib.resources.fluid.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public final class FluidAmounts {
    public static final long BUCKET = FluidConstants.BUCKET;
    public static final long BOTTLE = FluidConstants.BOTTLE;
    public static final long BLOCK = FluidConstants.BUCKET;
    public static final long INGOT = FluidConstants.INGOT;
    public static final long NUGGET = FluidConstants.NUGGET;

    private FluidAmounts() {
    }

    public static long toPlatformAmount(long millibuckets) {
        return FluidConstants.fromBucketFraction(millibuckets, 1000);
    }

    public static long toMillibuckets(long platformAmount) {
        return (platformAmount * 1000) / FluidConstants.BUCKET;
    }
}
