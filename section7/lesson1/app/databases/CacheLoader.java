package section7.lesson1.app.databases;

import section7.lesson1.annotations.InitializerClass;
import section7.lesson1.annotations.InitializerMethod;

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
