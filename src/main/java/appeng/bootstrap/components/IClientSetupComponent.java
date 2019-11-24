package appeng.bootstrap.components;

import appeng.bootstrap.IBootstrapComponent;

/**
 * @author BrockWS
 */
@FunctionalInterface
public interface IClientSetupComponent extends IBootstrapComponent {
    void clientSetup();
}
