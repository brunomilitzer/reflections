package section7.app.databases;

import section7.annotations.InitializerMethod;
import section7.annotations.InitializerClass;

@InitializerClass
public class CacheLoader {

    @InitializerMethod
    public void loadCache() {
        System.out.println("Loading data from cache");
    }

    public void reloadCache() {
        System.out.println("Reload cache");
    }
}
