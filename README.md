# jPelican
jPelican is a java client to interact to [Pelican Server](https://github.com/mostafa-asg/pelican)  
Ensure pelican server is up and running, then you can connect to it:
```Java
import com.github.pelican.client.PelicanClient;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        PelicanClient pelican = PelicanClient.connect("localhost", 4051);
        pelican.put("MyKey", "MyValue");
        System.out.println( new String(pelican.get("MyKey")) );
    }

}
```
