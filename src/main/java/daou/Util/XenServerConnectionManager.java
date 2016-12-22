package daou.Util;

import com.xensource.xenapi.APIVersion;
import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Session;

import java.net.URL;


/**
 * Created by user on 2016-12-06.
 */
public class XenServerConnectionManager {

    private static final String address = "http://vm2.terracetech.co.kr";
    private static final String userID = "root";
    private static final String password = "qwopZX!@";

    private static Connection connection;

    private XenServerConnectionManager(){}

    public static synchronized  Connection  getInstance(){

        if(connection == null) {
            try
            {
                URL url = new URL(address);
                connection = new Connection(url);
                Session.loginWithPassword(connection, userID, password, APIVersion.latest().toString());
            } catch (Exception ex)
            {
                System.out.println("Could not connect to XenServer host: " + address);
                System.out.println(ex.getMessage());
            }
        }

        return connection;
    }
}
