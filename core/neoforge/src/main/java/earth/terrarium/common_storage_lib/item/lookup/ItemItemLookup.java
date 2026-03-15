package earth.terrarium.common_storage_lib.item.lookup;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.context.impl.ModifyOnlyContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.item.wrappers.CommonItemContainerItem;
import earth.terrarium.common_storage_lib.item.wrappers.NeoItemHandler;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ItemItemLookup implements ItemLookup<CommonStorage<ItemResource>, ItemContext>, RegistryEventListener {
    public static final ItemItemLookup INSTANCE = new ItemItemLookup();
    private static final ItemCapability<CommonStorage<ItemResource>, ItemContext> CAPABILITY = ItemCapability.create(Identifier.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "item_item"), CommonStorage.asClass(), ItemContext.class);

    private final List<Consumer<ItemRegistrar<CommonStorage<ItemResource>, ItemContext>>> registrars = new ArrayList<>();

    private ItemItemLookup() {
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(ItemStack stack, ItemContext context) {
        CommonStorage<ItemResource> capability = stack.getCapability(CAPABILITY, context);
        if (capability != null) {
            return capability;
        }
        if (!stack.isEmpty()) {
            ItemAccess access = ItemAccess.forStack(stack);
            ResourceHandler<net.neoforged.neoforge.transfer.item.ItemResource> handler = stack.getCapability(Capabilities.Item.ITEM, access);
            if (handler != null) {
                IItemHandler legacyHandler = IItemHandler.of(handler);
                return new CommonItemContainerItem(legacyHandler, stack, context);
            }
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<CommonStorage<ItemResource>, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, containers) -> {
            event.registerItem(CAPABILITY, getter::getContainer, containers);

            event.registerItem(Capabilities.Item.ITEM, (stack, ignored) -> {
                ModifyOnlyContext context = new ModifyOnlyContext(stack);
                CommonStorage<ItemResource> container = getter.getContainer(stack, context);
                return container == null ? null : new NeoItemHandler(container);
            }, containers);
        }));
    }
}
