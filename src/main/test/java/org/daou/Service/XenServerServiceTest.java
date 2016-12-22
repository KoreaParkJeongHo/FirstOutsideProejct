package org.daou.Service;

import com.xensource.xenapi.VM;
import org.daou.Exception.MyRepositoryException;
import org.daou.Repository.XenServerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by user on 2016-12-19.
 */
@RunWith(MockitoJUnitRunner.class)
public class XenServerServiceTest {

    @Mock
    private XenServerRepository mockedXenServerRepository;
    @Mock
    private VM mockedVM;

    @InjectMocks
    private  XenServerService xenServerService = new XenServerService();

    @Test
    public void findOneVMByUuid(){

        try {
            xenServerService.findOneVMByUuid(anyString());
            verify(mockedXenServerRepository).findOneVMByUuid(anyString()); //호출 확인
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }

    }

    @Test
    public void findOneSnapshotByVMAndUuid() {
        try {
            xenServerService.findOneSnapshotByVMAndUuid(mockedVM, anyString());
            verify(mockedXenServerRepository).findAllSnapshot(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

    @Test
    public void findOneVMUuid(){
        try {
            xenServerService.findOneVMUuid(mockedVM);
            verify(mockedXenServerRepository).findOneVMUuid(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

    @Test
    public void destroyOneVM() {
        try {
            xenServerService.destroyOneVM(mockedVM);
            verify(mockedXenServerRepository).deleteOneVM(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

    @Test
    public void startOneVM() {
        try {
            xenServerService.startOneVM(mockedVM);
            verify(mockedXenServerRepository).startOneVM(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

    @Test
    public void resumeOneVM() {
        try {
            xenServerService.resumeOneVM(mockedVM);
            verify(mockedXenServerRepository).resumeOneVM(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

    @Test
    public void shutdownOneVM() {
        try {
            xenServerService.shutdownOneVM(mockedVM);
            verify(mockedXenServerRepository).shutdownOneVm(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

    @Test
    public void revertOneSnapshot()  {
        try {
            xenServerService.revertOneSnapshot(mockedVM);
            verify(mockedXenServerRepository).revertOneVm(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

    @Test
    public void pauseOneVM() {
        try {
            xenServerService.pauseOneVM(mockedVM);
            verify(mockedXenServerRepository).pauseOneVM(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

    @Test
    public void addOneVM()  {
        try {
            xenServerService.addOneVM(mockedVM);
            verify(mockedXenServerRepository).addOneVM(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

    @Test
    public void suspendOneVM() {
        try {
            xenServerService.suspendOneVM(mockedVM);
            verify(mockedXenServerRepository).suspendOneVM(mockedVM);
        } catch (MyRepositoryException e) {
            assertTrue(false);
        }
    }

}