package org.daou.Util;

import com.xensource.xenapi.APIVersion;
import com.xensource.xenapi.Connection;
import com.xensource.xenapi.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;


/**
 * Created by user on 2016-12-06.
 * XenServer의 Connection객체를 Singleton으로 관리
 */
public class XenServerConnectionManager {

    private static Logger logger = LoggerFactory.getLogger(XenServerConnectionManager.class);

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
            } catch (Exception e)
            {
                logger.error("Error : Location : getXenServerConnectionInstance Message : " + e.getMessage());
            }
        }

        return connection;
    }
}
