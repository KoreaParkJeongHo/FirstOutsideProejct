package daou.Service;

import com.xensource.xenapi.VM;
import daou.Exception.MyRepositoryException;
import daou.Repository.XenServerRepository;

import java.util.Set;

/**
 * Created by user on 2016-12-07.
 */
public class XenServerService {

    private XenServerRepository xenServerRepository;

    public XenServerService(){

        xenServerRepository = new XenServerRepository();
    }


    public VM findOneVMByUuid(String uuid) throws MyRepositoryException {
        return xenServerRepository.findOneVMByUuid(uuid);
    }




    /**
     * VM안의 uuid를 갖는 snapshot을 찾는다.
     *
     * @param vm   the vm
     * @param uuid the snapshot의 uuid
     * @return the snapshot
     */
    public VM findOneSnapshotByVMAndUuid(VM vm, String uuid) throws MyRepositoryException{
        Set<VM> vms = null;
        VM snapshot = null;
        vms = xenServerRepository.findAllSnapshot(vm);

        for(VM v : vms){
            if(xenServerRepository.findOneVMUuid(v).equals(uuid)) {
                snapshot = v;
                break;
            }
        }

        return snapshot;
    }

    public String findOneVMUuid(VM vm) throws MyRepositoryException{
        return xenServerRepository.findOneVMUuid(vm);
    }

    public void destroyOneVM(VM vm) throws MyRepositoryException{
        xenServerRepository.deleteOneVM(vm);
    }

    public void startOneVM(VM vm) throws MyRepositoryException{
        xenServerRepository.startOneVM(vm);
    }

    public void resumeOneVM(VM vm) throws MyRepositoryException{
        xenServerRepository.resumeOneVM(vm);
    }

    public void shutdownOneVM(VM vm) throws MyRepositoryException{
        xenServerRepository.shutdownOneVm(vm);
    }

    public void revertOneSnapshot(VM vm) throws MyRepositoryException{
        xenServerRepository.revertOneVm(vm);
    }

    public void pauseOneVM(VM vm) throws MyRepositoryException{
        xenServerRepository.pauseOneVM(vm);
    }

    public VM addOneVM(VM vm) throws MyRepositoryException{
        return xenServerRepository.addOneVM(vm);
    }

    public void suspendOneVM(VM vm) throws MyRepositoryException{
        xenServerRepository.suspendOneVM(vm);
    }
}
