package section7.lesson1.app;

import section7.lesson1.annotations.InitializerClass;
import section7.lesson1.annotations.InitializerMethod;

@InitializerClass
public class AutoSaver {

    @InitializerMethod
    public void startAutoSavingThreads() {
        System.out.println("Start automatic data saving to disk");
    }
}
