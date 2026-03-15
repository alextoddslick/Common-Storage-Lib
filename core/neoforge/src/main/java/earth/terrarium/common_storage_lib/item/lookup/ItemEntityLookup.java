package earth.terrarium.common_storage_lib.item.lookup;

import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.item.wrappers.CommonItemContainer;
import earth.terrarium.common_storage_lib.item.wrappers.NeoItemHandler;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.impl.AutoUpdatingCommonStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ItemEntityLookup<C> implements EntityLookup<CommonStorage<ItemResource>, C>, RegistryEventListener {
    public static final ItemEntityLookup<Void> INSTANCE = new ItemEntityLookup<>(Capabilities.Item.ENTITY);
    public static final ItemEntityLookup<Direction> AUTOMATION = new ItemEntityLookup<>(Capabilities.Item.ENTITY_AUTOMATION);

    private final List<Consumer<EntityRegistrar<CommonStorage<ItemResource>, C>>> registrars = new ArrayList<>();
    private final EntityCapability<ResourceHandler<net.neoforged.neoforge.transfer.item.ItemResource>, C> capability;

    private ItemEntityLookup(EntityCapability<ResourceHandler<net.neoforged.neoforge.transfer.item.ItemResource>, C> capability) {
        this.capability = capability;
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(Entity entity, C context) {
        ResourceHandler<net.neoforged.neoforge.transfer.item.ItemResource> handler = entity.getCapability(capability, context);
        if (handler instanceof NeoItemHandler(CommonStorage<ItemResource> container)) {
            return new AutoUpdatingCommonStorage<>(container);
        }

        if (handler != null) {
            IItemHandler legacyHandler = IItemHandler.of(handler);
            return new CommonItemContainer(legacyHandler);
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<CommonStorage<ItemResource>, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, entityTypes) -> {
            for (EntityType<?> entityType : entityTypes) {
                event.registerEntity(capability, entityType, (entity, direction) -> {
                    CommonStorage<ItemResource> container = getter.getContainer(entity, direction);
                    return container == null ? null : new NeoItemHandler(container);
                });
            }
        }));
    }
}
