package org.daou.Repository.impl;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.VM;
import org.daou.Exception.MyRepositoryException;
import org.daou.Util.XenServerConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

/**
 * Created by user on 2016-12-07.
 */
@Repository
public class XenRepositoryForSchedulerImpl {

    private static Logger logger = LoggerFactory.getLogger(XenRepositoryForSchedulerImpl.class);

    private Connection connection = XenServerConnectionManager.getInstance();


    public Map<VM, VM.Record> findAllNoCachedVMRecord() throws MyRepositoryException {
        Map<VM, VM.Record> vms = null;

        try{
            vms = VM.getAllRecords(connection);
        }catch (Exception e){
            throw new MyRepositoryException("Location : findAllVMRecord Message : " + e.getMessage());
        }

        return vms;
    }


    /**
     * XenServer API를 통해 지정된 VM의 VM.Record를 Map형태로 받아 반환한다.
     *
     * @param vm the vm
     * @return the vm . record
     * @throws MyRepositoryException the my repository exception
     */
    public VM.Record findOneNoCachedVMRecord(VM vm) throws MyRepositoryException{
        VM.Record record = null;

        try{
            record = vm.getRecord(connection);
        }catch (Exception e){
            throw new MyRepositoryException("Location : findOneNoCachedVMRecord Message : " + e.getMessage());
        }

        return record;
    }

    public VM findOneNoCachedVmByUuid(String uuid) throws MyRepositoryException{
        VM vm = null;
        try{
            logger.info("QQQQQQQQQ123123QQQQ");
            vm = VM.getByUuid(connection,uuid);
            logger.info("QPPPPPPPPQQQ123123QQQQ");
        }catch (Exception e){
            throw new MyRepositoryException("Location : findOneNoCachedVmByUuid Message : " + e.getMessage());
        }finally {
            return vm;
        }
    }

    public Set<VM> findAllNoCachedSnapshotByVm(VM vm){
        Set<VM> snapshots = null;

        try{
            snapshots = vm.getSnapshots(connection);
        }catch (Exception e){
            throw new MyRepositoryException("Location : findOneNoCachedVmByUuid Message : " + e.getMessage());
        }finally {
            return snapshots;
        }
    }
}
