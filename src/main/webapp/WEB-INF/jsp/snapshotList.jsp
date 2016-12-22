<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../css/xenCss.css">
</head>
<body >
<div class="container myText-center myMargin-top" >
    <div class="center-block"><h1><string class="myFont-size">${vmNameLabel} VM's Snapshot List</string></h1></div>
</div>


<div class="container" style="margin-top: 50px">
    <div class="row">
        <div class="col-md-12">
            <table class="table table-bordered table-condensed table-hover myText-center">
                <tr class="lead">
                    <td><strong>#</strong></td>
                    <td><string>Snapshot Name</string></td>
                    <td><strong>Edit</strong></td>
                </tr>
                <c:forEach items="${snapshotRecordMap}" var="snapshotRecord" varStatus="status">
                    <tr>
                        <td class="myText-center"><strong>${status.count}</strong></td>
                        <td>${snapshotRecord.value.name_label}</td>
                        <td>
                            <button type="button" class="btn btn-primary" onclick="addSnapshotFunc('${vmId}','${snapshotRecord.value.uuid}')">Add</button>
                            <button type="button" class="btn btn-warning" onclick="revertSnapshotFunc('${vmId}','${snapshotRecord.value.uuid}')">Revert</button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>



<form action="/xenServer/addVM" id="VMAddForm">
    <input type="hidden" name="vmId">
    <input type="hidden" name="snapshotId">
</form>

<form action="/xenServer/revertVM" id="VMRevertForm">
    <input type="hidden" name="vmId">
    <input type="hidden" name="snapshotId">
</form>



<script type="text/javascript" src="../../js/xenJs.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</body>
</html>
