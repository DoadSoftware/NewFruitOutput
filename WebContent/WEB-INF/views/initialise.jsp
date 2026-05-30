<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Initialise Screen</title>

<!-- CSS -->
<link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.1.3/css/bootstrap.min.css' />">
<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/6.5.2/css/all.min.css' />">

<!-- JS -->
<script src="<c:url value='/webjars/jquery/3.6.0/jquery.min.js' />"></script>
<script src="<c:url value='/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js' />"></script>
<script src="<c:url value='/resources/javascript/index.js' />"></script>

</head>
<body onload="initialiseForm('initialise',null)">
<form:form name="initialise_form" autocomplete="off" action="output" method="POST">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card shadow-sm">
           <div class="card-header">
             <h3 class="mb-0">Initialise</h3>
           </div>
          <div class="card-body">
            <div id="initialise_div" style="display:none;">
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:0.8px;">
			    <label for="Category" class="col-sm-4 col-form-label text-left">Select Category </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="Category" name="Category" class="browser-default custom-select custom-select-sm"
			      		onchange="processCricketProcedures('GET-CATEGORY-DATA')">
			      		<option value=" "> </option>
			      		<option value="MEN">MEN</option>
			      		<option value="WOMEN">WOMEN</option>
			      </select>
			    </div>
			  </div>
			  <div class="row mb-2 ms-2 align-items-center" >
			    <label for="select_configuration_file" class="col-sm-4 col-form-label text-start">Select Configuration </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_configuration_file" name="select_configuration_file" 
			      		class="form-select form-select-sm" onchange="processUserSelection(this)">
			          	<option value=""></option>
						<c:forEach items = "${configuration_files}" var = "config">
				          	<option value="${config.name}">${config.name}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
			  <div class="row mb-2 ms-2 align-items-center" >
			    <label for="configuration_file_name" class="col-sm-4 col-form-label text-start">Configuration File Name </label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="configuration_file_name" name="configuration_file_name"
		             	class="form-control form-control-sm floatlabel">
			    </div>
			  </div>
			  <div class="row mb-2 ms-2 align-items-center" >
			    <label for="select_cricket_matches" class="col-sm-4 col-form-label text-start">Select Cricket Match </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_cricket_matches" name="select_cricket_matches" 
			      		class="form-select form-select-sm">
						<c:forEach items = "${match_files}" var = "match">
				          	<option value="${match.name}">${match.name}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
			  <div class="row mb-2 ms-2 align-items-center" >
			    <label for="select_broadcaster" class="col-sm-4 col-form-label text-start">Select Broadcaster </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_broadcaster" name="select_broadcaster" class="form-select form-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value="DOAD_FRUIT">DOAD_FRUIT NEW</option>
			      		<option value="ISPL_FRUIT">DOAD_FRUIT<!-- ISPL FRUIT --></option>
			      		<option value="LCT_FRUIT">LCT FRUIT</option> 
			      		
			      </select>
			    </div>
			  </div>
			  <div class="row mb-2 ms-2 align-items-center" >
				    <label for="showSpeed" class="col-sm-4 col-form-label text-start"> Select Speed </label>
				    <div class="col-sm-6 col-md-6">
				        <select id="showSpeed" name="showSpeed" class="form-select form-select-sm"
				                onchange="processUserSelection(this)">
				            <option value="WITH" ${session_configuration.showSpeed == 'WITH' ? 'selected' : ''}>WITH</option>
				            <option value="WITHOUT" ${session_configuration.showSpeed == 'WITHOUT' ? 'selected' : ''}>WITHOUT</option>
				        </select>
				    </div>
				</div>
			   <div class="row mb-2 ms-2 align-items-center" >
			    <label for="speed_select" class="col-sm-4 col-form-label text-start"> Select Speed Type</label>
			    <div class="col-sm-6 col-md-6">
			      <select id="speed_select" name="speed_select" class="form-select form-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value="KP/H">KP/H</option>
			      		<option value="MP/H">MP/H</option>
			      </select>
			    </div>
			  </div>
			  <div class="row mb-2 ms-2 align-items-center" >
			    <label for="showReview" class="col-sm-4 col-form-label text-start"> Select Review </label>
			    <div class="col-sm-6 col-md-6">
			        <select id="showReview" name="showReview" class="form-select form-select-sm"
			                onchange="processUserSelection(this)">
			            <option value="WITH" ${session_configuration.showReview == 'WITH' ? 'selected' : ''}>WITH</option>
			            <option value="WITHOUT" ${session_configuration.showReview == 'WITHOUT' ? 'selected' : ''}>WITHOUT</option>
			        </select>
			    </div>
				</div>
			 <div class="row mb-2 ms-2 align-items-center" >
			    <label for="showSubs" class="col-sm-4 col-form-label text-start"> Select Subs </label>
			    <div class="col-sm-6 col-md-6">
			        <select id="showSubs" name="showSubs" class="form-select form-select-sm"
			                onchange="processUserSelection(this)">
			            <option value="WITH" ${session_configuration.showSubs == 'WITH' ? 'selected' : ''}>WITH</option>
			            <option value="WITHOUT" ${session_configuration.showSubs == 'WITHOUT' ? 'selected' : ''}>WITHOUT</option>
			        </select>
			    </div>
				</div>
			  <div class="row mb-2 ms-2 align-items-center" >
			    <label for="speed_select" class="col-sm-4 col-form-label text-start"> Audio </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_audio" name="select_audio" class="form-select form-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value=""></option>
			      		<option value="LastBallAudio">Last Ball Audio</option>
			      		<option value="LastBallEndOverAudio">Without Audio</option>
			      </select>
			    </div>
			  </div>
			  <div class="row mb-2 ms-2 align-items-center" >
			    <label for="select_Client" class="col-sm-4 col-form-label text-start">Select Client </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_Client" name="select_Client" class="form-select form-select-sm"
			      		onchange="processUserSelection(this)">
			      		<option value="0"></option>
			      		<option value="1">BCCI</option>
			      		<option value="2">Absolute Broadcast</option>
			      		<option value="3">ICC</option> 
			      		<option value="4">RISE</option>
			      		<option value="5">WILDTRACK</option> 
			      </select>
			    </div>
			  </div>
			<div class="row">
			<div class="table-responsive">
			  <table class="table table-bordered">
			  <tbody>
			    <tr>
			      <td>
				    <label for="vizIPAddress" class="col-sm-4 col-form-label text-start">Viz/Everest IP</label>				    
		             <input type="text" id="vizIPAddress" name="vizIPAddress" value="${session_configuration.primaryIpAddress}"
		             	class="form-control form-control-sm floatlabel">
			      </td>
			      <td>
				    <label for="vizPortNumber" class="col-sm-4 col-form-label text-start">Viz/Everest Port</label>				    
		             <input type="text" id="vizPortNumber" name="vizPortNumber" value="${session_configuration.primaryPortNumber}"
		             	class="form-control form-control-sm floatlabel">
			      </td>
			    </tr>
			  </tbody>
		    </table>
		    </div>
		    </div>		
			<button class="btn btn-primary btn-sm" type="button" name="load_scene_btn" id="load_scene_btn" 
				onclick="processUserSelection(this)">Load Scene</button>		      
	       </div>
	    </div>
       </div>
    </div>
  </div>
</div>
</form:form>
</body>
</html>