package me.flopsterstream.bfutility.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager();

    private final List<Module> modules = new ArrayList<>();

    private ModuleManager() {
        register(new FullbrightModule());
        register(new NofallModule());
        register(new FlightModule());
        register(new AutoTotemModule());
        register(new ZoomModule());
        register(new KillauraModule());
        register(new CriticalsModule());
        register(new BlockEspModule());
        register( new AirJumpModule());
        register( new JetpackModule());
        register( new ReachModule());
        register( new SpeedModule());

    }

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    public void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public Module getModuleByName(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toList());
    }

    public void tickAll() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }
}