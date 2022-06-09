package section7.app.databases;

import section7.annotations.InitializerMethod;
import section7.annotations.InitializerClass;
import section7.annotations.RetryOperation;

import java.io.IOException;

@InitializerClass
public class DatabaseConnection {

    private int failCounter = 5;

    @RetryOperation(numberOfRetries = 10,
            retryException = IOException.class,
            durationBetweenRetriesMs = 1000,
            failureMessage = "Connection to database 1 failed after retries")
    @InitializerMethod
    public void connectToDatabase1() throws IOException {
        System.out.println( "Connecting to database 1" );

        if ( failCounter > 0 ) {
            failCounter--;
            throw new IOException( "Connection failed" );
        }
    }

    @InitializerMethod
    public void connectToDatabase2() {
        System.out.println( "Connecting to database 2" );
    }

}
