package earth.terrarium.botarium.fabric.menu;

import earth.terrarium.botarium.common.menu.ExtraDataMenuProvider;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class ExtraDataMenuProviderWrapper implements ExtendedScreenHandlerFactory<FriendlyByteBuf> {
    private final ExtraDataMenuProvider provider;

    public ExtraDataMenuProviderWrapper(ExtraDataMenuProvider provider) {
        this.provider = provider;
    }

    @Override
    public FriendlyByteBuf getScreenOpeningData(ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        provider.writeExtraData(player, buf);
        return buf;
    }

    @Override
    public Component getDisplayName() {
        return provider.getDisplayName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
        return provider.createMenu(windowId, inventory, player);
    }
}