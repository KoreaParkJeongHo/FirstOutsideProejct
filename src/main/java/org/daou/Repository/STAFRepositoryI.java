package org.daou.Repository;

import com.ibm.staf.STAFResult;
import org.daou.Exception.MyStafException;

/**
 * Created by user on 2016-12-20.
 */
public interface STAFRepositoryI {

    public STAFResult sendRequestToSTAF(String request) throws MyStafException;
}
