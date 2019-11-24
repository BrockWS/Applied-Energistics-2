
package appeng.bootstrap.components;


import net.minecraft.tileentity.TileEntityType;

import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.IForgeRegistry;

import appeng.bootstrap.IBootstrapComponent;


/**
 * @author GuntherDW
 */
@FunctionalInterface
public interface ITileEntityRegistrationComponent extends IBootstrapComponent
{
    void tileRegistration(LogicalSide side, IForgeRegistry<TileEntityType<?>> registry);
}
