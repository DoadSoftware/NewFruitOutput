package com.cricket.broadcaster;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.cricket.containers.Scene;
import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.MatchStats;
import com.cricket.model.Player;
import com.cricket.model.Review;
import com.cricket.model.Speed;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class ISPL_FRUIT extends Scene{

	public String session_selected_broadcaster = "ISPL_FRUIT";
	public String which_graphics_onscreen = "";
	boolean player_found = false,IsLastEventRead = false;
	int previousOver=-1,previousBall=-1,shotSize=0,EventSize=0;
	public String icon_path = "C:\\DOAD_In_House_Everest\\Everest_Cricket\\EVEREST_FRUIT\\Icons\\";
	List<String>str=new ArrayList<String>();
	
	public ISPL_FRUIT() {
		super();
	}
	
	public ISPL_FRUIT(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public void updateFruit(Scene scene, MatchAllData match,PrintWriter print_writer,Configuration config) throws Exception
	{
		populateFruit(match, print_writer,config);

	}
	
	public Object ProcessGraphicOption(String whatToProcess, MatchAllData match, CricketService cricketService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess,Configuration config) throws Exception{
		
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-FRUIT": 
			if(!which_graphics_onscreen.equalsIgnoreCase("FRUIT")||which_graphics_onscreen.isEmpty()) {
				scenes.get(0).setScene_path("D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_FRUIT/Scenes/Fruit.sum");
				scenes.get(0).scene_load(print_writer,session_selected_broadcaster);
				initialize_fruit(print_writer, match, config);
			}
			populateFruit(match, print_writer,config);
			break;
		case "POPULATE-TEAM":case "POPULATE-LOGO":
			if(which_graphics_onscreen.equalsIgnoreCase("FRUIT")||which_graphics_onscreen.isEmpty()) {
				 scenes.get(0).setScene_path("D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_FRUIT/Scenes/Fruit.sum");
				 scenes.get(0).scene_load(print_writer,session_selected_broadcaster);	
			}
    		switch (whatToProcess.toUpperCase()) {
    			case "POPULATE-TEAM":
    				populateTeam(match, print_writer,config);
				break;
    		}
		
		 break;
		case "ANIMATE-IN-FRUIT":case"ANIMATE-IN-TEAM": case "ANIMATE-IN-LOGO":case "ANIMATE-OUT": case "CLEAR-ALL": 
			switch (session_selected_broadcaster.toUpperCase()) {
			case "ISPL_FRUIT":
				switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-IN-FRUIT":
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPage 2;");	
					if(!which_graphics_onscreen.equalsIgnoreCase("FRUIT")||which_graphics_onscreen.isEmpty()) {
						processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
					}
					which_graphics_onscreen = "FRUIT";
					break;
				case"ANIMATE-IN-TEAM":
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPage 1;");
					if(!which_graphics_onscreen.equalsIgnoreCase("TEAM")||which_graphics_onscreen.isEmpty()) {
						processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
					}
					which_graphics_onscreen = "TEAM";
					break;
				case "ANIMATE-IN-LOGO":
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPage 0;");
					if(!which_graphics_onscreen.equalsIgnoreCase("TEAM")||which_graphics_onscreen.isEmpty()) {
						processAnimation(print_writer, "In", "START", session_selected_broadcaster,1);
					}
					which_graphics_onscreen = "TEAM";
					break;
				case "CLEAR-ALL":
					print_writer.println("LAYER1*EVEREST*SINGLE_SCENE CLEAR;");
					which_graphics_onscreen = "";
					break;
				case "ANIMATE-OUT":
					switch(which_graphics_onscreen) {
					case "FRUIT": case "TEAM":
						processAnimation(print_writer, "Out", "START", session_selected_broadcaster,1);
						which_graphics_onscreen = "";
						break;
					}
					break;
				}
			}
		}
			return null;
		}
	public void populateInfobar(PrintWriter print_writer,String viz_scene, MatchAllData match, String session_selected_broadcaster,Configuration config) throws Exception 
	{
		populateFruit(match, print_writer,config);
	}
	
	private void populateTeam(MatchAllData match, PrintWriter print_writer, Configuration config) throws InterruptedException {
		
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTounamentName " + match.getSetup().getTournament().toUpperCase() + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMatchNumber " + match.getSetup().getMatchIdent()+" : "+
				match.getSetup().getHomeTeam().getTeamName4()+" vs "+match.getSetup().getAwayTeam().getTeamName4()+ ";");

		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeTeamName " + 
				match.getSetup().getHomeTeam().getTeamName1() + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayTeamName " + 
				match.getSetup().getAwayTeam().getTeamName1() + ";");
		
		if(match.getSetup().getHomeSubstitutes() != null) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectTeamStyle 1;");

		}else {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectTeamStyle 0;");

		}

		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeTeamHeader" +  " " + 
				 "PLAYING XI ;");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeSubHeader"+ " " + 
				 " SUBSTITUTES;");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayTeamHeader" + " " + 
				 "PLAYING XI ;");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwaySubHeader" + " " + 
				 "SUBSTITUTES;");
		
		for(int k=1;k<=5;k++) {
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwaySubPlayer" + k + " " + 
		    			";");
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeSubPlayer" +k + " " + 
		    			";"); 
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$TeamPage$TeamsGrp$Style2$AwayGrp$AwaySubPlayerGrp$PlayerGrp"+k+"*FUNCTION*CONTAINER SET ACTIVE 0;");
			  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$TeamPage$TeamsGrp$Style2$HomeGrp$HomeSubPlayerGrp$PlayerGrp"+k+"*FUNCTION*CONTAINER SET ACTIVE 0;");
		  }
		
		int i=0;
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole2 C:\\DOAD_In_House_Everest\\Everest_Cricket\\EVEREST_FRUIT\\Icons\\Keeper.png ;");

		for(Player hs : match.getSetup().getHomeSquad()) {
				i = i+1;
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomePlayer" + i + " " + 
		    			hs.getFull_name() + ";");
				
				if(hs.getCaptainWicketKeeper() != null) {
					if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeCaptain" + i +
								" "+ " (C)" + ";");
					}else if(hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeCaptain" + i +
								" "+ " (C & WK)" + ";");
					}else if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeCaptain" + i +
								" "+ " (WK)" + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeCaptain" + i + " ;");
					}
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeCaptain" + i + " ;");
				}
				
				
				//subs 0
				if(match.getSetup().getHomeSubstitutes() != null) {
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeRole" + i + " " + 
							 hs.getRole().toUpperCase().replace("-", "").toUpperCase() + ";");
				}
				
				if(hs.getRole().toUpperCase().equalsIgnoreCase("BATSMAN")) {
					if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "Batsman"+CricketUtil.PNG_EXTENSION  + ";");
					}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "LeftHandBatsman"+CricketUtil.PNG_EXTENSION  + ";");
					}
					TimeUnit.MILLISECONDS.sleep(5);
				}else if(hs.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")||hs.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
					if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "Batsman" +CricketUtil.PNG_EXTENSION + ";");
					}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "LeftHandBatsman"+CricketUtil.PNG_EXTENSION  + ";");
					}
				}else if(hs.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
					if(hs.getBowlingStyle() == null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "FastBowler"+CricketUtil.PNG_EXTENSION  + ";");
					}else {
						switch(hs.getBowlingStyle()) {
						case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "FastBowler"+CricketUtil.PNG_EXTENSION  + ";");
							break;
						case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "SpinBowlerIcon" +CricketUtil.PNG_EXTENSION + ";");
							break;
						}
					}
				}else if(hs.getRole().toUpperCase().contains("ALL-ROUNDER")||hs.getRole().toUpperCase().contains("ALLROUNDER")) {
					if(hs.getBowlingStyle() == null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "FastBowlerAllrounder" +CricketUtil.PNG_EXTENSION + ";");
					}else {
						switch(hs.getBowlingStyle()) {
						case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "FastBowlerAllrounder" +CricketUtil.PNG_EXTENSION + ";");
							break;
						case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeRole" + i + " " + icon_path  + "SpinBowlerAllrounder"+CricketUtil.PNG_EXTENSION  + ";");
							break;
						}
					}
				}
			}
			int s=0;
			
			if(match.getSetup().getHomeSubstitutes() != null) {
				for(Player hs : match.getSetup().getHomeSubstitutes()) {
					if(!hs.getZone().equalsIgnoreCase("U19")) {
						String zoneText = "";
					    switch (hs.getZone().toUpperCase()) {
					        case "NORTH ZONE": zoneText = "NZ"; break;
					        case "EAST ZONE": zoneText = "EZ"; break;
					        case "SOUTH ZONE": zoneText = "SZ"; break;
					        case "WEST ZONE": zoneText = "WZ"; break;
					        case "CENTRAL ZONE": zoneText = "CZ"; break;
					        case "UNDER 19": zoneText = "U19"; break;
					        default: zoneText = hs.getZone() ; break;
					    }
					    
					    switch (hs.getZone().toUpperCase()) {
				        case "NORTH": zoneText = "NZ"; break;
				        case "EAST": zoneText = "EZ"; break;
				        case "SOUTH": zoneText = "SZ"; break;
				        case "WEST": zoneText = "WZ"; break;
				        case "CENTRAL": zoneText = "CZ"; break;
				        case "UNDER 19": zoneText = "U19"; break;
				        default: zoneText = hs.getZone() ; break;
					    }
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainHomeSubPlayer" + (s+1) + " " + 
								 hs.getFull_name() + "("+ zoneText +");"); 
						 if(hs.getRole().toUpperCase().equalsIgnoreCase("BATSMAN")) {
								if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "Batsman"+CricketUtil.PNG_EXTENSION  + ";");
								}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "LeftHandBatsman"+CricketUtil.PNG_EXTENSION  + ";");
								}
							}else if(hs.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")||hs.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
								if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "Batsman"+CricketUtil.PNG_EXTENSION  + ";");
								}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "LeftHandBatsman" +CricketUtil.PNG_EXTENSION + ";");
								}
							}else if(hs.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
								if(hs.getBowlingStyle() == null) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "FastBowler"+CricketUtil.PNG_EXTENSION  + ";");
								}else {
									switch(hs.getBowlingStyle()) {
									case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "FastBowler"+CricketUtil.PNG_EXTENSION  + ";");
										break;
									case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "SpinBowlerIcon" +CricketUtil.PNG_EXTENSION + ";");
										break;
									}
								}
							}else if(hs.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")||hs.getRole().toUpperCase().contains("ALLROUNDER")) {
								if(hs.getBowlingStyle() == null) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "FastBowlerAllrounder"+CricketUtil.PNG_EXTENSION  + ";");
								}else {
									switch(hs.getBowlingStyle()) {
									case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "FastBowlerAllrounder" +CricketUtil.PNG_EXTENSION + ";");
										break;
									case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainHomeSubRole" + (s+1) + " " + icon_path  + "SpinBowlerAllrounder"+CricketUtil.PNG_EXTENSION  + ";");
										break;
									}
								}
							}
						  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$TeamPage$TeamsGrp$Style2$HomeGrp$HomeSubPlayerGrp$PlayerGrp"+(s+1)+"*FUNCTION*CONTAINER SET ACTIVE 1 ;");
			   			 s++;
					} 
				}
			}
			
			
		int ai=0;
		for(Player hs : match.getSetup().getAwaySquad()) {		
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayPlayer" + (ai+1) + " " + 
	    			hs.getFull_name() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayRole" + (ai+1) + " " + 
					 hs.getRole().toUpperCase().replace("-", "").toUpperCase() + ";");
			
			if(hs.getCaptainWicketKeeper() != null) {
				if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayCaptain" + (ai+1) +
							" "+ " (C)" + ";");
				}else if(hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayCaptain" + (ai+1) +
							" "+ " (C & WK)" + ";");
				}else if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayCaptain" + (ai+1) +
							" "+ " (WK)" + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayCaptain" + (ai+1) + " ;");
				}
			}else {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayCaptain" + (ai+1) + " ;");
			}
			
			if(hs.getRole().toUpperCase().equalsIgnoreCase("BATSMAN")) {
				if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "Batsman" +CricketUtil.PNG_EXTENSION + ";");
				}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "LeftHandBatsman"+CricketUtil.PNG_EXTENSION  + ";");
				}
				
			}else if(hs.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")||hs.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
				if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "Batsman"+CricketUtil.PNG_EXTENSION  + ";");
				}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "LeftHandBatsman"+CricketUtil.PNG_EXTENSION  + ";");
				}
			}else if(hs.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
				if(hs.getBowlingStyle() == null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "FastBowler" +CricketUtil.PNG_EXTENSION + ";");
				}else {
					switch(hs.getBowlingStyle()) {
					case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "FastBowler"+CricketUtil.PNG_EXTENSION  + ";");
						break;
					case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "SpinBowlerIcon"+CricketUtil.PNG_EXTENSION  + ";");
						break;
					}
				}
			}else if(hs.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")||hs.getRole().toUpperCase().equalsIgnoreCase("ALLROUNDER")) {
				if(hs.getBowlingStyle() == null) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "FastBowlerAllrounder"+CricketUtil.PNG_EXTENSION  + ";");
				}else {
					switch(hs.getBowlingStyle()) {
					case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "FastBowlerAllrounder"+CricketUtil.PNG_EXTENSION  + ";");
						break;
					case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwayRole" + (ai+1) + " " + icon_path  + "SpinBowlerAllrounder"+CricketUtil.PNG_EXTENSION  + ";");
						break;
					}
				}
			}
			
			if(match.getSetup().getAwaySubstitutes() != null) {
			}else {
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwayRole" + (ai+1) + " " + 
		    			hs.getRole().toUpperCase().replace("-", "") + ";");
			}
			
			ai++;
			}
			int bi=0;
			
			if(match.getSetup().getAwaySubstitutes() != null) {
				for(Player hs : match.getSetup().getAwaySubstitutes()) {
					if(!hs.getZone().equalsIgnoreCase("U19")) {
						String zoneText = "";
					    switch (hs.getZone().toUpperCase()) {
					        case "NORTH ZONE": zoneText = "NZ"; break;
					        case "EAST ZONE": zoneText = "EZ"; break;
					        case "SOUTH ZONE": zoneText = "SZ"; break;
					        case "WEST ZONE": zoneText = "WZ"; break;
					        case "CENTRAL ZONE": zoneText = "CZ"; break;
					        case "UNDER 19": zoneText = "U19"; break;
					        default: zoneText = hs.getZone() ; break;
					    }
					    
					    switch (hs.getZone().toUpperCase()) {
				        case "NORTH": zoneText = "NZ"; break;
				        case "EAST": zoneText = "EZ"; break;
				        case "SOUTH": zoneText = "SZ"; break;
				        case "WEST": zoneText = "WZ"; break;
				        case "CENTRAL": zoneText = "CZ"; break;
				        case "UNDER 19": zoneText = "U19"; break;
				        default: zoneText = hs.getZone() ; break;
					    }
					    print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMainAwaySubPlayer" + (bi+1) + " " + 
								 hs.getFull_name() + "(" + zoneText + ");"); 
						 if(hs.getRole().toUpperCase().equalsIgnoreCase("BATSMAN")) {
								if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "Batsman"+CricketUtil.PNG_EXTENSION  + ";");
								}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "LeftHandBatsman"+CricketUtil.PNG_EXTENSION  + ";");
								}
							}else if(hs.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")||hs.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
								if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "Batsman" +CricketUtil.PNG_EXTENSION + ";");
								}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "LeftHandBatsman"+CricketUtil.PNG_EXTENSION  + ";");
								}
							}else if(hs.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
								if(hs.getBowlingStyle() == null) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "FastBowler"+CricketUtil.PNG_EXTENSION  + ";");
								}else {
									switch(hs.getBowlingStyle()) {
									case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "FastBowler" +CricketUtil.PNG_EXTENSION + ";");
										break;
									case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "SpinBowlerIcon"+CricketUtil.PNG_EXTENSION  + ";");
										break;
									}
								}
							}else if(hs.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
								if(hs.getBowlingStyle() == null) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "FastBowlerAllrounder"+CricketUtil.PNG_EXTENSION  + ";");
								}else {
									switch(hs.getBowlingStyle()) {
									case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "FastBowlerAllrounder" +CricketUtil.PNG_EXTENSION + ";");
										break;
									case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgMainAwaySubRole" + (bi+1) + " " + icon_path  + "SpinBowlerAllrounder"+CricketUtil.PNG_EXTENSION + ";");
										break;
									}
								}
							}
						  print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$Slect_Page$TeamPage$TeamsGrp$Style2$AwayGrp$AwaySubPlayerGrp$PlayerGrp"+(bi+1)+"*FUNCTION*CONTAINER SET ACTIVE 1 ;");
						 bi++;
					}
				}
			}
		}

	public void populateFruit( MatchAllData match,PrintWriter print_writer,Configuration config) throws Exception {
		BattingCard this_bc;
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + match.getMatch().getInning().get(0).getTotalFours() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue2 " + match.getMatch().getInning().get(0).getTotalSixes() + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue5 " + match.getMatch().getInning().get(0).getTotalNines() + ";");
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeTeamName " + 
						inn.getBatting_team().getTeamName2() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayTeamName " + 
						inn.getBowling_team().getTeamName2() + ";");
/********************************************************** EVENTS  *********************************************/
				MatchStats  Stats= CricketFunctions.getAllEvents(match,config.getBroadcaster(), match.getEventFile().getEvents());
/****************************************** Powerplay ***************************************************************/
				if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
		    			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
		    		}
					if(match.getSetup().getFollowOn().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "1" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPowerPlay " + "f/o" + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
					}
			    }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI) || match.getSetup().getMatchType().equalsIgnoreCase("OD")) {
			    	if(CricketFunctions.processPowerPlay(CricketUtil.MINI,match).isEmpty()) {
			    		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
			    	}else {
			    		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "1" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPowerPlay " + 
								CricketFunctions.processPowerPlay(CricketUtil.MINI,match) + ";");
			    	}
	    			
	    		}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) 
	    			|| match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)
	    			||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
	    			
	    			if(CricketFunctions.getBallCountStartAndEndRange(match, inn).get(1) > 
	    				(inn.getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver()) + inn.getTotalBalls())) {
	    				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "1" + ";");
	    				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPowerPlay " + "P" + ";");
	    				
	    			}else {
	    				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
	    			}
	    			
	    		}else {
	    			if(match.getSetup().getTargetOvers() != null ) {
	    				if(Double.valueOf(match.getSetup().getTargetOvers()) == 1) {
					    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "0" + ";");
					    }
	    			}
	    		}
				
/******************************************* Team total and comparision ***********************************************/
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBattingTeamName " + inn.getBatting_team().getTeamName4().toUpperCase() + ";");
				if(inn.getTotalWickets() >= 10) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTotalScore " + inn.getTotalRuns() + ";");
				}
				else{
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTotalScore " + inn.getTotalRuns() + "-" + inn.getTotalWickets() + ";");
				}
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls())+ ";");
				
				
				if(inn.getTotalPenalties() > 0) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPenalty " + "1" + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectPenalty " + "0" + ";");
				}

				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tExtras " + "EXTRAS: " + inn.getTotalExtras() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWideValue " + inn.getTotalWides() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNoBallValue " + inn.getTotalNoBalls() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tByeValue " + inn.getTotalByes() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLegByelValue " + inn.getTotalLegByes() + ";");
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPenaltyValue " + inn.getTotalPenalties() + ";");
/************************************************  comparision  ********************************************************************  */
				
				if(inn.getInningNumber() == 1) {
					
					if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tText " 
								+ match.getSetup().getHomeTeam().getTeamName1().toUpperCase() + 
								" ELECTED TO " + match.getSetup().getTossWinningDecision().toUpperCase() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tText " 
								+ match.getSetup().getAwayTeam().getTeamName1().toUpperCase() + 
								" ELECTED TO " + match.getSetup().getTossWinningDecision().toUpperCase() + ";");
					}						
						if(match.getMatch().getInning().get(0).getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName2()
									+"    (" +match.getMatch().getInning().get(0).getTotalRuns() +")" + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName2()
									+"    (" +match.getMatch().getInning().get(0).getTotalRuns() +" -"+match.getMatch().getInning().get(0).getTotalWickets()+")" + ";");
						}
						
						if(match.getMatch().getInning().get(1).getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName2() +
									"     (" +match.getMatch().getInning().get(1).getTotalRuns()+ ")" + ";");

						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName2() +
									"     (" +match.getMatch().getInning().get(1).getTotalRuns()+" -"+match.getMatch().getInning().get(1).getTotalWickets()+ ")" + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue2 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue3 " + "-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " + "-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " + "0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue5 " + "0" + ";");

				}else if(inn.getInningNumber() == 2) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tText " 
							+ CricketFunctions.GenerateMatchSummaryStatus(inn.getInningNumber(), match, CricketUtil.SHORT, "",session_selected_broadcaster,false).getTargetOrResult().toUpperCase() + ";");
						
						if(match.getMatch().getInning().get(0).getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName2()
									+"    (" +match.getMatch().getInning().get(0).getTotalRuns() +")" + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonHomeTeamName " + match.getMatch().getInning().get(0).getBatting_team().getTeamName2()
									+"    (" +match.getMatch().getInning().get(0).getTotalRuns() +" -"+match.getMatch().getInning().get(0).getTotalWickets()+")" + ";");
						}
						
						if(match.getMatch().getInning().get(1).getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName2() +
									"     (" +match.getMatch().getInning().get(1).getTotalRuns()+ ")" + ";");

						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tComparisonAwayTeamName " + match.getMatch().getInning().get(1).getBatting_team().getTeamName2() +
									"     (" +match.getMatch().getInning().get(1).getTotalRuns()+" -"+match.getMatch().getInning().get(1).getTotalWickets()+ ")" + ";");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + match.getMatch().getInning().get(0).getTotalFours() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue2 " + match.getMatch().getInning().get(0).getTotalSixes() + ";");

						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + inn.getTotalFours() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue2 " + inn.getTotalSixes() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue5 " + inn.getTotalNines() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue3 " + CricketFunctions.compareInningData(match, "-", 1, match.getEventFile().getEvents()) + ";");
						if(inn.getTotalWickets()==10) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " +inn.getTotalRuns()+ ";");

						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue3 " +inn.getTotalRuns()+" - "+inn.getTotalWickets() + ";");
	
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET AwayStatValue4 " +Stats.getAwayTeamScoreData().getTotalDots() + ";");
					
				}

				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue4 " + Stats.getHomeTeamScoreData().getTotalDots() + ";");				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText1 " + "BALL" + 
						CricketFunctions.Plural(Stats.getBallsSinceLastBoundary()).toUpperCase()+ " SINCE LAST BOUNDARY : " +Stats.getBallsSinceLastBoundary() + ";");
				
/****************************************** Projected And PhaseBy *************************************************************/
	
				if(inn.getInningNumber() == 1) {
					if(inn.getRunRate() != null) {
						str = CricketFunctions.projectedScore(match);
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType " + "0" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead1 " + 
								"@" +str.get(0) +" (CRR)" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue1 " + 
								str.get(1) + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead2 " + 
								"@" + str.get(2) +" RPO" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue2 " + 
								str.get(3) + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead3 " + 
								"@" + str.get(4) +" RPO" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue3 " + 
								str.get(5)+ ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType " + "0" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead1 " + 
								"-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue1 " + 
								"-" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead2 " + 
								"-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue2 " + 
								"-" + ";");
						
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedHead3 " + 
								"-" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tProjectedValue3 " + 
								"-" + ";");
					}
					
				}
				if(inn.getInningNumber() == 2 & inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
					 
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectDataType " + "1" + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName1 " + 
							match.getMatch().getInning().get(0).getBatting_team().getTeamName4() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName2 " + 
							match.getMatch().getInning().get(1).getBatting_team().getTeamName4() + ";");
					
					
					
					if((match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)||
							match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10))
								&&( match.getSetup().getTargetOvers()== null|| match.getSetup().getTargetOvers().isEmpty())) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$PhaseScoreHead*CONTAINER SET ACTIVE 1 ;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$Header*CONTAINER SET ACTIVE 1 ;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 " + 
									Stats.getHomeFirstPowerPlay().getTotalRuns()+"-"+Stats.getHomeFirstPowerPlay().getTotalWickets() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 " + 
									Stats.getHomeSecondPowerPlay().getTotalRuns()+"-"+Stats.getHomeSecondPowerPlay().getTotalWickets()+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 " + 
									Stats.getHomeThirdPowerPlay().getTotalRuns()+"-"+Stats.getHomeThirdPowerPlay().getTotalWickets() + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 " + 
									Stats.getAwayFirstPowerPlay().getTotalRuns()+"-"+Stats.getAwayFirstPowerPlay().getTotalWickets() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 " + 
									Stats.getAwaySecondPowerPlay().getTotalRuns()+"-"+Stats.getAwaySecondPowerPlay().getTotalWickets()+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 " + 
									Stats.getAwayThirdPowerPlay().getTotalRuns()+"-"+Stats.getAwayThirdPowerPlay().getTotalWickets() + ";");
						}
						else if((match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI))) {
							if(match.getSetup().getTargetOvers()== null|| match.getSetup().getTargetOvers().isEmpty()) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$PhaseScoreHead*CONTAINER SET ACTIVE 1 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$Header*CONTAINER SET ACTIVE 1 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 "
										+inn.getFirstPowerplayStartOver() + " - " +inn.getFirstPowerplayEndOver()+ ";"); 
								 print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 "
										 +inn.getSecondPowerplayStartOver()+ " - "+inn.getSecondPowerplayEndOver() + ";"); 
								 print_writer. println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 " 
										+inn.getThirdPowerplayStartOver() + " - "+inn.getThirdPowerplayEndOver() + ";");
							}
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 " + 
									Stats.getHomeFirstPowerPlay().getTotalRuns()+"-"+Stats.getHomeFirstPowerPlay().getTotalWickets() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 " + 
									Stats.getHomeSecondPowerPlay().getTotalRuns()+"-"+Stats.getHomeSecondPowerPlay().getTotalWickets()+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 " + 
									Stats.getHomeThirdPowerPlay().getTotalRuns()+"-"+Stats.getHomeThirdPowerPlay().getTotalWickets() + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 " + 
									Stats.getAwayFirstPowerPlay().getTotalRuns()+"-"+Stats.getAwayFirstPowerPlay().getTotalWickets() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 " + 
									Stats.getAwaySecondPowerPlay().getTotalRuns()+"-"+Stats.getAwaySecondPowerPlay().getTotalWickets()+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 " + 
									Stats.getAwayThirdPowerPlay().getTotalRuns()+"-"+Stats.getAwayThirdPowerPlay().getTotalWickets() + ";");	
							
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName2 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamName1 ;");
							  	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$PhaseScoreHead*CONTAINER SET ACTIVE 0 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$InningsDataGrp$PhaseScoreAll$Header*CONTAINER SET ACTIVE 0 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore1 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore2 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2PhaseScore3 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore1 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore2 ;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1PhaseScore3 ;");

						  }	
					
				}
/****************************************************Current Two Batsmen*******************************/
				if(inn.getPartnerships() != null && inn.getPartnerships().size() > 0) {
					
					this_bc = inn.getBattingCard().stream().filter(batcard -> batcard.getPlayerId() 
							== inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()).findAny().orElse(null);
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName1 " + this_bc.getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun1 " + this_bc.getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall1 " + this_bc.getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries1 " + this_bc.getFours() + "/" + 
						this_bc.getSixes() + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNines1 " + this_bc.getNines() + ";");
					if(this_bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(this_bc.getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {	
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1 1;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2 0;");
						
						}
					}
					
					
					if(this_bc.getStrikeRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + this_bc.getStrikeRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + "-" + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMinutes1 " + this_bc.getSeconds()/60 + ";");
					
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution1 " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterRuns() + "(" + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterBalls() + ")" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipContribution2 " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterRuns() + "(" + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterBalls() + ")" + ";");

					BattingCard this_bc1 = inn.getBattingCard().stream().filter(batcard -> batcard.getPlayerId() 
							== inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()).findAny().orElse(null);
	
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName2 " + this_bc1.getPlayer().getTicker_name() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun2 " + this_bc1.getRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall2 " + this_bc1.getBalls() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBoundaries2 " + this_bc1.getFours() + "/" + 
							this_bc1.getSixes() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNines2 " + this_bc1.getNines() + ";");
					
					if(this_bc1.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(this_bc1.getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker1 0;");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectStriker2 1;");
						}	
					}
					if(this_bc1.getStrikeRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate2 " + this_bc1.getStrikeRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate2 " + "-" + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMinutes2 " + this_bc1.getSeconds()/60 + ";");

					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipRus " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns() + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipBalls " + 
							inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls() + ";");
					
				}
				
/******************************************Run rate**********************************/
				
				if(inn.getInningNumber() == 1) {
					if(inn.getRunRate() != null) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
								"RUN RATE : " + inn.getRunRate() + ";");
					}else {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
								"RUN RATE : " + "-" + ";");
					}
					
				}else if(inn.getInningNumber() == 2) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tRunRate " + 
							"RRR: " + CricketFunctions.generateRunRate(CricketFunctions.GetTargetData(match).getRemaningRuns(), 0, 
									CricketFunctions.GetTargetData(match).getRemaningBall(), 2,match) + ";");
				}

/******************************************************* FALL OF WICKETS AND LAST WICKET *******************************/
				
				print_writer.println("LAYER1*EVEREST*TREEVIEW*Main$All$LastWicketGrp$DataGrp$XPosition$LastWicketValue*CONTAINER SET ACTIVE 1;");
				
				if(inn.getFallsOfWickets() != null && inn.getFallsOfWickets().size() > 0) {
					this_bc = inn.getBattingCard().stream().filter(batC -> inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowPlayerID() 
							== batC.getPlayerId()).findAny().orElse(null);
					
					if(this_bc != null) {
						String how_out = this_bc.getPlayer().getTicker_name()+"  "+this_bc.getHowOutText();
			
						if(how_out.length()>31) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " 
									+ this_bc.getPlayer().getTicker_name() +  "    ;");
						}else {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " 
									+ this_bc.getPlayer().getTicker_name() + "  " +this_bc.getHowOutText()+ "  ;");
						}
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketScore " 
									+" "+ this_bc.getRuns() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketBalls " 
									+ this_bc.getBalls() + ";");
					}
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketValue " + "" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketScore " + "" + ";");
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLastWicketBalls " + "" + ";");
				}
				if(inn.getInningNumber() == 1 && inn.getFallsOfWickets() != null) {
					
				    for (int i = 0; i <= inn.getFallsOfWickets().size() -1; i++)
				    {
				    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + (i+1) + " " + 
				    			inn.getFallsOfWickets().get(i).getFowRuns() + ";");
				    }
				    if(inn.getFallsOfWickets().size() < 10) {
				    	for(int j = inn.getFallsOfWickets().size() + 1; j <= 10; j++) {
					    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + j + " " + "" + ";");
					    }
				    }
				}else if(inn.getInningNumber() == 2) {
					if(match.getMatch().getInning().get(0).getFallsOfWickets() != null)
					{
					    for (int i = 0; i <= match.getMatch().getInning().get(0).getFallsOfWickets().size() -1; i++)
					    {
					    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + (i+1) + " " + 
					    			match.getMatch().getInning().get(0).getFallsOfWickets().get(i).getFowRuns() + ";");
					    }
					    if(match.getMatch().getInning().get(0).getFallsOfWickets().size() < 10) {
					    	for(int j = match.getMatch().getInning().get(0).getFallsOfWickets().size()+1; j <= 10; j++) {
						    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + j + " " + "" + ";");
						    }
					    }
					}
					
					if(match.getMatch().getInning().get(1).getFallsOfWickets() != null)
					{
					    for (int j = 0; j <= match.getMatch().getInning().get(1).getFallsOfWickets().size() -1; j++)
					    {
					    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2Fow" + (j+1) + " " + 
					    			match.getMatch().getInning().get(1).getFallsOfWickets().get(j).getFowRuns() + ";");
					    }
					    if(match.getMatch().getInning().get(1).getFallsOfWickets().size() < 10) {
					    	for(int j = match.getMatch().getInning().get(1).getFallsOfWickets().size()+1; j <= 10; j++) {
						    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2Fow" + j + " " + "" + ";");
						    }
					    }
					}
				}
/**************************************** BATTING CARD  *******************************************************************/
				if(inn.getFallsOfWickets()!=null) {
					if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId() && inn.getFallsOfWickets().size() > 0) {
								for(int i = 0; i <= inn.getFallsOfWickets().size() -1 ; i++) {
									player_found = false;
									if(inn.getFallsOfWickets().get(i) != null){
										if(player_found == false) {
											for(Player hs : match.getSetup().getHomeSquad()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == hs.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
											    			hs.getTicker_name() + ";");
													player_found = true;
												}
											}
										}
										
										if(player_found == false) {
											for(Player hos : match.getSetup().getHomeOtherSquad()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == hos.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
											    			hos.getTicker_name() + ";");
													player_found = true;
												}
											}
										}
										
										if(player_found == false) {
											if(match.getSetup().getHomeSubstitutes() != null) {
												for(Player hsub : match.getSetup().getHomeSubstitutes()) {
													if(inn.getFallsOfWickets().get(i).getFowPlayerID() == hsub.getPlayerId()){
														print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
																hsub.getTicker_name() + ";");
														player_found = true;
													}
												}
											}
											
										}int in=i;
										inn.getBattingCard().stream()
									    .filter(bc -> inn.getFallsOfWickets().get(in).getFowPlayerID() == bc.getPlayerId())
									    .forEach(bc -> {
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayer" + (in + 1) + " 1" + ";");
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectStriker" + (in + 1) + " 0" + ";");
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeade " +
									                " "+ ";");
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeScore" + (in + 1) + " " +
									                bc.getRuns() + ";");
									        print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeBalls" + (in + 1) + " " +
									                bc.getBalls() + ";");
									    });
									}
								}
					}else if(inn.getBattingTeamId() == match.getSetup().getAwayTeamId()) {
						if(inn.getFallsOfWickets() != null) {
							if(inn.getFallsOfWickets().size() > 0){
								for(int i = 0; i <= inn.getFallsOfWickets().size() -1 ; i++) {
									player_found = false;
									if(inn.getFallsOfWickets().get(i) != null){
										if(player_found == false) {
											for(Player as : match.getSetup().getAwaySquad()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == as.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
											    			as.getTicker_name() + ";");
												}
											}
										}
										if(player_found == false) {
											for(Player aos : match.getSetup().getAwayOtherSquad()) {
												if(inn.getFallsOfWickets().get(i).getFowPlayerID() == aos.getPlayerId()){
													print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
											    			aos.getTicker_name() + ";");
												}
											}
										}
										if(player_found == false) {
											if(match.getSetup().getAwaySubstitutes() != null) {
												for(Player asub : match.getSetup().getAwaySubstitutes()) {
													if(inn.getFallsOfWickets().get(i).getFowPlayerID() == asub.getPlayerId()){
														print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomePlayer" + (i+1) + " " + 
																asub.getTicker_name() + ";");
													}
												}
											}
											
										}
										int in=i;
										inn.getBattingCard().stream()
									    .filter(bc -> inn.getFallsOfWickets().get(in).getFowPlayerID() == bc.getPlayerId())
									    .forEach(bc -> {
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayer" + (in+1) + " 1" + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vHomeSelectStriker" + (in+1) + " 0" + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeade " + 
														" " + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeScore" + (in+1) + " " + 
														bc.getRuns() + ";");
												print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeBalls" + (in+1) + " " + 
														bc.getBalls() + ";");
									 });
										
									}
								}
							}
						}
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
							inn.getFallsOfWickets().size() + ";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber 0;"); 	
				}
/****************************************Current and last bowlers*******************************************************************/
				int id = 0;
				if(inn.getBowlingCard() != null) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "1" + ";");
							
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " +  boc.getDots() + ";");
							
							if(boc.getEconomyRate() == ""||boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " +boc.getEconomyRate()+ ";");
							}
						}
						else if(boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER)) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler2" + " " + "0" + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + boc.getPlayer().getTicker_name() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + boc.getWickets() + "-" + boc.getRuns() + ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " +  boc.getDots() + ";");
							
							id= Stats.getBowlingCard().getReplacementBowlerId();
							
							if(boc.getEconomyRate() == ""||boc.getEconomyRate() == null) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " +boc.getEconomyRate()+ ";");
							}
							
							}
							if(id==0) {
								id= Stats.getBowlingCard().getLastBowlerId();
							}
							if(id!=0) {
								if(boc.getPlayerId()==id) {
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2 " + boc.getPlayer().getTicker_name() + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2 " + boc.getWickets() + "-" + boc.getRuns() + ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2 " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+ ";");
									print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " +  boc.getDots() + ";");
									
									if(boc.getEconomyRate() == "") {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
									}else {
										print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " +boc.getEconomyRate()+ ";");
									}
								}	
							}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2  -;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2  -;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2  -;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2  -;");
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
							}
	/******************************************  THIS OVER  *****************************************************/
				if(match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverHead " + 
							"THIS OVER       "+ Stats.getOverData().getTotalRuns()+"  RUN"+ CricketFunctions.Plural(Integer.valueOf(Stats.getOverData().getTotalRuns())).toUpperCase() + 
							" (" + match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventExtra() 
							+ match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventExtraRuns() + ")"+";");
				}else {
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverHead " + 
							"THIS OVER       "+ Stats.getOverData().getTotalRuns()+"  RUN"+ CricketFunctions.Plural(Stats.getOverData().getTotalRuns()).toUpperCase() + ";");
				}
				if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER") || boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
					String str=replaceTermsInString(Stats.getOverData().getThisOverTxt().replace("9BOUNDARY", "9").replace("4BOUNDARY", "4").replace("6BOUNDARY", "6"));
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverData " + 
							reverseStringWithPreservation(str) + ";");
					
					if (((inn.getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) + inn.getTotalBalls()) >= 30)
					{	
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST 30 BALLS : " + 
								Stats.getLastThirtyBalls().getTotalRuns() + " RUN" + CricketFunctions.Plural(Stats.getLastThirtyBalls().getTotalRuns()).toUpperCase() + " , " + Stats.getLastThirtyBalls().getTotalWickets() +
								" WICKET" + CricketFunctions.Plural(Stats.getLastThirtyBalls().getTotalWickets()).toUpperCase()+ ";");
					}
					else  
					{
						if(inn.getTotalOvers() > 0) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : " + 
									Stats.getLastOverData().getTotalRuns()+ " RUN" +CricketFunctions.Plural(Stats.getLastOverData().getTotalRuns()).toUpperCase() +" AND " 
									+ Stats.getLastOverData().getTotalWickets() + " WICKET" +CricketFunctions.Plural(Stats.getLastOverData().getTotalWickets()).toUpperCase() + ";");	
						}else {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : ;");
						}
					
					}
				}
/**************************************** BOWLING CARD  *******************************************************************/
				if(inn.getBowlingCard() != null) {
					for(int i = 0; i <= inn.getBowlingCard().size() -1 ; i++) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayPlayer" + (i+1) + " " + 
								inn.getBowlingCard().get(i).getPlayer().getTicker_name() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayer" + (i+1) + " 1" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayStriker3 0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vAwaySelectStriker" + (i+1) + " 0" + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayScore" + (i+1) + " " + 
								inn.getBowlingCard().get(i).getWickets() + "-" + inn.getBowlingCard().get(i).getRuns() + ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayBalls" + (i+1) + " " + 
								CricketFunctions.OverBalls(inn.getBowlingCard().get(i).getOvers(), inn.getBowlingCard().get(i).getBalls()) + ";");
					}
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
						inn.getBowlingCard().size() + ";");
					
				   }else {
					   print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber 0;"); 
				   }
				}
			}
				if(config.getAudio().equalsIgnoreCase(CricketUtil.LastBallAudio)) // LastBall 
				{
					if(inn.getTotalBalls()==5 && previousOver != inn.getTotalOvers()) {
						if(new File(CricketUtil.CRICKET_DIRECTORY+"Audio/Last ball.WAV").exists()) {
							playAudio(CricketUtil.CRICKET_DIRECTORY+"Audio/Last ball.WAV");	
						}
						previousOver = inn.getTotalOvers();
						//previousBall = inn.getTotalBalls();
					}
				}
			}
			
		}
		
	}

	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		switch(which_broadcaster.toUpperCase()) {
		case CricketUtil.ISPL_FRUIT:
			switch(which_layer) {
			case 1:
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				break;
				
			case 2:
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*" + animationName + " " + animationCommand + ";");
				break;
			}
			break;
		}
		
	}	 	
	public void initialize_fruit(PrintWriter print_writer, MatchAllData match ,Configuration config) throws IOException {
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedHead " + 
    			"SPEED" + ";");
	
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedUnit " + 
							"("+config.getSpeedUnit()+")" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTounamentName " + match.getSetup().getTournament().toUpperCase() + ";");
		
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tMatchNumber " + match.getSetup().getMatchIdent()+" : "+
				match.getSetup().getHomeTeam().getTeamName4()+" vs "+match.getSetup().getAwayTeam().getTeamName4()+ ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tWideHead " + "WD" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tNoBallHead " + "NB" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tByeHead " + "B" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tLegByelHead " + "LB" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPenaltyHead " + "PEN" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead1 " + "FOURS" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead2 " + "SIXES" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead3 " + "AT THIS STAGE" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStatHead4 " + "DOTS" + ";");
		//CURR P'SHIP
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipHead " + 
				"PARTNERSHIP" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue " +"  "
	            + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tAwayStatValue1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tHomeStatValue1 " + "-" + ";");
		
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " + "SUMMARY " + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy2 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectBowler1" + " " + "0" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFowTeamName1 " + 
				match.getMatch().getInning().get(0).getBatting_team().getTeamName4() + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFowTeamName2 " + 
				match.getMatch().getInning().get(1).getBatting_team().getTeamName4() + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBowlerName1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tFigure1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tOvers1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tDots1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tEconomy1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tThisOverData " + " " + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName1 " +  ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate1 " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterName2 " +  ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tStrikeRate " + "-" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSmallFreeText2 " + "LAST OVER : " + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipRus " +";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPartnershipBalls " +";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun1 " +";"); 
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterRun2 " +";"); 
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall1 " +";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tBatterBall2 " +";"); 



		for (int i = 1; i <= 10; i++)
	    {
	    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam1Fow" + i + " " + 
	    			"" + ";");
	    	print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeam2Fow" + i + " " + 
	    			"" + ";");
	    }
		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD)) {
			
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 " + 
					"1-10" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 " + 
					"11-40" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 " + 
					"41-50" + ";");
			
		}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 " + 
					"1-6" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 " + 
					"7-16" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 " + 
					"17-20" + ";");
			
		}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead1 " + 
					"1-2" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead2 " + 
					"3-6" + ";");
			print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPhaseHead3 " + 
					"7-10" +";");
		}
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectHomePlayerNumber " + 
				"0" + ";");
		print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vSelectAwayPlayerNumber " + 
				"0" + ";");
		
	}	
	public void populateSpeed(PrintWriter printWriter, Speed lastSpeed) throws Exception {
		
		if(lastSpeed != null) {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue " + lastSpeed.getSpeedValue().substring(1)  + ";");
		}else {
			printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tSpeedValue;");
		}			      
	} 	
	public void populateReview(PrintWriter printWriter, MatchAllData match, Review review) throws Exception {
		switch (this.session_selected_broadcaster) {
		case CricketUtil.ISPL_FRUIT:
			if(review == null) {
	    		printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " + "SUMMARY " + ";");
			}else {
				String homeTeam = match.getSetup().getHomeTeam().getTeamName4(), awayTeam = match.getSetup().getAwayTeam().getTeamName4();
				printWriter.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tTeamsHeader " +  "REVIEWS REMAINING :-" + 
	    				String.format("%-10s",homeTeam) + " : " + review.getReviewStatus().split(",")[0]+"   " + 
						String.format("%-8s", awayTeam + " : " + review.getReviewStatus().split(",")[1]) + ";");	
			}			      
			break;
		}
	}

	public static String replaceTermsInString(String input) {
	    if (input != null && !input.isEmpty() && (input.contains("WIDE") || input.contains("NO_BALL") ||
	            input.contains("LEG_BYE") || input.contains("BYE") || input.contains("PENALTY") ||
	            input.contains("LOG_WICKET") || input.contains("WICKET"))) {
	        input = input.replace("WIDE", "wd")
	                .replace("NO_BALL", "nb")
	                .replace("LEG_BYE", "lb")
	                .replace("BYE", "b")
	                .replace("PENALTY", "pn")
	                .replace("LOG_WICKET", "w")
	                .replace("WICKET", "w");
	    }
	    return input;
	}

	public static String reverseStringWithPreservation(String input) {
        String[] parts = input.split("\\s*\\,\\s*"); // Split the string by "|"
        StringBuilder reversedString = new StringBuilder();

        for (int i = parts.length - 1; i >= 0; i--) {
            reversedString.append(parts[i]);
            if (i > 0) {
                reversedString.append(" | "); // Add back the "|" separator
            }
        }

        return reversedString.toString();
    }
	public static void playAudio(String audioFilePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if(new File(audioFilePath).exists()) {
			 File audioFile = new File(audioFilePath);
			 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
			 Clip clip = AudioSystem.getClip();
			 clip.open(audioInputStream);
			 clip.start();
			 while(clip.isRunning()) {
				 try {
					 Thread.sleep(100);
				 }catch(Exception e) {
					 e.printStackTrace();
				 }
			 }
		}
	}
}