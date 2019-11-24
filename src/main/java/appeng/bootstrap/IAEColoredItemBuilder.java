package appeng.bootstrap;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.Item;

import appeng.api.util.IColoredItemDefinition;
import appeng.core.features.AEFeature;

/**
 * @author BrockWS
 */
public interface IAEColoredItemBuilder {
    IAEColoredItemBuilder bootstrap( Function<Item, IBootstrapComponent> component );

    IAEColoredItemBuilder features( AEFeature... features );

    IAEColoredItemBuilder addFeatures( AEFeature... features );

    IAEColoredItemBuilder rendering( ItemRenderingCustomizer callback );

    /**
     * Registers a custom dispenser behavior for this item.
     */
    IAEColoredItemBuilder dispenserBehavior( Supplier<IDispenseItemBehavior> behavior );

    IColoredItemDefinition build();
}
