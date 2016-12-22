/**
 * Created by user on 2016-12-08.
 */


/*
    VM Delete 버튼클릭시, 해당 요청을 server로 보내주는 function
    Running 상태시 한번더 묻고, 그렇지않은 경우 그냥 삭제.
 */


////////////////////////////////////////VM관련 function//////////////////////////////////////////////////

function startVMFunc(vmId) {
    var VMstartForm = document.getElementById("VMStartForm");
    VMstartForm.vmId.value = vmId;

    if(confirm("Current VM is stop. Are you sure to run this VM?") == true)
        VMstartForm.submit();
    else
        return false;
}

function resumeVMFunc(vmId) {
    var VMResumeForm = document.getElementById("VMResumeForm");
    VMResumeForm.vmId.value = vmId;

    if(confirm("Current VM is stop. Are you sure to resume this VM?") == true)
        VMResumeForm.submit();
    else
        return false;
}

function stopVMFunc(vmId) {
    var VMStopForm = document.getElementById("VMStopForm");
    VMStopForm.vmId.value = vmId;

    if(confirm("Current VM is Running. Are you sure to stop this VM?") == true)
        VMStopForm.submit();
    else
        return false;
}

function deleteVMFunc(vmId){
    var VMdeleteForm = document.getElementById("VMDeleteForm");
    VMdeleteForm.vmId.value = vmId;

    if(confirm("Current VM is running. Are you sure to delete this VM?") == true){
        VMdeleteForm.submit();
    }else
        return false;
}

function pausedVMFunc(vmId){
    var VMPausedForm = document.getElementById("VMPausedForm");
    VMPausedForm.vmId.value = vmId;

    if(confirm("Current VM is running. Are you sure to paused this VM?") == true){
        VMPausedForm.submit();
    }else
        return false;
}

function suspendVMFunc(vmId){
    var VMSuspendForm = document.getElementById("VMSuspendForm");
    VMSuspendForm.vmId.value = vmId;

    if(confirm("Current VM is running. Are you sure to suspend this VM?") == true){
        VMSuspendForm.submit();
    }else
        return false;
}

///////////////////////////////////////동기화 function////////////////////////////////////////////////

function syncFunc(){
    var syncForm = document.getElementById("syncForm");

    if(confirm("Are you sure to synchronize VM List?") == true){
            syncForm.submit();
    }else
              return false;

}

///////////////////////////////////////동기화 function////////////////////////////////////////////////

////////////////////////////////////////VM관련 function//////////////////////////////////////////////////


////////////////////////////////////////SnapShot관련 function//////////////////////////////////////////////////

function addSnapshotFunc(vmId, snapshotId) {
    var addForm = document.getElementById("VMAddForm");

    addForm.vmId.value = vmId;
    addForm.snapshotId.value = snapshotId;

    if(confirm("Are you sure to create VM with this Snapshot?") == true)
        addForm.submit();
    else
        return false;
}

function revertSnapshotFunc(vmId, snapshotId){
    var revertForm = document.getElementById("VMRevertForm");

    revertForm.vmId.value = vmId;
    revertForm.snapshotId.value = snapshotId;

    if(confirm("Are you sure to revert this Snapshot?") == true)
        revertForm.submit();
    else
        return false;

}

////////////////////////////////////////SnapShot관련 function//////////////////////////////////////////////////