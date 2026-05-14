var session_match;
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
}
function initialiseForm(whatToProcess,dataToProcess)
{
	switch (whatToProcess) {
	case 'UPDATE-CONFIG':
		console.log(dataToProcess.showSubs);
	
		document.getElementById('configuration_file_name').value = $('#select_configuration_file option:selected').val();
		document.getElementById('select_cricket_matches').value = dataToProcess.filename;
		document.getElementById('select_broadcaster').value = dataToProcess.broadcaster;
		document.getElementById('speed_select').value = dataToProcess.speedUnit;
		document.getElementById('select_audio').value = dataToProcess.audio;
		document.getElementById('vizIPAddress').value = dataToProcess.primaryIpAddress;
		document.getElementById('vizPortNumber').value = dataToProcess.primaryPortNumber;
		document.getElementById('selectClient').value = dataToProcess.selectClient;
		document.getElementById('showSpeed').value = dataToProcess.showSpeed;
		document.getElementById('showReview').value = dataToProcess.showReview;
		document.getElementById('showSubs').value = dataToProcess.showSubs;
		break;
	case 'TEAMS_SCORE':
		 session_match.match.inning.forEach(function(hs){
			if(hs.inningNumber == 3 || hs.inningNumber == 4){
				
				var team1 ; var team2 ;
				   if(session_match.match.inning[2].batting_team.teamName1 == session_match.match.inning[0].batting_team.teamName1){
					  team1 = parseInt(session_match.match.inning[0].totalRuns) + (parseInt(session_match.match.inning[0].totalWickets) < 10 
				            ? "-" + session_match.match.inning[0].totalWickets : "") + " & ";
				      
				      team2 = parseInt(session_match.match.inning[1].totalRuns) + (parseInt(session_match.match.inning[1].totalWickets) < 10 
				            ? "-" + session_match.match.inning[1].totalWickets : "") + " & ";
				   }else{
					  team2 = parseInt(session_match.match.inning[0].totalRuns) + (parseInt(session_match.match.inning[0].totalWickets) < 10 
				            ? "-" + session_match.match.inning[0].totalWickets : "") + " & ";
				      
				      team1 = parseInt(session_match.match.inning[1].totalRuns) + (parseInt(session_match.match.inning[1].totalWickets) < 10 
				            ? "-" + session_match.match.inning[1].totalWickets : "") + " & ";
				   }
				   document.getElementById('team1Details').innerHTML = session_match.match.inning[2].batting_team.teamName4 + ' : ' + team1 + 
				  			parseInt(session_match.match.inning[2].totalRuns) + (parseInt(session_match.match.inning[2].totalWickets) < 10 
				            ? "-" + session_match.match.inning[2].totalWickets : "") + ' [' + parseInt(session_match.match.inning[2].totalOvers) + 
				            '.' + parseInt(session_match.match.inning[2].totalBalls) + ']';
				    
				   document.getElementById('team2Details').innerHTML = session_match.match.inning[3].batting_team.teamName4 + ' : ' + team2 + 
							parseInt(session_match.match.inning[3].totalRuns) + (parseInt(session_match.match.inning[3].totalWickets) < 10 
				            ? "-" + session_match.match.inning[3].totalWickets : "") + ' [' + parseInt(session_match.match.inning[3].totalOvers) + 
				            '.' + parseInt(session_match.match.inning[3].totalBalls) + ']';
				        
			}else if(hs.inningNumber == 1 || hs.inningNumber == 2){
				
				if(session_match.match.inning[0].totalWickets >= 10){
					document.getElementById('team1Details').innerHTML = session_match.match.inning[0].batting_team.teamName4 
					+ ' : ' + ChallengeScoreRUNS(session_match, session_match.match.inning[0]);
				}else {
					document.getElementById('team1Details').innerHTML = session_match.match.inning[0].batting_team.teamName4 
						+ ' : ' + ChallengeScoreRUNS(session_match, session_match.match.inning[0]) + ' - ' 
						+ session_match.match.inning[0].totalWickets;
				}
				
				if(session_match.match.inning[1].totalWickets >= 10){
					document.getElementById('team2Details').innerHTML = session_match.match.inning[1].batting_team.teamName4 
					+ ' : ' + ChallengeScoreRUNS(session_match, session_match.match.inning[1]);
				}else {
					document.getElementById('team2Details').innerHTML = session_match.match.inning[1].batting_team.teamName4 
						+ ' : ' + ChallengeScoreRUNS(session_match, session_match.match.inning[1]) + ' - ' 
						+ session_match.match.inning[1].totalWickets;
			   }
			}
		 });
		break;
	case 'initialise':
		processUserSelection($('#select_broadcaster'));
		break;
	}
}
function processUserSelectionData(whatToProcess,dataToProcess){
	
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		
		switch (dataToProcess) {
		case 'DOAD_FRUIT': 
			processCricketProcedures('POPULATE-FRUIT');
			break;
		case 'DOAD_TEAM':
			processCricketProcedures('POPULATE-TEAM');
			break;
		case 'DOAD_LOGO':
			processCricketProcedures('POPULATE-LOGO');
			break;
		case 32: //Space Bar
			processCricketProcedures('CLEAR-ALL');
			break;
		
		case 189: //Minus
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processCricketProcedures('ANIMATE-OUT');
			}
			break;
		case 49: case 50: case 51: case 52: // Key 1 to 4
			if(session_match.setup.maxOvers > 0){
				switch (dataToProcess) {
				case 51: case 52: // Key 1 to 4
					alert("3rd and 4th inning NOT available in a limited over match");
					return false;
				}				
			}
			document.getElementById('which_keypress').value = parseInt(dataToProcess) - 48; 
			document.getElementById('selected_inning').innerHTML = 'Selected Inning: ' + (parseInt(dataToProcess) - 48);
			break;
		}
	}
}
function processUserSelection(whichInput)
{	
	switch ($(whichInput).attr('name')) {
	
	case 'select_configuration_file':
		processCricketProcedures('GET-CONFIG-DATA');
		break;
	case 'load_scene_btn':
      	document.initialise_form.submit();
		break;
	}
}
function ChallengeScoreRUNS(match, inn) {
    let InningRun = 0;
    
    for (var i = match.eventFile.events.length - 1; i >= 0; i--) {
	  if (match.eventFile.events[i].eventExtra != null && match.eventFile.events[i].eventExtra.toUpperCase() == 'CHALLENGE' && 
	  		match.eventFile.events[i].eventInningNumber == inn.inningNumber && inn.totalOvers >= parseInt(match.eventFile.events[i].eventOverNo + 1)) {
		if(inn.specialRuns != null) {
			if (inn.specialRuns.startsWith('+')) {
				InningRun = parseInt(inn.totalRuns + parseInt(inn.specialRuns.replace('+', '')));
			}else if (inn.specialRuns.startsWith('-')) {
				InningRun = parseInt(inn.totalRuns - parseInt(inn.specialRuns.replace('-', '')));
			}
		}else {
			InningRun = parseInt(inn.totalRuns);
		}
    	break;
	  }else {
		InningRun = parseInt(inn.totalRuns);
		}
	} 
    return InningRun;
}
function processCricketProcedures(whatToProcess)
{
	var valueToProcess;
	
	switch(whatToProcess) {
	case 'GET-CONFIG-DATA':
		valueToProcess = $('#select_configuration_file option:selected').val();
		break;
	case 'READ-MATCH-AND-POPULATE':
		valueToProcess = $('#matchFileTimeStamp').val();
		break;
	case 'POPULATE-FRUIT':case'POPULATE-TEAM':case'POPULATE-LOGO':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'DOAD_FRUIT':
			valueToProcess = 'C:/EVEREST_FRUIT/Scenes/Fruit.sum';
			break;
		case 'ISPL_FRUIT':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_FRUIT/Scenes/Fruit.sum';
			break;
		case 'LCT_FRUIT': 
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_FRUIT_LCT/Scenes/Fruit.sum';
			break;
		}
		break;
	}

	$.ajax({    
        type : 'Get',     
        url : 'processCricketProcedures.html',     
        data : 'whatToProcess=' + whatToProcess + '&valueToProcess=' + valueToProcess, 
        dataType : 'json',
        success : function(data) {
        	switch(whatToProcess) {
			case 'GET-CONFIG-DATA':
				initialiseForm('UPDATE-CONFIG',data);
				break;
			case 'READ-MATCH-AND-POPULATE': case 'RE_READ_DATA':
				if(data){
					session_match = data;
					initialiseForm('TEAMS_SCORE',null);
				}
				break;
			case 'POPULATE-FRUIT':case'POPULATE-TEAM':case 'POPULATE-LOGO':
				if(confirm('Animate In?') == true){
					$('#select_graphic_options_div').empty();
					document.getElementById('select_graphic_options_div').style.display = 'none';
					$("#captions_div").show();
		        	switch(whatToProcess) {
					case 'POPULATE-FRUIT':
						//alert($('#selected_broadcaster').val());
						processCricketProcedures('ANIMATE-IN-FRUIT');
						break;
					case 'POPULATE-TEAM':
						processCricketProcedures('ANIMATE-IN-TEAM');
						break;
					case 'POPULATE-LOGO':
						processCricketProcedures('ANIMATE-IN-LOGO');
						break;
					}
				}
				break;
        	}
			processWaitingButtonSpinner('END_WAIT_TIMER');
	    },    
	    error : function(e) {    
	  	 	console.log('Error occured in ' + whatToProcess + ' with error description = ' + e);     
	    }    
	});
}