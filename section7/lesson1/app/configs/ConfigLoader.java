package section7.lesson1.app.configs;

import section7.lesson1.annotations.InitializerClass;
import section7.lesson1.annotations.InitializerMethod;

@InitializerClass
public class ConfigLoader {

    @InitializerMethod
    public void loadAllConfigs() {
        System.out.println("Loading all configuration files");
    }
}
