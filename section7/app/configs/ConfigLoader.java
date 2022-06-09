package section7.app.configs;

import section7.annotations.InitializerMethod;
import section7.annotations.InitializerClass;

@InitializerClass
public class ConfigLoader {

    @InitializerMethod
    public void loadAllConfigs() {
        System.out.println("Loading all configuration files");
    }
}
