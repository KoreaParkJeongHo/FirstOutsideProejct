package org.daou.Repository;

import com.xensource.xenapi.Connection;
import com.xensource.xenapi.VM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Created by user on 2016-12-19.
 */

@RunWith(MockitoJUnitRunner.class)
public class XenServerRepositoryTest {

    @Mock
    private VM mockedVM;
    @Mock
    private Connection mockedConnection;

    @InjectMocks
    private XenServerRepository xenServerRepository = new XenServerRepository();

//    @BeforeClass
//    public void init(){
//        try {
//            when(mockedVM.createClone(mockedConnection,anyString())).thenReturn(mockedVM);
//        } catch (Types.XenAPIException e) {
//            e.printStackTrace();
//        } catch (XmlRpcException e) {
//            e.printStackTrace();
//        }
//    }


//    @Test
//    public void findOneVMByUuid() {
//        try {
//            xenServerRepository.findOneVMByUuid(anyString());
//           verify(mockedVM).getByUuid(mockedConnection,anyString());
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//            assertTrue(false);
//        }
//    }

    @Test
    public void deleteOneVM()  {
        try {
            xenServerRepository.deleteOneVM(mockedVM);
            verify(mockedVM).destroy(mockedConnection);
        }catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void startOneVM() {
        try{
            xenServerRepository.startOneVM(mockedVM);
            verify(mockedVM).start(mockedConnection,false,true);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void resumeOneVM()  {
        try{
            xenServerRepository.resumeOneVM(mockedVM);
            verify(mockedVM).resume(mockedConnection,false,true);
        }catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void shutdownOneVm() {
        try{
            xenServerRepository.shutdownOneVm(mockedVM);
            verify(mockedVM).shutdown(mockedConnection);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void revertOneVm()  {
        try{
            xenServerRepository.revertOneVm(mockedVM);
            verify(mockedVM).revert(mockedConnection);
        }catch (Exception e){
            assertTrue(false);
        }
    }

//    @Test
//    public void addOneVM() {
//        try{
//            xenServerRepository.addOneVM(mockedVM);
//            verify(mockedVM).createClone(mockedConnection,anyString());
//            verify(mockedVM).provision(mockedConnection);
//            verify(mockedVM).start(mockedConnection,false,true);
//
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//            assertTrue(false);
//        }
//    }

    @Test
    public void pauseOneVM(){
        try{
            xenServerRepository.pauseOneVM(mockedVM);
            verify(mockedVM).pause(mockedConnection);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void suspendOneVM() throws Exception {
        try{
            xenServerRepository.suspendOneVM(mockedVM);
            verify(mockedVM).suspend(mockedConnection);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void findAllSnapshot() throws Exception {
        try{
            xenServerRepository.findAllSnapshot(mockedVM);
            verify(mockedVM).getSnapshots(mockedConnection);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void findOneVMUuid() throws Exception {
        try{
            xenServerRepository.findOneVMUuid(mockedVM);
            verify(mockedVM).getUuid(mockedConnection);
        }catch (Exception e){
            assertTrue(false);
        }
    }

}