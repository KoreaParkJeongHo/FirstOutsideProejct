package org.daou.Util;

import com.ibm.staf.STAFHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by user on 2016-12-07.
 * STAF Service의 Handle객체를 Singleton으로 관리
 */
public class STAFHandleManager {

    private STAFHandleManager(){}
    private static Logger logger = LoggerFactory.getLogger(STAFHandleManager.class);

    private static STAFHandle handle;

    public static synchronized STAFHandle getInstance(){

        if(handle == null) {
            try
            {
                 handle = new STAFHandle("Xenserver Handle");
            } catch (Exception e)
            {
                logger.error("Error : Location : getStafInstance Message : " + e.getMessage());
            }
        }

        return handle;
    }

}

