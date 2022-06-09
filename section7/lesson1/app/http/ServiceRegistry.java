package section7.lesson1.app.http;

import section7.lesson1.annotations.InitializerClass;
import section7.lesson1.annotations.InitializerMethod;

@InitializerClass
public class ServiceRegistry {

    @InitializerMethod
    public void registerService() {
        System.out.println("Service successfully registered");
    }
}
