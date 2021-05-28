package fun.reallyisnt.oms.core.modules.dms;

import fun.reallyisnt.oms.core.modules.Module;

public class DMModule extends Module {

    public DMModule() {
        super("dms");
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {

    }
}
