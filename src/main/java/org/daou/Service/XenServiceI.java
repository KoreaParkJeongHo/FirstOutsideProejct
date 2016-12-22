package org.daou.Service;

import com.xensource.xenapi.Types;
import com.xensource.xenapi.VM;
import org.daou.Exception.MyRepositoryException;
import org.daou.Exception.MyServiceException;

import java.util.Map;

/**
 * Created by user on 2016-12-20.
 */
public interface XenServiceI {

    /**
     * XenServer API를 통해 모든 VM.Record를 Map형태로 받아 반환한다.
     *
     * @return VM.Records Map
     * @throws MyRepositoryException the my repository exception
     */
    public Map<VM, VM.Record> findAllNoCachedVMRecord() throws MyRepositoryException;


    /**
     * Service Class내에 caching된 VM.Record를 반환한다.
     *
     * @return the map : Chaching된 VM.Record Map
     * @throws MyServiceException : Repository class에서 발생한 Exception,
     */
    public Map<VM, Map<String,Object>> findAllCachedVMRecord() throws MyServiceException, MyRepositoryException;


    /**
     * XenServer API를 통해 하나의 VM.Record를 리턴받는다.
     *
     * @param vm VM.Record정보의 대상 VM
     * @return the vm . record
     * @throws MyRepositoryException Repository class에서 발생한 Exception,
     */
    public VM.Record findOneNoCachedVMRecordByVm(VM vm) throws MyRepositoryException;


    /**
     * Service class내의 caching된 특정 uuid를 갖는 VM의 Snapshot의 VM.Record들을 Map형식으로 반환한다.
     *
     * @param uuid the uuid : caching된 VM의 uuid
     * @return the map : Snapshot들의 VM.Record를 <uuid, VM.Record.topMap()> pair로 갖는다.
     * @throws MyServiceException the my service exception
     */
    public Map<VM, Map<String, Object>> findAllCachedSnapshotRecordByUuid(String uuid) throws MyServiceException;


    /**
     *Service class내의 caching된 특정 uuid를 갖는 VM의 Snapshot의 VM.Record.toMap()을 반환한다.
     *
     * @param uuid the uuid : caching된 VM의 uuid
     * @return the map : : caching된 VM의 toMap()
     * @throws MyServiceException the my service exception
     */
    public Map<String,Object> findOneCachedRecordByUuid(String uuid) throws MyServiceException;

    public void deleteOneCachedRecordByUuid(String uuid) throws MyServiceException;
    public void updateOneCachedRecordStateByUuid(String uuid, Types.VmPowerState newState) throws MyServiceException;
    public VM findOneCachedVMByUuid(String uuid) throws MyServiceException;
    public Map<String,Object> findOneCachedVMRecordByVm(VM vm) throws MyServiceException;
    public void addOneNoCachedVmToCacheByUuid(String clonedVmUuid) throws MyRepositoryException;
    public VM findOneNoCachedVmByUuid(String uuid) throws MyRepositoryException;
    public boolean syncVMWithXenServer(String uuid) throws MyRepositoryException, MyServiceException;
    public void forceSync() throws MyRepositoryException;
    public boolean syncSnapshotWithXenServer(String uuid) throws MyServiceException, MyRepositoryException;
    public Map<VM, Map<String,Object>> findAllNoCachedSnapshotRecordByUuid(String uuid) throws MyServiceException, MyRepositoryException;
}
