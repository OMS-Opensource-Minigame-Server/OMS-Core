package fun.reallyisnt.oms.core.modules;

import fun.reallyisnt.oms.core.modules.dms.DMModule;

public enum Modules {
    DMS(DMModule.class);

    private Class<? extends Module> clazz;

    Modules(Class<? extends Module> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Module> getClazz() {
        return this.clazz;
    }
}
