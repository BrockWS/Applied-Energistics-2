package appeng.bootstrap.components;

import appeng.bootstrap.IBootstrapComponent;

/**
 * @author BrockWS
 */
@FunctionalInterface
public interface IServerSetupComponent extends IBootstrapComponent {
    void serverSetup();
}
