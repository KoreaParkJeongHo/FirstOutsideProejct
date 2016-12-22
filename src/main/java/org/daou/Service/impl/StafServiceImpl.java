package org.daou.Service.impl;

import com.ibm.staf.STAFResult;
import org.daou.Exception.MyStafException;
import org.daou.Repository.STAFRepositoryI;
import org.daou.Service.StafServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2016-12-20.
 */
@Service
public class StafServiceImpl implements StafServiceI{

    @Autowired
    private STAFRepositoryI stafRepository;


    @Override
    public void deleteOneVMByVMUuid(String uuid) throws MyStafException{
        String request = "DELETE vmId " + uuid;  //STAF를 통해 Xenserver VM객체 삭제.
        STAFResult stafResult = stafRepository.sendRequestToSTAF(request);

        if(stafResult.rc != STAFResult.Ok)
            throw new MyStafException("ERROR : STAF DELETE REQUEST");
    }


    @Override
    public void startOneVMByUuid(String uuid) throws MyStafException{
        String request = "START vmId " + uuid;  //STAF를 통해 Xenserver VM객체 start
        STAFResult stafResult = stafRepository.sendRequestToSTAF(request);

        if(stafResult.rc != STAFResult.Ok)
            throw new MyStafException("ERROR : STAF START REQUEST");
    }



    @Override
    public void resumeOneVMByUuid(String uuid) throws MyStafException{
        String request = "RESUME vmId " + uuid;  //STAF를 통해 Xenserver VM객체 start
        STAFResult stafResult = stafRepository.sendRequestToSTAF(request);

        if(stafResult.rc != STAFResult.Ok)
            throw new MyStafException("ERROR : STAF RESUME REQUEST");
    }


    @Override
    public void suspendOneVMByUuid(String uuid) throws MyStafException{
        String request = "SUSPEND vmId " + uuid;  //STAF를 통해 Xenserver VM객체 start
        STAFResult stafResult = stafRepository.sendRequestToSTAF(request);

        if(stafResult.rc != STAFResult.Ok)
            throw new MyStafException("ERROR : STAF SUSPEND REQUEST");
    }



    @Override
    public void shutdownVMByUuid(String uuid) throws MyStafException{
        String request = "STOP vmId " + uuid;  //STAF를 통해 Xenserver VM객체 삭제.
        STAFResult stafResult = stafRepository.sendRequestToSTAF(request);

        if(stafResult.rc != STAFResult.Ok)
            throw new MyStafException("ERROR : STAF SHUTDOWN REQUEST");
    }


    @Override
    public void pauseVMByUuid(String uuid) throws MyStafException{
        String request = "PAUSE vmId " + uuid;
        STAFResult stafResult = stafRepository.sendRequestToSTAF(request);

        if(stafResult.rc != STAFResult.Ok)
            throw new MyStafException("ERROR : STAF PAUSE REQUEST");
    }



    @Override
    public String addOneVMByVMUuidAndSanpshotUuid (String VMUuid, String snapshotUuid) throws MyStafException{
        String request = "ADD vmId " + VMUuid + " snapshotId " + snapshotUuid;
        STAFResult stafResult = stafRepository.sendRequestToSTAF(request);

        if(stafResult.rc != STAFResult.Ok)
            throw new MyStafException("ERROR : STAF ADD REQUEST");

        return stafResult.result; //새로 추가된 VM의 uuid
    }


    @Override
    public void revertOneSnapshotByVMUuidAndSnapshotUuid(String VMUuid, String snapshotUuid) throws  MyStafException{
        String request = "REVERT vmId " + VMUuid + " snapshotId " + snapshotUuid;
        STAFResult stafResult = stafRepository.sendRequestToSTAF(request);

        if(stafResult.rc != STAFResult.Ok)
            throw new MyStafException("ERROR : STAF REVERT REQUEST");
    }
}
