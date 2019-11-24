package appeng.bootstrap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.Item;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.util.AEColor;
import appeng.api.util.IColoredItemDefinition;
import appeng.bootstrap.components.IItemRegistrationComponent;
import appeng.bootstrap.components.ILoadCompleteComponent;
import appeng.core.AppEng;
import appeng.core.features.AEFeature;
import appeng.core.features.ActivityState;
import appeng.core.features.ColoredItemDefinition;
import appeng.core.features.ItemStackSrc;
import appeng.util.Platform;

/**
 * @author BrockWS
 */
public class AEColoredItemDefinitionBuilder implements IAEColoredItemBuilder {

    private final FeatureFactory factory;
    private final String registryName;
    private final List<AEColor> validColors;
    private final Function<AEColor, Item> itemFunction;
    private final EnumSet<AEFeature> features = EnumSet.noneOf(AEFeature.class);
    private final List<Function<Item, IBootstrapComponent>> boostrapComponents = new ArrayList<>();
    private Supplier<IDispenseItemBehavior> dispenserBehaviorSupplier;
    @OnlyIn(Dist.CLIENT)
    private ItemRendering itemRendering;

    AEColoredItemDefinitionBuilder(FeatureFactory factory, String registryName, List<AEColor> validColors, Function<AEColor, Item> itemFunction) {
        this.factory = factory;
        this.registryName = registryName;
        this.validColors = validColors;
        this.itemFunction = itemFunction;
        if (Platform.isClient()) {
            this.itemRendering = new ItemRendering();
        }
    }

    @Override
    public IAEColoredItemBuilder bootstrap(Function<Item, IBootstrapComponent> component) {
        this.boostrapComponents.add(component);
        return this;
    }

    @Override
    public IAEColoredItemBuilder features(AEFeature... features) {
        this.features.clear();
        this.addFeatures(features);
        return this;
    }

    @Override
    public IAEColoredItemBuilder addFeatures(AEFeature... features) {
        Collections.addAll(this.features, features);
        return this;
    }

    @Override
    public IAEColoredItemBuilder rendering(ItemRenderingCustomizer callback) {
        if (Platform.isClient()) {
            callback.customize(this.itemRendering);
        }
        return this;
    }

    @Override
    public IAEColoredItemBuilder dispenserBehavior(Supplier<IDispenseItemBehavior> behavior) {
        this.dispenserBehaviorSupplier = behavior;
        return this;
    }

    @Override
    public IColoredItemDefinition build() {
        ColoredItemDefinition definition = new ColoredItemDefinition();
        this.validColors.forEach(color -> {
            Item item = this.itemFunction.apply(color);
            item.setRegistryName(AppEng.MOD_ID, String.format(this.registryName, color));

            // Register all extra handlers
            this.boostrapComponents.forEach(component -> this.factory.addBootstrapComponent(component.apply(item)));

            // Register custom dispenser behavior if requested
            if (this.dispenserBehaviorSupplier != null) {
                this.factory.addBootstrapComponent((ILoadCompleteComponent) () -> {
                    IDispenseItemBehavior behavior = this.dispenserBehaviorSupplier.get();
                    DispenserBlock.registerDispenseBehavior(item, behavior);
                });
            }

            this.factory.addBootstrapComponent((IItemRegistrationComponent) (reg) -> reg.register(item));

            if (Platform.isClient()) {
                this.itemRendering.apply(this.factory, item);
            }

            definition.add(color, new ItemStackSrc(item, ActivityState.Enabled));
        });
        return definition;
    }
}
