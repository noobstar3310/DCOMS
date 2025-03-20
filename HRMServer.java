
package dcomsassignment;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class HRMServer {
    public static void main(String[] args) {
        try {
            System.setProperty("javax.net.debug", "ssl,handshake");
            System.setProperty("javax.net.ssl.keyStore", "C:\\Program Files\\Java\\jdk-21\\bin\\keystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "password");
            System.setProperty("javax.net.ssl.keyStoreType", "JKS");
            
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
             System.out.println("HRM Server is running on IP: " + ipAddress + " Port: 1099");
            // Create the remote object (it will be automatically exported)
            HRMServiceImpl hrm = new HRMServiceImpl();
            
            HRMService stub = (HRMService) UnicastRemoteObject.exportObject(
                    hrm,
                    0,  // ephemeral port
                    new SslRMIClientSocketFactory(),
                    new SslRMIServerSocketFactory()
            );
            
            // Create the RMI registry
            Registry registry = LocateRegistry.createRegistry(1099); // Default port 1099
            
            // Bind the remote object to the RMI registry
            registry.rebind("HRMService", stub);
            
            System.out.println("HRM Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}