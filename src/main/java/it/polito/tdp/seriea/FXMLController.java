package it.polito.tdp.seriea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SquadraNumero;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Season> boxSeason;

    @FXML
    private ChoiceBox<String> boxTeam;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCarica(ActionEvent event) {
    	this.txtResult.clear();
    	Season stagione = this.boxSeason.getValue();
    	if(stagione==null) {
    		this.txtResult.appendText("Scegliere una stagione dalla tendina!\n");
    		return;
    	}
    	
    	this.model.generateGraph(stagione);
    	this.txtResult.appendText("Il grafo è stato creato con successo!\n");
    	this.txtResult.appendText("Il numero di vertici del grafo è: "+this.model.getNumVertici()+"\n");
    	this.txtResult.appendText("Il numero di archi del grafo è: "+this.model.getNumArchi()+"\n");
    	
    	this.txtResult.appendText("La classifica delle squadre è:\n\n");
    	List<SquadraNumero> classifica = this.model.getPunti();
    	for(SquadraNumero sn : classifica) {
    		this.txtResult.appendText(sn.toString()+"\n");
    	}
    	
    	this.boxTeam.getItems().setAll(this.model.getVertici());
    }

    @FXML
    void handleDomino(ActionEvent event) {
    	this.txtResult.clear();
    	String squadra = this.boxTeam.getValue();
    	if(squadra==null) {
    		this.txtResult.appendText("Scegliere una squadra dalla tendina!\n");
    		return;
    	}
    	
    	this.txtResult.appendText("Il cammino di lunghezza massima è:\n\n");
    	
    	List<String> cammino = this.model.getCammino(squadra);
    	for(String s : cammino) {
    		this.txtResult.appendText(s+"\n");
    	}
    }

    @FXML
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxSeason.getItems().setAll(this.model.getStagioni());
	}
}
