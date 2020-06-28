package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private Graph<String, DefaultWeightedEdge> graph;
	private List<Adiacenza> adiacenze;
	private List<String> best;
	
	public Model() {
		this.dao = new SerieADAO();
		this.best = new ArrayList<>();
	}
	
	public List<Season> getStagioni() {
		return this.dao.listSeasons();
	}
	
	public void generateGraph(Season stagione) {
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.graph, this.dao.getSquadreStagione(stagione));
		
		adiacenze = new ArrayList<>(this.dao.getAdiacenze(stagione));
		for(Adiacenza a : adiacenze) {
			Graphs.addEdge(this.graph, a.getS1(), a.getS2(), a.getPunti());
		}
	}
	
	public Integer getNumVertici() {
		return this.graph.vertexSet().size();
	}
	
	public Integer getNumArchi() {
		return this.graph.edgeSet().size();
	}
	
	public Set<String> getVertici() {
		return this.graph.vertexSet();
	}
	
	public List<SquadraNumero> getPunti() {
		List<SquadraNumero> classifica = new ArrayList<>();
		
		for(String s : this.graph.vertexSet()) {
			Integer punti = 0;
			for(DefaultWeightedEdge e : this.graph.edgesOf(s)) {
				if(this.graph.getEdgeWeight(e)==0) {
					punti++;
				}
				if(this.graph.getEdgeWeight(e)>0) {
					punti += 3;
				}
			}
			SquadraNumero sn = new SquadraNumero(s, punti);
			classifica.add(sn);
		}
		Collections.sort(classifica);
		return classifica;
	}
	
	public List<String> getCammino(String squadra) {
		List<String> parziale = new ArrayList<>();
		parziale.add(squadra);
		List<Adiacenza> ad = new ArrayList<>();
		ricorsiva(parziale, ad);
		return best;
	}
	
	public void ricorsiva(List<String> parziale, List<Adiacenza> ad) {
		if(parziale.size()>this.best.size() && !best.isEmpty()) {
			this.best = new ArrayList<>(parziale);
		}
		
		String ultimoAggiunto = parziale.get(parziale.size()-1);
		
		for(DefaultWeightedEdge d : this.graph.outgoingEdgesOf(ultimoAggiunto)) {
			if(this.graph.getEdgeWeight(d)==1) {
				String successivo = Graphs.getOppositeVertex(this.graph, d, ultimoAggiunto);
				Adiacenza a = new Adiacenza(ultimoAggiunto, successivo, 0);
				if(!ad.contains(a)) {
					parziale.add(successivo);
					ad.add(a);
					ricorsiva(parziale, ad);
				
					// backtracking
					parziale.remove(successivo);
					ad.remove(a);
				}
			}
		}
	}

}
