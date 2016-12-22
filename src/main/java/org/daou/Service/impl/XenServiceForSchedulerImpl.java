package org.daou.Service.impl;

import com.xensource.xenapi.Types;
import com.xensource.xenapi.VM;
import org.daou.Exception.MyRepositoryException;
import org.daou.Exception.MyServiceException;
import org.daou.Repository.impl.XenRepositoryForSchedulerImpl;
import org.daou.Service.XenServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 2016-12-20.
 */
@Service
public class XenServiceForSchedulerImpl implements XenServiceI {

    private static Logger logger = LoggerFactory.getLogger(XenServiceForSchedulerImpl.class);

    @Autowired
    private XenRepositoryForSchedulerImpl xenServerRepositoryForScheduler;

    private Map<VM, Map<String,Object>> vmRecordCache = new ConcurrentHashMap<>();
    private Map<VM, Map<VM, Map<String,Object>>> snapshotRecordCache = new ConcurrentHashMap<>();

    /**
     //     * 매 1초마다 별도의 Thread로 실행되는 Scheduled Method
     //     * 이는 VMRecordCache객체를 XenServer의 VM List와의 동기화를 위해 수행된다.
     //     * 기존의 Cache에서 변경된 VM과 Snapshot만 update한다.
     */
    //TODO : 해당 Thread가 돌때, Main Thread가 기다리도록 한다.
    @Scheduled(fixedRate = 600000) //1분마다 동기화
    public void vmRecordCacheScheduler() throws MyRepositoryException{
        SyncWithXenServer();
    }

    @Override
    public void forceSync() throws MyRepositoryException{
        SyncWithXenServer();
    }

    private void SyncWithXenServer() throws MyRepositoryException{
        Map<VM, VM.Record> vmRecordCachetemp = findAllNoCachedVMRecord();
        logger.info("INFO : caching....");

        /////////////////////////////////////Delete 작업///////////////////////////////////////
        Iterator<VM> deleteVmIt = vmRecordCache.keySet().iterator();
        VM deleteVmKey = null;

        while(deleteVmIt.hasNext()){
            deleteVmKey = deleteVmIt.next();

            //Xen Server에는 없는데, Cache에는 있다면 cache에서 제거해주자
            if(vmRecordCachetemp.get(deleteVmKey) == null){
                vmRecordCache.remove(deleteVmKey);
            }else{
                //Xen Server, Cache 둘다 있다면 해당 VM의 Snapshot도 확인해주자.

                //새로 들어온 Snapsoht List
                Set<VM> snapshotSet = (LinkedHashSet)vmRecordCachetemp.get(deleteVmKey).toMap().get("snapshots");
                Map<VM,Map<String,Object>> snapshotRecordMap = changeSnapshotSetToMap(snapshotSet);

                //기존에 있던 snapshot List
                Map<VM, Map<String, Object>> snapshotMap = snapshotRecordCache.get(deleteVmKey);

                if(snapshotMap.size() != 0){//기존에 snapshot의 Record가 없을 수도있다.

                    Iterator<VM> deleteSnapshotIt = snapshotMap.keySet().iterator();
                    VM deleteSnapshotKey = null;

                    while (deleteSnapshotIt.hasNext()) {
                        deleteSnapshotKey = deleteSnapshotIt.next(); //이게
                        if (snapshotRecordMap.get(deleteSnapshotKey) == null) {
                            snapshotRecordCache.get(deleteVmKey).remove(deleteSnapshotKey);
                        }
                    }
                }
            }
        }

        /////////////////////////////////////Delete 작업///////////////////////////////////////

        /////////////////////////////////////ADD 작업/////////////////////////////////////////

        Iterator<VM> addIt = vmRecordCachetemp.keySet().iterator();
        VM addKey = null;
        while(addIt.hasNext()){
            addKey = addIt.next();

            //해당 VM이 snapshot과 template이 아닌경우
            if(!(Boolean)vmRecordCachetemp.get(addKey).toMap().get("is_a_snapshot") && !(Boolean)vmRecordCachetemp.get(addKey).toMap().get("is_a_template")){
                logger.info("INFO : " + vmRecordCachetemp.get(addKey).toMap().get("name_label") + " 추가 OR 변경됨");

                vmRecordCache.put(addKey,vmRecordCachetemp.get(addKey).toMap()); //VM Cache Update

                Set<VM> snapshotSet = (LinkedHashSet)vmRecordCachetemp.get(addKey).toMap().get("snapshots");
                Map<VM,Map<String,Object>> snapshotRecordMap = changeSnapshotSetToMap(snapshotSet);

                snapshotRecordCache.put(addKey, snapshotRecordMap); //Snapshot cache Update
            }
        }

        /////////////////////////////////////ADD 작업/////////////////////////////////////////

        logger.info("INFO : caching complete");
    }

    private Map<VM, Map<String,Object>> changeSnapshotSetToMap(Set<VM> snapshotSet) throws MyRepositoryException {
        Map<VM,Map<String,Object>> snapshotRecordMap = new HashMap<>();
        for(VM snapshot : snapshotSet)
            snapshotRecordMap.put(snapshot,findOneNoCachedVMRecordByVm(snapshot).toMap());

        return snapshotRecordMap;
    }

    /**
     * XenServer API를 통해 모든 VM.Record를 리턴받는다.
     *
     * @return the map : VM.Record Map
     * @throws MyRepositoryException : Repository class에서 발생한 Exception,
     */
    @Override
    public Map<VM, VM.Record> findAllNoCachedVMRecord() throws MyRepositoryException {
        return xenServerRepositoryForScheduler.findAllNoCachedVMRecord();
    }


    @Override
    public Map<VM, Map<String,Object>> findAllCachedVMRecord() throws MyServiceException{
        try {
            return vmRecordCache;
        }catch (Exception e){
            throw new MyServiceException("Location : findAllCachedVMRecord Message : " + e.getMessage());
        }
    }

    @Override
    public VM.Record findOneNoCachedVMRecordByVm(VM vm) throws MyRepositoryException{
        return xenServerRepositoryForScheduler.findOneNoCachedVMRecord(vm);
    }

    @Override
    public Map<String,Object> findOneCachedVMRecordByVm(VM vm){
        return vmRecordCache.get(vm);
    }

    @Override
    public VM findOneCachedVMByUuid(String uuid) throws MyServiceException{
        try {
            for(VM vm : vmRecordCache.keySet()){
                if(vmRecordCache.get(vm).get("uuid").equals(uuid))
                    return vm;
            }
        }catch (Exception e){
            throw new MyServiceException("Location : findOneCachedVMByUuid Message : " + e.getMessage());
        }

        return null;
    }


    @Override
    public Map<VM, Map<String,Object>> findAllCachedSnapshotRecordByUuid(String uuid) throws MyServiceException{
        VM vm = null;
        try {
            vm = findOneCachedVMByUuid(uuid);
            return snapshotRecordCache.get(vm);
        }catch (Exception e){
            throw new MyServiceException("Location : findAllCachedVMRecord Message : " + e.getMessage());
        }
    }

    @Override
    public Map<VM, Map<String,Object>> findAllNoCachedSnapshotRecordByUuid(String uuid) throws MyServiceException, MyRepositoryException {
        VM vm = findOneCachedVMByUuid(uuid);
        Map<VM, Map<String,Object>> noCachedSnapshotRecords = new HashMap<>();
        Set<VM> noCachedSnapshots = xenServerRepositoryForScheduler.findAllNoCachedSnapshotByVm(vm);

        if(noCachedSnapshots == null)
            return null;

        for(VM snapshot : noCachedSnapshots){
            noCachedSnapshotRecords.put(snapshot,findOneNoCachedVMRecordByVm(snapshot).toMap());
        }

        return noCachedSnapshotRecords;
    }

    @Override
    public Map<String,Object> findOneCachedRecordByUuid(String uuid) throws MyServiceException{
        VM vm = null;
        try {
            vm = findOneCachedVMByUuid(uuid);
            return vmRecordCache.get(vm);
        }catch (Exception e){
            throw new MyServiceException("Location : findAllCachedVMRecord Message : " + e.getMessage());
        }
    }

    @Override
    public void deleteOneCachedRecordByUuid(String uuid) throws MyServiceException {
        VM vm = null;
        try {
            vm = findOneCachedVMByUuid(uuid);
            vmRecordCache.remove(vm);
            snapshotRecordCache.remove(vm);
        }catch (Exception e){
            throw new MyServiceException("Location : findAllCachedVMRecord Message : " + e.getMessage());
        }
    }

    @Override
    public void updateOneCachedRecordStateByUuid(String uuid, Types.VmPowerState newState) throws MyServiceException {

        VM vm = null;
        try {
            vm = findOneCachedVMByUuid(uuid);
            vmRecordCache.get(vm).put("power_state",newState);
        }catch (Exception e){
            throw new MyServiceException("Location : findAllCachedVMRecord Message : " + e.getMessage());
        }
    }

    @Override
    public void addOneNoCachedVmToCacheByUuid(String clonedVmUuid) throws MyRepositoryException{
        VM clonedVm = findOneNoCachedVmByUuid(clonedVmUuid);
        VM.Record clonedVmRecord = findOneNoCachedVMRecordByVm(clonedVm);
        vmRecordCache.put(clonedVm, clonedVmRecord.toMap());
    }

    @Override
    public VM findOneNoCachedVmByUuid(String uuid) throws MyRepositoryException{
        return xenServerRepositoryForScheduler.findOneNoCachedVmByUuid(uuid);
    }

    @Override
    public boolean syncVMWithXenServer(String uuid) throws MyRepositoryException, MyServiceException{
        VM cachedVm = findOneCachedVMByUuid(uuid);
        VM nonCachedVm = findOneNoCachedVmByUuid(uuid);

        if(nonCachedVm == null)//해당 VM이 Xen Server에서는 삭제되어 있음
            return false;

        VM.Record nonCachedVmRecord = findOneNoCachedVMRecordByVm(nonCachedVm);

        if(!nonCachedVmRecord.toMap().get("power_state").equals(vmRecordCache.get(cachedVm).get("power_state")))//해당 VM이 Xen Server의 VM과 State상태가 일치하지 않음
            return false;

        return true;
    }

    @Override
    public boolean syncSnapshotWithXenServer(String uuid) throws MyServiceException, MyRepositoryException {
        Map<VM, Map<String,Object>> cachedSnapshot = findAllCachedSnapshotRecordByUuid(uuid);
        Map<VM, Map<String,Object>> noCachedSnapshot = findAllNoCachedSnapshotRecordByUuid(uuid);

        if(noCachedSnapshot == null)//해당 VM의 Snapshot의 모든 List는 이미 삭제되어 있음
            return false;

        if(cachedSnapshot.size() != noCachedSnapshot.size())//둘의 갯수가 같지 않다는 것은 싱크가 맞지않음.
            return false;

        Iterator<VM> it = cachedSnapshot.keySet().iterator();

        VM key = null;
        while(it.hasNext()){ //하나라도 해당 snapshot에대한 Record가 없다면 싱크가 맞지않음.
            key = it.next();
            if(noCachedSnapshot.get(key) == null)
                return false;
        }

        return true;
    }
}
