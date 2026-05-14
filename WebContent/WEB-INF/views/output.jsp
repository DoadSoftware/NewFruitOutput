<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>

<html>
<head>

	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title>Output Screen</title>

	<!-- CSS -->
	<link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.1.3/css/bootstrap.min.css' />">
	<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/6.5.2/css/all.min.css' />">

	<!-- JS -->
	<script src="<c:url value='/webjars/jquery/3.6.0/jquery.min.js' />"></script>
	<script src="<c:url value='/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js' />"></script>
	<script src="<c:url value='/resources/javascript/index.js' />"></script>

   <Style>
  .Right {
	    display: flex;
	    flex-wrap: wrap;
	    align-items: center;
	}

	.Right h6 {
	    display: flex;
	    flex-wrap: wrap;
	    align-items: center;
	    color: red; /* Set text color to red */
	    margin: 10;
	}

	.Right label {
	    flex: 1; /* Ensure labels are flexible and take up available space */
	    margin-right: 10px; /* Optional: Add margin between labels for spacing */
	}

	.Right label:last-child {
	    margin-right:

  </Style>
  <script type="text/javascript">
 
  $(document).on("keydown", function(e){
	  
	  if(e.altKey && e.key === 'f'){
		  e.preventDefault();
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','DOAD_FRUIT');
	  }else if(e.altKey && e.key === 'r'){
   		  e.preventDefault()
   		  processUserSelectionData('LOGGER_FORM_KEYPRESS','RE_READ_DATA');
   	  }else if(e.altKey && e.key === 't'){
		  e.preventDefault();
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','DOAD_TEAM');
	  }else if(e.altKey && e.key === 'l'){
		  e.preventDefault();
		  processUserSelectionData('LOGGER_FORM_KEYPRESS','DOAD_LOGO');
	  }else if(e.key === "F1" || e.key === "F2" || e.key === "F3" || e.key === "F4" || e.key === "F5" || e.key === "F6" ||  e.key === "F7" || e.key === "F8" ||
			e.key === "F9" || e.key === "F10" || e.key === "F11" || e.key === "F12" || e.key === "ArrowDown" || e.key === "ArrowUp" || e.key === " " ||
			e.key === "PageUp" || e.key === "PageDown" || e.key === "1") {
	      // Suppress default behaviour 
	      // e.g. F1 in Chrome on Windows usually opens Windows help
	      e.preventDefault()
	      processUserSelectionData('LOGGER_FORM_KEYPRESS',e.which);
	  }else{
		  processUserSelectionData('LOGGER_FORM_KEYPRESS',e.which);
	  }
  }); 
  
  setInterval(() => {
	  processCricketProcedures('READ-MATCH-AND-POPULATE');		
	}, 1000);
 	
  </script>
</head>
<body>
<form:form name="output_form" autocomplete="off" action="POST">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Output</h3>
            <!--   <h3 class="mb-0">${licence_expiry_message}</h3>  -->
           </div>
          <div class="card-body">
			  <div id="select_graphic_options_div" style="display:none;">
			  </div>
			  <div id="captions_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label class="col-sm-4 col-form-label text-left">Match: ${session_match.match.matchFileName} </label>
			    <label class="col-sm-4 col-form-label text-left">Broadcaster: ${session_selected_broadcaster.replace("_"," ")} </label>
  			  </div>
  				<div class="Right">
  				<h6><label class="col-sm-3 col-form-label text-left"> Fruit: (Alt + F) </label>
	  				<c:if test="${(session_selected_broadcaster =='DOAD_FRUIT'||session_selected_broadcaster =='ISPL_FRUIT')}">
	  					<label class="col-sm-3 col-form-label text-left"> Teams: (Alt + T) </label>
	  					<label class="col-sm-3 col-form-label text-left"> Logo: (Alt + L) </label>	
	  				</c:if>
  				<label class="col-sm-3 col-form-label text-left" style = "margin-left: 40px;"> AnimateOut: (-) </label>
  				<label class="col-sm-3 col-form-label text-left" style = "margin-left: 40px;"> Clear All: (SpaceBar) </label></h6>
  				</div>
  				
  				<div class="left" style = "margin-left: 5px; font-size: 21px;">
	  				<label id="team1Details" class="col-sm-4 col-form-label text-left"> 
				    	${session_match.match.inning[0].batting_team.teamName4} </label>
				    <label id="team2Details" class="col-sm-4 col-form-label text-left"> 
				    	${session_match.match.inning[1].batting_team.teamName4} </label>
  				</div>
			  </div>
	       </div>
	    </div>
       </div>
    </div>
  </div>
<input type="hidden" id="which_keypress" name="which_keypress" value="${session_match.setup.which_key_press}"/>
<input type="hidden" name="selected_broadcaster" id="selected_broadcaster" value="${session_selected_broadcaster}"/>
<input type="hidden" name="speed_select" id="speed_select" value="${selected_speed}"/>
<input type="hidden" name="select_audio" id="select_audio" value="${select_audio}"/>
<input type="hidden" name="select_Client" id="select_Client" value="${select_Client}"/>
<input type="hidden" name="selected_match_max_overs" id="selected_match_max_overs" value="${session_match.setup.maxOvers}"/>
<input type="hidden" id="matchFileTimeStamp" name="matchFileTimeStamp" value="${session_match.setup.matchFileTimeStamp}"></input>
</form:form>
</body>
</html>