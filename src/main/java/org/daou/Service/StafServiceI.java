package org.daou.Service;

import org.daou.Exception.MyStafException;

public interface StafServiceI {


    /**
     * 특정 uuid를 갖는 VM을 삭제하는 요청을 STAF service로 보낸다.
     *
     * @param uuid the uuid 삭제할 VM의 uuid
     * @throws MyStafException the my staf exception
     */
    public void deleteOneVMByVMUuid(String uuid) throws MyStafException;

    /**
     * 특정 uuid를 갖는 VM을 가동하는 요청을 STAF service로 보낸다.
     *
     * @param uuid the uuid 가동할 VM의 uuid
     * @throws MyStafException the my staf exception
     */
    public void startOneVMByUuid(String uuid) throws MyStafException;

    /**
     * 특정 uuid를 갖는 VM을 재가동하는 요청을 STAF service로 보낸다.
     *
     * @param uuid the uuid 재가동할 VM의 uuid
     * @throws MyStafException the my staf exception
     */
    public void resumeOneVMByUuid(String uuid) throws MyStafException;

    /**
     * 특정 uuid를 갖는 VM을 일시정지하는 요청을 STAF service로 보낸다.
     *
     * @param uuid the uuid 일시정지할 VM의 uuid
     * @throws MyStafException the my staf exception
     */
    public void suspendOneVMByUuid(String uuid) throws MyStafException;

    /**
     * 특정 uuid를 갖는 VM을 중단하는 요청을 STAF service로 보낸다.
     *
     * @param uuid the uuid 중단할 VM의 uuid
     * @throws MyStafException the my staf exception
     */
    public void shutdownVMByUuid(String uuid) throws MyStafException;

    /**
     * 특정 uuid를 갖는 VM을 일시정지하는 요청을 STAF service로 보낸다.
     *
     * @param uuid the uuid 일시정지할 VM의 uuid
     * @throws MyStafException the my staf exception
     */
    public void pauseVMByUuid(String uuid) throws MyStafException;


    /**
     * 특정 uuid를 갖는 VM이 갖는 특정uuid를 갖는 snapshot을 또다른 VM으로 생성하는 요청을 STAF Service로 보낸다.
     * @param VMUuid       the vm uuid
     * @param snapshotUuid the snapshot uuid
     * @throws MyStafException the my staf exception
     */
    public String addOneVMByVMUuidAndSanpshotUuid(String VMUuid, String snapshotUuid) throws MyStafException;

    /**
     * 특정 uuid를 갖는 VM을  특정uuid를 갖는 snapshot의 상태로 변경하는 요청을 STAF service로 보낸다.
     * @param VMUuid       the vm uuid
     * @param snapshotUuid the snapshot uuid
     * @throws MyStafException the my staf exception
     */
    public void revertOneSnapshotByVMUuidAndSnapshotUuid(String VMUuid, String snapshotUuid) throws  MyStafException;

}
