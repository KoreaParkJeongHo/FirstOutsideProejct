package org.daou.Repository.impl;

import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFResult;
import org.daou.Exception.MyStafException;
import org.daou.Repository.STAFRepositoryI;
import org.daou.Util.STAFHandleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Created by user on 2016-12-20.
 */
@Repository
public class STAFRepositoryImpl implements STAFRepositoryI{

    private static Logger logger = LoggerFactory.getLogger(STAFRepositoryImpl.class);


    private STAFHandle stafHandle = STAFHandleManager.getInstance();

    private final String targetMachine = "local";
    private final String targetService = "xenServer";

    /**
     * request를 받아서 STAF로 request를 전송하는 method
     *
     * @param request : STAF service로 전송될 request
     * @return the int : STAF result message
     * @throws MyStafException the my staf exception
     */
    @Override
    public STAFResult sendRequestToSTAF(String request){

        logger.info("INFO : startVM request : " + request);
        STAFResult stafResult = stafHandle.submit2(targetMachine, targetService, request);

        return stafResult;
    }
}
