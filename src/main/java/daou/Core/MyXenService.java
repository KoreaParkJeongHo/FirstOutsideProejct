package daou.Core;

import com.ibm.staf.STAFException;
import com.ibm.staf.STAFHandle;
import com.ibm.staf.STAFResult;
import com.ibm.staf.STAFUtil;
import com.ibm.staf.service.STAFCommandParseResult;
import com.ibm.staf.service.STAFCommandParser;
import com.ibm.staf.service.STAFServiceInterfaceLevel30;
import com.xensource.xenapi.VM;
import daou.Exception.MyRepositoryException;
import daou.Service.XenServerService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by daou on 2016-12-06.
 */
public class MyXenService implements STAFServiceInterfaceLevel30  {


    private final String kVersion = "3";

    private String fServiceName;
    private STAFHandle fHandle;
    private String fLocalMachineName = "";

    // Define any error codes unique to this service
    private static final int kDeviceInvalidSerialNumber = 4001;

    private final static SimpleDateFormat sTimestampFormat =
            new SimpleDateFormat("yyyyMMdd-HH:mm:ss");

    private STAFCommandParser fAddParser;
    private STAFCommandParser fDeleteParser;
    private STAFCommandParser fStartParser;
    private STAFCommandParser fStopParser;
    private STAFCommandParser fRevertParser;
    private STAFCommandParser fSuspendParser;
    private STAFCommandParser fPauseParser;
    private STAFCommandParser fResumeParser;

    private static String sHelpMsg;
    private String fLineSep;

    private XenServerService xenServerService;

    public STAFResult init(InitInfo initInfo) {

        try
        {
            fServiceName = initInfo.name; //Service name을 전역변수로 저장
            fHandle = new STAFHandle("STAF/Service/" + initInfo.name); //Service등록 : STAF/Service/ + service name으로 등록
            xenServerService = new XenServerService(); //사용할 Service객체 생성
        }
        catch (STAFException e)
        {
            return new STAFResult(STAFResult.STAFRegistrationError,
                    e.toString());
        }



        // ADD parser

        fAddParser = new STAFCommandParser();

        fAddParser.addOption("ADD", 1,
                STAFCommandParser.VALUENOTALLOWED);

        fAddParser.addOption("vmId",1,
                STAFCommandParser.VALUEREQUIRED);

        fAddParser.addOption("snapshotId",1,
                STAFCommandParser.VALUEREQUIRED);

        // REVERT parser

        fRevertParser = new STAFCommandParser();

        fRevertParser.addOption("REVERT", 1,
                STAFCommandParser.VALUENOTALLOWED);

        fRevertParser.addOption("vmId",1,
                STAFCommandParser.VALUEREQUIRED);

        fRevertParser.addOption("snapshotId",1,
                STAFCommandParser.VALUEREQUIRED);

        // DELETE parser

        fDeleteParser = new STAFCommandParser();

        fDeleteParser.addOption("DELETE", 1,
                STAFCommandParser.VALUENOTALLOWED);

        fDeleteParser.addOption("vmId",1,
                STAFCommandParser.VALUEREQUIRED);

        // STOP parser

        fStopParser = new STAFCommandParser();

        fStopParser.addOption("STOP", 1,
                STAFCommandParser.VALUENOTALLOWED);

        fStopParser.addOption("vmId",1,
                STAFCommandParser.VALUEREQUIRED);

        // SUSPEND parser

        fSuspendParser = new STAFCommandParser();

        fSuspendParser.addOption("SUSPEND", 1,
                STAFCommandParser.VALUENOTALLOWED);

        fSuspendParser.addOption("vmId",1,
                STAFCommandParser.VALUEREQUIRED);

        // PAUSE parser

        fPauseParser = new STAFCommandParser();

        fPauseParser.addOption("PAUSE", 1,
                STAFCommandParser.VALUENOTALLOWED);

        fPauseParser.addOption("vmId",1,
                STAFCommandParser.VALUEREQUIRED);

        // START parser

        fStartParser = new STAFCommandParser();

        fStartParser.addOption("START", 1,
                STAFCommandParser.VALUENOTALLOWED);

        fStartParser.addOption("vmId",1,
                STAFCommandParser.VALUEREQUIRED);


        // RESUME parser

        fResumeParser = new STAFCommandParser();

        fResumeParser.addOption("RESUME", 1,
                STAFCommandParser.VALUENOTALLOWED);

        fResumeParser.addOption("vmId",1,
                STAFCommandParser.VALUEREQUIRED);

        //STAFResult 설정

        STAFResult res = new STAFResult();

        res = STAFUtil.resolveInitVar("{STAF/Config/Sep/Line}", fHandle);
        if (res.rc != STAFResult.Ok) return res;

        fLineSep = res.result;

        res = STAFUtil.resolveInitVar("{STAF/Config/Machine}", fHandle);
        if (res.rc != STAFResult.Ok) return res;

        fLocalMachineName = res.result;


        sHelpMsg = "*** " + fServiceName + " Service Help ***" +
                fLineSep + fLineSep +
                "ADD     vmId <vmIdValue> snapshotId <snapshotIdValue>" +
                fLineSep +
                "DELETE  vmId <vmIdValue>" +
                fLineSep +
                "VERSION" +
                fLineSep +
                "HELP";

        // Register Help Data

        registerHelpData(
                kDeviceInvalidSerialNumber,
                "Invalid serial number",
                "A non-numeric value was specified for serial number");

        return new STAFResult(STAFResult.Ok);
    }

    private void registerHelpData(int errorNumber, String info,
                                  String description)
    {
        STAFResult res = fHandle.submit2(
                "local", "HELP", "REGISTER SERVICE " + fServiceName +
                        " ERROR " + errorNumber +
                        " INFO " + STAFUtil.wrapData(info) +
                        " DESCRIPTION " + STAFUtil.wrapData(description));
    }

    public STAFResult acceptRequest(RequestInfo reqInfo) {

        try {
            String action;
            int spaceIndex = reqInfo.request.indexOf(" ");

            if (spaceIndex != -1)
                action = reqInfo.request.substring(0, spaceIndex);//첫번째 word를 가져온다.
            else
                action = reqInfo.request;

            String actionLC = action.toLowerCase();

            if (actionLC.equals("add"))
                return handleAdd(reqInfo);
            else if(actionLC.equals("revert"))
                return handleRevert(reqInfo);
            else if (actionLC.equals("delete"))
                return handleDelete(reqInfo);
            else if(actionLC.equals("start"))
                return handleStart(reqInfo);
            else if(actionLC.equals("resume"))
                return handleResume(reqInfo);
            else if(actionLC.equals("stop"))
                return handleStop(reqInfo);
            else if(actionLC.equals("suspend"))
                return handleSuspend(reqInfo);
            else if(actionLC.equals("pause"))
                return handlePause(reqInfo);
            else if (actionLC.equals("help"))
                return handleHelp(reqInfo);
            else if (actionLC.equals("version"))
                return handleVersion(reqInfo);
            else
            {
                return new STAFResult(
                        STAFResult.InvalidRequestString,
                        "'" + action +
                                "' is not a valid command request for the " +
                                fServiceName + " service" +
                                fLineSep + fLineSep + sHelpMsg);
            }
        }catch (Throwable t)
        {
            // Write the Java stack trace to the JVM log for the service
            System.out.println(
                    sTimestampFormat.format(Calendar.getInstance().getTime()) +
                            " ERROR: Exception on " + fServiceName + " service request:" +
                            fLineSep + fLineSep + reqInfo.request + fLineSep);

            t.printStackTrace();

            // And also return the Java stack trace in the result

            StringWriter sr = new StringWriter();
            t.printStackTrace(new PrintWriter(sr));

            if (t.getMessage() != null)
            {
                return new STAFResult(
                        STAFResult.JavaError,
                        t.getMessage() + fLineSep + sr.toString());
            }
            else
            {
                return new STAFResult(
                        STAFResult.JavaError, sr.toString());
            }

        }
    }

    private STAFResult handleSuspend(RequestInfo reqInfo) {
        STAFResult trustResult = STAFUtil.validateTrust(5, fServiceName, "SUSPEND", fLocalMachineName, reqInfo);
        if (trustResult.rc != STAFResult.Ok) return trustResult;

        STAFCommandParseResult parsedRequest = fSuspendParser.parse(reqInfo.request);
        if (parsedRequest.rc != STAFResult.Ok)
        {
            return new STAFResult(STAFResult.InvalidRequestString,
                    parsedRequest.errorBuffer); //buffer는 error에 대한 내용을 내보내준다.
        }

        STAFResult res = new STAFResult();

        //vmid의 optionValue를 parsing
        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("vmId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;

        String vmId = res.result; //addres가져오기

        try {
            VM vm = xenServerService.findOneVMByUuid(vmId);
            xenServerService.suspendOneVM(vm);
        } catch (MyRepositoryException e) {
            String myLogMsg = "Error : " + e.getMessage();
            fHandle.submit2("local","LOG","LOG MACHINE LOGNAME " + fServiceName +
                    "LEVEL into MESSAGE " + STAFUtil.wrapData(myLogMsg));
        }


        String logMsg = "[LOG] : TargetService : " + fServiceName + " Message : SUSPEND VM request vmID=" + vmId;
        return new STAFResult(STAFResult.Ok,logMsg);
    }

    private STAFResult handleResume(RequestInfo reqInfo) {
        STAFResult trustResult = STAFUtil.validateTrust(5, fServiceName, "RESUME", fLocalMachineName, reqInfo);
        if (trustResult.rc != STAFResult.Ok) return trustResult;

        STAFCommandParseResult parsedRequest = fResumeParser.parse(reqInfo.request);
        if (parsedRequest.rc != STAFResult.Ok)
        {
            return new STAFResult(STAFResult.InvalidRequestString,
                    parsedRequest.errorBuffer); //buffer는 error에 대한 내용을 내보내준다.
        }

        STAFResult res = new STAFResult();

        //vmid의 optionValue를 parsing
        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("vmId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;


        String vmId = res.result; //addres가져오기
        VM vm = null;
        try {
            vm = xenServerService.findOneVMByUuid(vmId);
            xenServerService.resumeOneVM(vm);

        } catch (MyRepositoryException e) {
            String myLogMsg = "Error : " + e.getMessage();
            fHandle.submit2("local","LOG","LOG MACHINE LOGNAME " + fServiceName +
                                "LEVEL into MESSAGE " + STAFUtil.wrapData(myLogMsg));
        }


        String logMsg = "[LOG] : TargetService : " + fServiceName + " Message : RESUME VM request vmID=" + vmId;
        return new STAFResult(STAFResult.Ok,logMsg);

    }

    private STAFResult handlePause(RequestInfo reqInfo) {
        STAFResult trustResult = STAFUtil.validateTrust(5, fServiceName, "PAUSE", fLocalMachineName, reqInfo);
        if (trustResult.rc != STAFResult.Ok) return trustResult;

        STAFCommandParseResult parsedRequest = fPauseParser.parse(reqInfo.request);
        if (parsedRequest.rc != STAFResult.Ok)
        {
            return new STAFResult(STAFResult.InvalidRequestString,
                    parsedRequest.errorBuffer); //buffer는 error에 대한 내용을 내보내준다.
        }

        STAFResult res = new STAFResult();

        //vmid의 optionValue를 parsing
        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("vmId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;

        String vmId = res.result; //addres가져오기

        try {
            VM vm = xenServerService.findOneVMByUuid(vmId);
            xenServerService.pauseOneVM(vm);
        } catch (MyRepositoryException e) {
            String myLogMsg = "Error : " + e.getMessage();
            fHandle.submit2("local","LOG","LOG MACHINE LOGNAME " + fServiceName +
                    "LEVEL into MESSAGE " + STAFUtil.wrapData(myLogMsg));
        }


        String logMsg = "[LOG] : TargetService : " + fServiceName + " Message : PAUSE VM request vmID=" + vmId;
        return new STAFResult(STAFResult.Ok,logMsg);

    }

    private STAFResult handleHelp(RequestInfo info)
    {
        STAFResult trustResult = STAFUtil.validateTrust(1, fServiceName, "HELP", fLocalMachineName, info);
        if (trustResult.rc != STAFResult.Ok) return trustResult;

        return new STAFResult(STAFResult.Ok, sHelpMsg);
    }

    private STAFResult handleVersion(RequestInfo info)
    {

        STAFResult trustResult = STAFUtil.validateTrust(1, fServiceName, "VERSION", fLocalMachineName, info);
        if (trustResult.rc != STAFResult.Ok) return trustResult;

        return new STAFResult(STAFResult.Ok, kVersion);
    }

    private STAFResult handleStart(RequestInfo reqInfo){
        STAFResult trustResult = STAFUtil.validateTrust(5, fServiceName, "START", fLocalMachineName, reqInfo);
        if (trustResult.rc != STAFResult.Ok) return trustResult;

        STAFCommandParseResult parsedRequest = fStartParser.parse(reqInfo.request);
        if (parsedRequest.rc != STAFResult.Ok)
        {
            return new STAFResult(STAFResult.InvalidRequestString,
                    parsedRequest.errorBuffer); //buffer는 error에 대한 내용을 내보내준다.
        }

        STAFResult res = new STAFResult();

        //vmid의 optionValue를 parsing
        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("vmId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;

        String vmId = res.result; //addres가져오기
        VM vm = null;
        try {
            vm = xenServerService.findOneVMByUuid(vmId);
            xenServerService.startOneVM(vm);
        } catch (MyRepositoryException e) {
            String myLogMsg = "Error : " + e.getMessage();
            fHandle.submit2("local","LOG","LOG MACHINE LOGNAME " + fServiceName +
                    "LEVEL into MESSAGE " + STAFUtil.wrapData(myLogMsg));
        }


        String logMsg = "[LOG] : TargetService : " + fServiceName + " Message : Start VM Request vmID=" + vmId;
        return new STAFResult(STAFResult.Ok,logMsg);
    }



    private STAFResult handleStop(RequestInfo reqInfo){
        STAFResult trustResult = STAFUtil.validateTrust(5, fServiceName, "STOP", fLocalMachineName, reqInfo);
        if (trustResult.rc != STAFResult.Ok) return trustResult;

        STAFCommandParseResult parsedRequest = fStopParser.parse(reqInfo.request);
        if (parsedRequest.rc != STAFResult.Ok)
        {
            return new STAFResult(STAFResult.InvalidRequestString,
                    parsedRequest.errorBuffer); //buffer는 error에 대한 내용을 내보내준다.
        }

        STAFResult res = new STAFResult();

        //vmid의 optionValue를 parsing
        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("vmId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;

        String vmId = res.result; //addres가져오기
        VM vm = null;
        try {
            vm = xenServerService.findOneVMByUuid(vmId);
            xenServerService.shutdownOneVM(vm);

        } catch (MyRepositoryException e) {
            String myLogMsg = "Error : " + e.getMessage();
            fHandle.submit2("local","LOG","LOG MACHINE LOGNAME " + fServiceName +
                    "LEVEL into MESSAGE " + STAFUtil.wrapData(myLogMsg));
        }


        String logMsg = "[LOG] : TargetService : " + fServiceName + " Message : STOP VM request vmID=" + vmId;
        return new STAFResult(STAFResult.Ok,logMsg);
    }

    //입력 포맷 ADD vmId {value} snapshotId {value}
    public STAFResult handleAdd(RequestInfo reqInfo){

        //진입 Level check
        STAFResult trustResult = STAFUtil.validateTrust(5, fServiceName, "ADD", fLocalMachineName, reqInfo);

        if (trustResult.rc != STAFResult.Ok) return trustResult;

        //AddParser로 파싱
        STAFCommandParseResult parsedRequest = fAddParser.parse(reqInfo.request);

        if (parsedRequest.rc != STAFResult.Ok)
        {
            return new STAFResult(STAFResult.InvalidRequestString,
                    parsedRequest.errorBuffer); //buffer는 error에 대한 내용을 내보내준다.
        }

        //결과 String을 담을 STAFResult 생성
        STAFResult res = new STAFResult();

        //vmid의 optionValue를 parsing
        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("vmId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;

        String vmId = res.result; //addres가져오기

        //snapshotId의 optionValue를 parsing
        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("snapshotId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;

        String snapshotId = res.result;

        VM vm = null; //우선 VM찾는다.
        VM clonedVm = null;
        String cloendVmUuid = null;
        try {
            vm = xenServerService.findOneVMByUuid(vmId);
            VM snapshot = xenServerService.findOneSnapshotByVMAndUuid(vm, snapshotId); //다시 추가하고싶은 snapshot을 찾는다.
            clonedVm = xenServerService.addOneVM(snapshot);//snapshot을 VM으로 생성
            cloendVmUuid = xenServerService.findOneVMUuid(clonedVm);
        } catch (MyRepositoryException e) {
            String myLogMsg = "Error : " + e.getMessage();
            fHandle.submit2("local","LOG","LOG MACHINE LOGNAME " + fServiceName +
                    "LEVEL into MESSAGE " + STAFUtil.wrapData(myLogMsg));
        }


        String logMsg = "[LOG] : TargetService : " + fServiceName + " Message : ADD VM request vmID = " + vmId
                    + "snapshotID = " + snapshotId;

        return new STAFResult(STAFResult.Ok,cloendVmUuid);

    }

    private STAFResult handleRevert(RequestInfo reqInfo){

        //진입 Level check
        STAFResult trustResult = STAFUtil.validateTrust(5, fServiceName, "REVERT", fLocalMachineName, reqInfo);

        if (trustResult.rc != STAFResult.Ok) return trustResult;

        //AddParser로 파싱
        STAFCommandParseResult parsedRequest = fRevertParser.parse(reqInfo.request);

        if (parsedRequest.rc != STAFResult.Ok)
        {
            return new STAFResult(STAFResult.InvalidRequestString,
                    parsedRequest.errorBuffer); //buffer는 error에 대한 내용을 내보내준다.
        }

        //결과 String을 담을 STAFResult 생성
        STAFResult res = new STAFResult();

        //vmid의 optionValue를 parsing
        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("vmId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;

        String vmId = res.result; //addres가져오기

        //snapshotId의 optionValue를 parsing
        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("snapshotId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;

        String snapshotId = res.result;

        VM vm = null; //우선 VM찾는다.
        try {
            vm = xenServerService.findOneVMByUuid(vmId);
            VM snapshot = xenServerService.findOneSnapshotByVMAndUuid(vm, snapshotId); //다시 추가하고싶은 snapshot을 찾는다.
            xenServerService.revertOneSnapshot(snapshot); //snapshot을 VM으로 생성

        } catch (MyRepositoryException e) {
            String myLogMsg = "Error : " + e.getMessage();
            fHandle.submit2("local","LOG","LOG MACHINE LOGNAME " + fServiceName +
                    "LEVEL into MESSAGE " + STAFUtil.wrapData(myLogMsg));
        }


        String logMsg = "[LOG] : TargetService : " + fServiceName + " Message : REVERT VM request vmID = " + vmId
                + "snapshotID = " + snapshotId;
        return new STAFResult(STAFResult.Ok,logMsg);

    }

    //입력 포맷 : DELETE vmId {value}
    private STAFResult handleDelete(RequestInfo reqInfo){

        STAFResult trustResult = STAFUtil.validateTrust(5, fServiceName, "DELETE", fLocalMachineName, reqInfo);
        if (trustResult.rc != STAFResult.Ok) return trustResult;

        STAFCommandParseResult parsedRequest = fDeleteParser.parse(reqInfo.request);

        if (parsedRequest.rc != STAFResult.Ok)
        {
            return new STAFResult(STAFResult.InvalidRequestString,
                    parsedRequest.errorBuffer); //buffer는 error에 대한 내용을 내보내준다.
        }

        STAFResult res = new STAFResult(); //STAFResult생성

        res = STAFUtil.resolveRequestVar(parsedRequest.optionValue("vmId"), fHandle, reqInfo.requestNumber);
        if (res.rc != STAFResult.Ok) return res;

        String vmId = res.result; //addres가져오기

        VM vm = null; //지우고 싶은 VM찾는다.
        try {
            vm = xenServerService.findOneVMByUuid(vmId);
            xenServerService.destroyOneVM(vm); //VM 삭제
        } catch (MyRepositoryException e) {
            String myLogMsg = "Error : " + e.getMessage();
            fHandle.submit2("local","LOG","LOG MACHINE LOGNAME " + fServiceName +
                    "LEVEL into MESSAGE " + STAFUtil.wrapData(myLogMsg));
        }



        String logMsg = "[LOG] : TargetService : " + fServiceName + " Message : DELETE VM request vmID = " + vmId;
        return new STAFResult(STAFResult.Ok,logMsg);
    }

    public STAFResult term() {
        try
        {
            // Un-register Help Data
            unregisterHelpData(kDeviceInvalidSerialNumber);

            // Un-register the service handle
            fHandle.unRegister();
        }
        catch (STAFException ex)
        {
            return new STAFResult(STAFResult.STAFRegistrationError,
                    ex.toString());
        }

        return new STAFResult(STAFResult.Ok);
    }

    private void unregisterHelpData(int errorNumber)
    {
        STAFResult res = fHandle.submit2(
                "local", "HELP", "UNREGISTER SERVICE " + fServiceName +
                        " ERROR " + errorNumber);
    }


}