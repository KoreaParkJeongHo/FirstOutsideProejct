package daou.Repository;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.VM;
import daou.Exception.MyRepositoryException;
import daou.Util.XenServerConnectionManager;

import java.util.Set;

/**
 * Created by user on 2016-12-07.
 */

public class XenServerRepository {

    private Connection connection = XenServerConnectionManager.getInstance();

    public VM findOneVMByUuid(String uuid) throws MyRepositoryException{
        VM vm = null;

        try{
            vm = VM.getByUuid(connection,uuid);
        }catch (Exception e){
            throw new MyRepositoryException("Location : findOneVMByUuid Message : " + e.getMessage());
        }
        return vm;
    }

    public void deleteOneVM(VM vm) throws MyRepositoryException{
        try{
            vm.destroy(connection);
        }catch (Exception e){
            throw new MyRepositoryException("Location : deleteOneVM Message : " + e.getMessage());
        }
    }

    public void startOneVM(VM vm) throws MyRepositoryException{
        try{
            vm.start(connection,false,true);
        }catch (Exception e){
            throw new MyRepositoryException("Location : startOneVM Message : " + e.getMessage());
        }
    }

    public void resumeOneVM(VM vm) throws MyRepositoryException{
        try{
            vm.resume(connection,false,true);
        }catch (Exception e){
            throw new MyRepositoryException("Location : resumeOneVM Message : " + e.getMessage());
        }
    }

    public void shutdownOneVm(VM vm) throws MyRepositoryException{
        try{
            vm.shutdown(connection);
        }catch (Exception e){
            throw new MyRepositoryException("Location : shutdownOneVm Message : " + e.getMessage());
        }
    }

    public void revertOneVm(VM vm) throws MyRepositoryException{
        try{
            vm.revert(connection);
        }catch (Exception e){
            throw new MyRepositoryException("Location : revertOneVm Message : " + e.getMessage());
        }
    }

    public VM addOneVM(VM vm) throws MyRepositoryException{
        VM clonedVM = null;
        try{
            String newName = vm.getNameLabel(connection);
            clonedVM = vm.createClone(connection, newName + "'s clone");
            clonedVM.provision(connection);
            clonedVM.start(connection,false,true);
        }catch (Exception e){
            throw new MyRepositoryException("Location : addOneVM Message : " + e.getMessage());
        }

        return clonedVM;
    }

    public void pauseOneVM(VM vm) throws MyRepositoryException{
        try{
            vm.pause(connection);
        }catch (Exception e){
            throw new MyRepositoryException("Location : suspendOneVM Message : " + e.getMessage());
        }
    }

    public void suspendOneVM(VM vm) throws MyRepositoryException{
        try{
            vm.suspend(connection);
        }catch (Exception e){
            throw new MyRepositoryException("Location : suspendOneVM Message : " + e.getMessage());
        }
    }

    public Set<VM> findAllSnapshot(VM vm) throws MyRepositoryException{
        Set<VM> vms = null;

        try{
            vms = vm.getSnapshots(connection);

        }catch (Exception e){
            throw new MyRepositoryException("Location : findAllSnapshot Message : " + e.getMessage());
        }
        return vms;
    }

    public String findOneVMUuid(VM vm) throws MyRepositoryException{
        String uuid = null;

        try{
            uuid = vm.getUuid(connection);
        }catch (Exception e){
            throw new MyRepositoryException("Location : findOneVMUuid Message : " + e.getMessage());
        }

        return uuid;
    }



}
