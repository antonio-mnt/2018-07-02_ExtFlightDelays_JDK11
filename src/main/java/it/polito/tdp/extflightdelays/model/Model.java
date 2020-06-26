package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private ExtFlightDelaysDAO dao;
	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> idMap;
	private List<Airport> aeroporti;
	private List<Arco> archi;
	private List<Airport> best;
	private double pesoTotale;
	private int bestSize;
	
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
	}
	
	
	public void creaGrafo(int distance) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.aeroporti = new ArrayList<>(this.dao.loadAllAirports());
		
		Graphs.addAllVertices(this.grafo, this.aeroporti);
		
		
		this.idMap = new HashMap<>();
		
		for(Airport a: this.grafo.vertexSet()) {
			this.idMap.put(a.getId(), a);
		}
		
		this.archi = new ArrayList<>(this.dao.loadArchi(this.idMap));
		
		for(Arco arco: this.archi) {
			
			DefaultWeightedEdge def = this.grafo.getEdge(arco.getA1(), arco.getA2());
			
			if(def==null) {
				double peso = this.dao.getPeso(arco.getA1(), arco.getA2());
				if(peso>distance) {
					Graphs.addEdgeWithVertices(this.grafo, arco.getA1(), arco.getA2(), peso);
					
				}
			}
			
		}

	}
	
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}


	public List<Airport> getAeroporti() {
		return aeroporti;
	}
	
	
	public List<Vicino> getVicini(Airport partenza) {
		
		
		List<Airport> aerop = new ArrayList<>(Graphs.neighborListOf(this.grafo, partenza));
		List<Vicino> vicini = new ArrayList<>();
		
		for(Airport a: aerop) {
			Vicino v = new Vicino(a,this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, a)));
			vicini.add(v);
		}
		
		Collections.sort(vicini);
		
		return vicini;
	}
	
	
	public void trovaPercorso(Airport partenza, double pesoMax) {
		
		this.best = null;
		this.pesoTotale = 0;
		
		
		List<Airport> parziale = new ArrayList<>();
		parziale.add(partenza);
		this.bestSize = 1;
		
		ricorsione(parziale, pesoMax);
		
		
	}


	private void ricorsione(List<Airport> parziale, double pesoMax) {
		
		
		double peso = calcolaPeso(parziale);
		if(peso > pesoMax) {
			return;
		}
		
		if(parziale.size()>this.bestSize && parziale.size()>1) {
			this.best = new ArrayList<>(parziale);
			this.pesoTotale = peso;
			this.bestSize = this.best.size();
		}
			
		Airport ultimo = parziale.get(parziale.size()-1);
		
		List<Airport> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		
		for(Airport a: vicini) {
			if(!parziale.contains(a)) {
				parziale.add(a);
				ricorsione(parziale, pesoMax);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}


	public double calcolaPeso(List<Airport> parziale) {
		
		double peso = 0;
		List<Airport> tempA = new ArrayList<>(parziale);
		Airport precedente  = tempA.get(0);
		tempA.remove(precedente);
		for(Airport a: tempA) {
			peso+= this.grafo.getEdgeWeight(this.grafo.getEdge(precedente, a));
			precedente  = a;
		}
		
		
		return peso;
	}


	public List<Airport> getBest() {
		return best;
	}


	public double getPesoTotale() {
		return pesoTotale;
	}
	
	

}
