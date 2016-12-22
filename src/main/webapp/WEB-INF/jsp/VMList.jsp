<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../css/xenCss.css">

</head>
<body >
<div class="container myText-center myMargin-top" >
    <div class="center-block"><h1><string class="myFont-size">VM Instace List</string></h1></div>
</div>
<div class="container myMargin-top">
    <div class="row">
        <div class="col-md-8">
            <div class="container">
                <div class="row">
                    <div class="col-md-1 myDefault-circle bg-success"><div class="myCircle"></div></div>
                    <div class="col-md-1"><strong>Running</strong></div>
                    <div class="col-md-1 myDefault-circle bg-danger"><div class="myCircle"></div></div>
                    <div class="col-md-1"><strong>Halted</strong></div>
                    <div class="col-md-1 myDefault-circle bg-info"><div class="myCircle"></div></div>
                    <div class="col-md-1"><strong>Paused</strong></div>
                    <div class="col-md-1 myDefault-circle bg-warning"><div class="myCircle"></div></div>
                    <div class="col-md-1"><strong>Suspended</strong></div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container" style="margin-top: 50px">
    <div class="row">
        <div class="col-md-12">
            <table class="table table-bordered table-condensed table-hover myText-center">
                <tr class="lead">
                    <td><strong>#</strong></td>
                    <td><string>VM Name</string></td>
                    <td><string>Snapshot Count</string></td>
                    <td><strong>Edit</strong></td>
                </tr>
                <c:forEach items="${VMRecordMap}" var="VMRecord" varStatus="stats">
                    <tr
                            <c:if test="${VMRecord.value.power_state eq 'RUNNING'}"> class="bg-success"</c:if>
                            <c:if test="${VMRecord.value.power_state eq 'HALTED'}"> class="bg-danger"</c:if>
                            <c:if test="${VMRecord.value.power_state eq 'SUSPENDED'}"> class="bg-warning" </c:if>
                            <c:if test="${VMRecord.value.power_state eq 'PAUSED'}"> class="bg-info"</c:if>

                    >
                        <td class="myText-center"><strong>${stats.count}</strong></td>
                        <td>${VMRecord.value.name_label}</td>
                        <td><a href="/xenServer/showAllSnapshot?vmId=${VMRecord.value.uuid}">${fn:length(VMRecord.value.snapshots)}</a></td>
                        <td>
                            <c:choose>
                                <c:when test="${VMRecord.value.power_state eq 'HALTED'}">
                                    <button type="button" class="btn btn-success" onclick="startVMFunc('${VMRecord.value.uuid}')">Running</button>
                                    <button type="button" class="btn btn-danger" onclick="deleteVMFunc('${VMRecord.value.uuid}')">Delete </button>
                                </c:when>
                                <c:when test="${VMRecord.value.power_state eq 'RUNNING'}">
                                    <button type="button" class="btn btn-primary" onclick="pausedVMFunc('${VMRecord.value.uuid}')">Paused</button>
                                    <button type="button" class="btn btn-info" onclick="suspendVMFunc('${VMRecord.value.uuid}')">Suspend</button>
                                    <button type="button" class="btn btn-warning" onclick="stopVMFunc('${VMRecord.value.uuid}')">Shutdown</button>
                                </c:when>
                                <c:when test="${VMRecord.value.power_state eq 'PAUSED'}">
                                    <button type="button" class="btn btn-warning" onclick="stopVMFunc('${VMRecord.value.uuid}')">Shutdown</button>
                                </c:when>
                                <c:when test="${VMRecord.value.power_state eq 'SUSPENDED'}">
                                    <button type="button" class="btn btn-success" onclick="resumeVMFunc('${VMRecord.value.uuid}')">Running</button>
                                    <button type="button" class="btn btn-warning" onclick="stopVMFunc('${VMRecord.value.uuid}')">Shutdown</button>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<form action="/xenServer/deleteVM" id="VMDeleteForm">
    <input type="hidden" name="vmId">
</form>

<form action="/xenServer/runVM" id="VMStartForm">
    <input type="hidden" name="vmId">
</form>

<form action="/xenServer/resumeVM" id="VMResumeForm">
    <input type="hidden" name="vmId">
</form>

<form action="/xenServer/stopVM" id="VMStopForm">
    <input type="hidden" name="vmId">
</form>

<form action="/xenServer/pauseVM" id="VMPausedForm">
    <input type="hidden" name="vmId">
</form>

<form action="/xenServer/suspendVM" id="VMSuspendForm">
    <input type="hidden" name="vmId">
</form>

<form action="/xenServer/showAllSortedVM" id="showAllSortedVMForm">
    <input type="hidden" name="category">
    <input type="hidden" name="standard">
</form>



<script type="text/javascript" src="../../js/xenJs.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>
