package section7.app.http;

import section7.annotations.InitializerMethod;
import section7.annotations.InitializerClass;

@InitializerClass
public class ServiceRegistry {

    @InitializerMethod
    public void registerService() {
        System.out.println("Service successfully registered");
    }
}
