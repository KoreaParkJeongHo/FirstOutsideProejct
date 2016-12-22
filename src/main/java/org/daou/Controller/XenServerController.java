package org.daou.Controller;

import com.xensource.xenapi.Types;
import com.xensource.xenapi.VM;
import org.daou.Exception.MyRepositoryException;
import org.daou.Exception.MyServiceException;
import org.daou.Exception.MyStafException;
import org.daou.Service.StafServiceI;
import org.daou.Service.XenServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by user on 2016-12-07.
 */

@Controller
@RequestMapping("/xenServer")
public class XenServerController {

    private static Logger logger = LoggerFactory.getLogger(XenServerController.class);

    @Autowired
    private XenServiceI xenService;
    @Autowired
    private StafServiceI stafService;
///////////////////////////////////////////////////VM관련 Controller ////////////////////////////////////////

    /**
     * VM List전체를 보여주는 Controller
     *
     * @param model the model
     * @return VMList.jsp
     */
    @RequestMapping("/showAllVM")
    public String showAllVMList(Model model){
        Map<VM, Map<String,Object>> VMRecordMap = null;

        try {
            VMRecordMap = xenService.findAllCachedVMRecord();
        } catch (MyRepositoryException e) {
            logger.error("Error :", e);
        } catch (MyServiceException e) {
            logger.error("Error :", e);
        }

        logger.info("INFO : showAllVMList ");
        model.addAttribute("VMRecordMap",VMRecordMap);

        return "VMList";
    }

    /**
     * 특정 uuid를 갖는 VM을 지우는 Controller
     *
     * @param vmId 삭제시킬 VM의 uuid
     * @return VMList.jsp로 redirect한다.
     */
    @RequestMapping("/deleteVM")
    public String deleteVM(@RequestParam("vmId") String vmId){

        logger.info("INFO : deleteVM " + vmId);
        try {
            if(!xenService.syncVMWithXenServer(vmId)) {
                logger.info("SYNC checking....");
                return "syncError";
            }
            logger.info("SYNC Complete");
            stafService.deleteOneVMByVMUuid(vmId);
            xenService.deleteOneCachedRecordByUuid(vmId);
        } catch (MyStafException e) {
            logger.error("Error : " + e.getMessage());
        }catch (MyServiceException e){
            logger.error("Error : " + e.getMessage());
        } catch (MyRepositoryException e) {
            e.printStackTrace();
        }

        return "redirect:/xenServer/showAllVM";
    }

    /**
     * Shutdown상태에 있는 VM을 run상태로 바꿔주는 Controller
     *
     * @param vmId Run상태로 변경할 VM의 uuid
     * @return VMList.jsp로 redirect한다.
     */
    @RequestMapping("/runVM")
    public String runVM(@RequestParam("vmId") String vmId){

        logger.info("INFO : runVM " + vmId);
        try {
            if(!xenService.syncVMWithXenServer(vmId)) {
                logger.info("SYNC checking....");
                return "syncError";
            }
            logger.info("SYNC Complete");
            stafService.startOneVMByUuid(vmId);
            xenService.updateOneCachedRecordStateByUuid(vmId, Types.VmPowerState.RUNNING);

        } catch (MyStafException e) {
            logger.error("Error : " + e.getMessage());
        }catch (MyServiceException e){
            logger.error("Error : " + e.getMessage());
        } catch (MyRepositoryException e) {
            e.printStackTrace();
        }

        return "redirect:/xenServer/showAllVM";
    }

    /**
     * Paused 상태에 있는 VM을 Run상태로 바꿔주는 Controller
     *
     * @param vmId Paused상태에 있는 VM의 uuid
     * @return VMList.jsp로 redirect한다.
     */
    @RequestMapping("/resumeVM")
    public String resumeVM(@RequestParam("vmId") String vmId){

        logger.info("INFO : resumeVM " + vmId);
        try {
            if(!xenService.syncVMWithXenServer(vmId)) {
                logger.info("SYNC checking....");
                return "syncError";
            }
            logger.info("SYNC Complete");
            stafService.resumeOneVMByUuid(vmId);
            xenService.updateOneCachedRecordStateByUuid(vmId, Types.VmPowerState.RUNNING);
        } catch (MyStafException e) {
            logger.error("Error : " + e.getMessage());
        }catch (MyServiceException e){
            logger.error("Error : " + e.getMessage());
        } catch (MyRepositoryException e) {
            e.printStackTrace();
        }

        return "redirect:/xenServer/showAllVM";
    }


    /**
     * Run상태에 있는 VM을 Shutdown상태로 바꿔주는 Controller
     *
     * @param vmId Run상태에 있는 VM의 uuid
     * @return the VMList.jsp로 redirect한다.
     */
    @RequestMapping("/stopVM")
    public String stopVM(@RequestParam("vmId") String vmId){

        logger.info("INFO : stopVM " + vmId);
        try {
            if(!xenService.syncVMWithXenServer(vmId)) {
                logger.info("SYNC checking....");
                return "syncError";
            }
            logger.info("SYNC Complete");
            stafService.shutdownVMByUuid(vmId);
            xenService.updateOneCachedRecordStateByUuid(vmId, Types.VmPowerState.HALTED);
        } catch (MyStafException e) {
            logger.error("Error : " + e.getMessage());
        }catch (MyServiceException e){
            logger.error("Error : " + e.getMessage());
        } catch (MyRepositoryException e) {
            e.printStackTrace();
        }

        return "redirect:/xenServer/showAllVM";
    }

    /**
     * Run상태에 있는 VM을 Paused상태로 변경한다.
     *
     * @param vmId Run상태에 있는 VM의 uuid
     * @return the VMList.jsp로 redirect한다.
     */
    @RequestMapping("/pauseVM")
    public String pauseVM(@RequestParam("vmId") String vmId){

        logger.info("INFO : pauseVM " + vmId);
        try {
            if(!xenService.syncVMWithXenServer(vmId)) {
                logger.info("SYNC checking....");
                return "syncError";
            }
            logger.info("SYNC Complete");
            stafService.pauseVMByUuid(vmId);
            xenService.updateOneCachedRecordStateByUuid(vmId, Types.VmPowerState.PAUSED);
        } catch (MyStafException e) {
            logger.error("Error : " + e.getMessage());
        }catch (MyServiceException e){
            logger.error("Error : " + e.getMessage());
        } catch (MyRepositoryException e) {
            e.printStackTrace();
        }

        return "redirect:/xenServer/showAllVM";
    }

    /**
     * Run상태에 있는 VM을 Suspended상태로 변경한다.
     *
     * @param vmId Run상태에 있는 VM의 uuid
     * @return the string : VMList.jsp로 redirect한다.
     */
    @RequestMapping("/suspendVM")
    public String suspendVM(@RequestParam("vmId") String vmId){

        logger.info("INFO : suspendVM " + vmId);
        try {
            if(!xenService.syncVMWithXenServer(vmId)) {
                logger.info("SYNC checking....");
                return "syncError";
            }
            logger.info("SYNC Complete");
            stafService.suspendOneVMByUuid(vmId);
            xenService.updateOneCachedRecordStateByUuid(vmId, Types.VmPowerState.SUSPENDED);
        } catch (MyStafException e) {
            logger.error("Error : " + e.getMessage());
        }catch (MyServiceException e){
            logger.error("Error : " + e.getMessage());
        } catch (MyRepositoryException e) {
            e.printStackTrace();
        }

        return "redirect:/xenServer/showAllVM";
    }

    /////////////////////////////////////////////Synchronize controller////////////////////////////

    @RequestMapping("/syncVM")
    public String syncVM(){

        try {
            xenService.forceSync();
        } catch (MyRepositoryException e) {
            logger.error("ERROR : " + e.getMessage());
        }

        return "redirect:/xenServer/showAllVM";
    }

    ///////////////////////////////////////////////////VM관련 Controller ////////////////////////////////////////

    ///////////////////////////////////////////////////Snapshot관련 Controller ////////////////////////////////////////

    /**
     * 특정 uuid를 갖는 VM의 snapshot list를 모두보여주는 controller
     *
     * @param vmId  : snapshot List를 갖는 VM의 uuid
     * @param model the model
     * @return the string : snapshotList.jsp
     */
    @RequestMapping("/showAllSnapshot")
    public String showAllSnapshotList(@RequestParam("vmId") String vmId, Model model){
        logger.info("INFO : showAllSnapshot " + vmId);

        Map<VM,Map<String, Object>> snapshotRecordMap = null;
        String vmNameLabel = null;
        try {
            if(!xenService.syncSnapshotWithXenServer(vmId)){
                logger.info("SNAPSHOT SYNC checking....");
                return "syncError";
            }
            logger.info("SNAPSHOT SYNC Complete");

            snapshotRecordMap = xenService.findAllCachedSnapshotRecordByUuid(vmId);
            vmNameLabel = (String)(xenService.findOneCachedRecordByUuid(vmId).get("name_label"));
        } catch (MyServiceException e) {
            logger.error("Error : " + e.getMessage());
        } catch (MyRepositoryException e) {
            e.printStackTrace();
        }

        model.addAttribute("snapshotRecordMap",snapshotRecordMap);
        model.addAttribute("vmNameLabel",vmNameLabel);
        model.addAttribute("vmId",vmId);

        return "snapshotList";
    }


    /**
     * 특정 uuid를 갖는 VM의 snapshot List중 특정 uuid를 갖는 Snapshot을 또다른 VM으로 생성하는 controller
     *
     * @param vmId       : 특정 snapshot을 갖는 VM의 uuid
     * @param snapshotId : 특정 snapshot의 uuid
     * @return the string : showAllVM.jsp로 redirect한다.
     */
    @RequestMapping("/addVM")
    public String addVM(@RequestParam("vmId") String vmId, @RequestParam("snapshotId") String snapshotId){

        String request = "ADD vmId " + vmId + " snapshotId " + snapshotId;
        String clonedVMUuid = null;
        try {
            if(!xenService.syncSnapshotWithXenServer(vmId)){
                logger.info("SNAPSHOT SYNC checking....");
                return "syncError";
            }
            logger.info("SNAPSHOT SYNC Complete");
            clonedVMUuid = stafService.addOneVMByVMUuidAndSanpshotUuid(vmId,snapshotId);
            xenService.addOneNoCachedVmToCacheByUuid(clonedVMUuid); //add된 VM cache에 새로 update
        } catch (MyStafException e) {
            logger.error("Error : " + e.getMessage());
        } catch (MyRepositoryException e) {
            logger.error("Error : " + e.getMessage());
        } catch (MyServiceException e) {
            e.printStackTrace();
        }

        return "redirect:/xenServer/showAllVM";
    }

    /**
     * 특정 uuid를 갖는 VM을 snapshot List중 특정 uuid를 갖는 Snapshot의 상태로 되돌리는 Controller
     *
     * @param vmId       : 특정 snapshot의 상태로 되돌린 VM의 uuid
     * @param snapshotId : 특정 상태를 갖는 snapshot의 uuid
     * @return the string : showAllVM.jsp로 redirect한다.
     */
    @RequestMapping("/revertVM")
    public String revertVM(@RequestParam("vmId") String vmId, @RequestParam("snapshotId") String snapshotId){

        String request = "REVERT vmId " + vmId + " snapshotId " + snapshotId;
        try {
            if(!xenService.syncSnapshotWithXenServer(vmId)){
                logger.info("SNAPSHOT SYNC checking....");
                return "syncError";
            }
            logger.info("SNAPSHOT SYNC Complete");
            stafService.revertOneSnapshotByVMUuidAndSnapshotUuid(vmId,snapshotId);
            xenService.addOneNoCachedVmToCacheByUuid(vmId); // Rever된 VM을 Cache에 새로 update
        } catch (MyStafException e) {
            logger.error("Error : " + e.getMessage());
        } catch (MyRepositoryException e) {
            logger.error("Error : " + e.getMessage());
        } catch (MyServiceException e) {
            e.printStackTrace();
        }

        return "redirect:/xenServer/showAllVM";
    }

    ///////////////////////////////////////////////////Snapshot관련 Controller ////////////////////////////////////////

    @RequestMapping("/test")
    public String test(){
        return "syncError";
    }
}
