package fun.reallyisnt.oms.core.modules.dms;

import fun.reallyisnt.oms.common.Config;
import fun.reallyisnt.oms.core.Module;
import fun.reallyisnt.oms.core.OMSCore;
import fun.reallyisnt.oms.core.modules.dms.listeners.LeaveListener;
import fun.reallyisnt.oms.core.modules.dms.listeners.JoinListener;

public class DMModule extends Module {

    public DMModule() {
        super("dms");
    }

    private static DMModule instance;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        DMModule.instance = this;

        if (OMSCore.getInstance().getConfigString(Config.REDIS_ENABLED).equalsIgnoreCase("true")) {
            this.getServer().getPluginManager().registerEvents(new JoinListener(),OMSCore.getInstance());
            this.getServer().getPluginManager().registerEvents(new LeaveListener(),OMSCore.getInstance());
        }


    }

    @Override
    public void onDisable() {
        //TODO: Unsubscribe from all channels
    }

    public static DMModule getInstance() {
        return instance;
    }
}
