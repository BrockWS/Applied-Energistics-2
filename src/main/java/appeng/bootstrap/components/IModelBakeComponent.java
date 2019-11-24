package appeng.bootstrap.components;

import java.util.Map;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ModelLoader;

import appeng.bootstrap.IBootstrapComponent;

/**
 * @author BrockWS
 */
@FunctionalInterface
public interface IModelBakeComponent extends IBootstrapComponent {

    void modelBake(ModelManager modelManager, ModelLoader modelLoader, Map<ResourceLocation, IBakedModel> modelRegistry);
}
