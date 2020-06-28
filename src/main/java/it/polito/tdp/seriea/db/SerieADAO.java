package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.seriea.model.Adiacenza;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("team"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getSquadreStagione(Season stagione) {
		String sql = "SELECT DISTINCT (m.HomeTeam) AS team " + 
				"FROM matches AS m " + 
				"WHERE m.Season = ?" ;
		
		List<String> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, stagione.getSeason());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("team"));
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> getAdiacenze(Season stagione) {
		String sql = "SELECT m.HomeTeam AS s1, m.AwayTeam AS s2, (m.FTHG-m.FTAG) AS ris " + 
				"FROM matches AS m " + 
				"WHERE m.Season = ? " + 
				"GROUP BY m.HomeTeam, m.AwayTeam" ;
		
		List<Adiacenza> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, stagione.getSeason());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(res.getInt("ris")==0) {
					Adiacenza a = new Adiacenza(res.getString("s1"), res.getString("s2"), res.getInt("ris"));
					result.add(a);
				}
				if(res.getInt("ris")>0) {
					Adiacenza a = new Adiacenza(res.getString("s1"), res.getString("s2"), 1);
					result.add(a);
				}
				else {
					Adiacenza a = new Adiacenza(res.getString("s1"), res.getString("s2"), -1);
					result.add(a);
				}
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}


}
