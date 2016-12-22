<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../css/xenCss.css">
</head>
<body >

<div class="container" style="margin-top: 200px">
      <div class="row myText-center">
            <div class="col-md-12 ">
                <h1>Xen Server의 내용과 Local Cache의 Data정보가 일치지 않습니다.</h1>
                <br>
                <br>
                <h3>아래의 버튼을 눌러 동기화를 진행해 주세요.</h3>

            </div>
        </div>
        <br>
        <br>
    <div class="row myText-center">
        <div class="col-md-12 ">
             <button type="button" class="btn btn-default btn-lg" onclick="syncFunc()">
                <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Refresh
             </button>
        </div>
    </div>
</div>


<form action="/xenServer/syncVM" id="syncForm">
</form>


<script type="text/javascript" src="../../js/xenJs.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</body>
</html>
