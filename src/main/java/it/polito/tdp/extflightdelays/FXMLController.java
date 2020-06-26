package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare ai branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField distanzaMinima;

    @FXML
    private Button btnAnalizza;

    @FXML
    private ComboBox<Airport> cmbBoxAeroportoPartenza;

    @FXML
    private Button btnAeroportiConnessi;

    @FXML
    private TextField numeroVoliTxtInput;

    @FXML
    private Button btnCercaItinerario;

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	
    	int distanza;
    	
    	try {
    		
    		distanza = Integer.parseInt(this.distanzaMinima.getText());
    		
    	}catch(NumberFormatException ne) {
    		this.txtResult.setText("Formato distanza inserita non valido");
    		return;
    	}
    	
    	
    	this.model.creaGrafo(distanza);
    	
    	this.txtResult.setText("Grafo creato!\nVertici: "+this.model.getNumeroVertici()+" Archi: "+this.model.getNumeroArchi()+"\n");
    	
    	this.cmbBoxAeroportoPartenza.getItems().setAll(this.model.getAeroporti());

    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	
    	Airport a = this.cmbBoxAeroportoPartenza.getValue();
    	
    	if(a==null) {
    		this.txtResult.setText("Selezionare un aeroporto di partenza");
    		return;
    	}
    	
    	this.txtResult.clear();
    	
    	for(Vicino v: this.model.getVicini(a)) {
    		this.txtResult.appendText(v.toString()+"\n");
    	}

    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	
    	int numero;
    	
    	try {
    		
    		numero = Integer.parseInt(this.numeroVoliTxtInput.getText());
    		
    	}catch(NumberFormatException ne) {
    		this.txtResult.setText("Formato numero voli inserito non valido");
    		return;
    	}
    	
    	Airport a = this.cmbBoxAeroportoPartenza.getValue();
    	
    	if(a==null) {
    		this.txtResult.setText("Selezionare un aeroporto di partenza");
    		return;
    	}
    	
    	this.model.trovaPercorso(a, numero);
    	
    	
    	if(this.model.getBest() == null) {
    		this.txtResult.setText("Nessun percorso disponibile");
    		return;
    	}
    	List<Airport> percorso = new ArrayList<>(this.model.getBest());
    	
    	this.txtResult.clear();
    	for(Airport air: percorso) {
    		this.txtResult.appendText(air+"\n");
    	}
    	
    	this.txtResult.appendText("Migliaia totali: "+this.model.getPesoTotale());

    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
