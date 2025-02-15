
package dcomsassignment;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HRMServer {
    public static void main(String[] args) {
        try {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
             System.out.println("HRM Server is running on IP: " + ipAddress + " Port: 1099");
            // Create the remote object (it will be automatically exported)
            HRMServiceImpl hrm = new HRMServiceImpl();
            
            // Create the RMI registry
            Registry registry = LocateRegistry.createRegistry(1099); // Default port 1099
            
            // Bind the remote object to the RMI registry
            registry.rebind("HRMService", hrm);
            
            System.out.println("HRM Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}