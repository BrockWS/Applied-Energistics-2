
package appeng.bootstrap.components;


import net.minecraft.item.Item;

import net.minecraftforge.registries.IForgeRegistry;

import appeng.bootstrap.IBootstrapComponent;


@FunctionalInterface
public interface IItemRegistrationComponent extends IBootstrapComponent {
    void itemRegistration(IForgeRegistry<Item> itemRegistry);
}
