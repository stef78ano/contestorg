﻿package org.contestorg.views;


import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelParticipation;
import org.contestorg.infos.InfosModelParticipationObjectif;
import org.contestorg.interfaces.ICollector;



@SuppressWarnings("serial")
public class JDMatchPhasesQualifsEditer extends JDMatchPhasesQualifsAbstract
{

	// Constructeur
	public JDMatchPhasesQualifsEditer(Window w_parent, ICollector<Triple<Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, TrackableList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule, int numeroPhase, Triple<Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, Triple<String, ArrayList<Pair<String, InfosModelParticipationObjectif>>, InfosModelParticipation>, InfosModelMatchPhasesQualifs> infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer un match", collector, nomCategorie, nomPoule, numeroPhase);
		
		// Rajouter les participants qui ne peuvent plus participer mais qui ont joué pour ce match
		ArrayList<String> participants = ContestOrg.get().getCtrlPhasesQualificatives().getListeParticipants(nomCategorie, nomPoule);
		if(infos.getFirst().getFirst() != null && !participants.contains(infos.getFirst().getFirst())) {
			this.jcb_participantA.insertItemAt(infos.getFirst().getFirst(), 0);
			this.jcb_participantB.insertItemAt(infos.getFirst().getFirst(), 0);
		}
		if(infos.getSecond().getFirst() != null && !participants.contains(infos.getSecond().getFirst())) {
			this.jcb_participantA.insertItemAt(infos.getSecond().getFirst(), 0);
			this.jcb_participantB.insertItemAt(infos.getSecond().getFirst(), 0);
		}
		
		// Remplir la liste des objectifs remportés
		this.jp_prix.setObjectifsRemportesA(infos.getFirst().getSecond());
		this.jp_prix.setObjectifsRemportesB(infos.getSecond().getSecond());
		
		// Remplir les entrées avec les informations du match
		if(infos.getFirst().getFirst() != null) {
			this.jcb_participantA.setSelectedItem(infos.getFirst().getFirst());
		} else {
			this.jcb_participantA.setSelectedIndex(this.jcb_participantA.getItemCount()-1);
		}
		if(infos.getSecond().getFirst() != null) {
			this.jcb_participantB.setSelectedItem(infos.getSecond().getFirst());
		} else {
			this.jcb_participantB.setSelectedIndex(this.jcb_participantB.getItemCount()-1);
		}
		switch(infos.getFirst().getThird().getResultat()) {
			case InfosModelParticipation.RESULTAT_ATTENTE:
				this.jcb_resultatA.setSelectedIndex(0);
				break;
			case InfosModelParticipation.RESULTAT_VICTOIRE:
				this.jcb_resultatA.setSelectedIndex(1);
				break;
			case InfosModelParticipation.RESULTAT_EGALITE:
				this.jcb_resultatA.setSelectedIndex(2);
				break;
			case InfosModelParticipation.RESULTAT_DEFAITE:
				this.jcb_resultatA.setSelectedIndex(3);
				break;
			case InfosModelParticipation.RESULTAT_FORFAIT:
				this.jcb_resultatA.setSelectedIndex(4);
				break;
		}
		switch(infos.getSecond().getThird().getResultat()) {
			case InfosModelParticipation.RESULTAT_ATTENTE:
				this.jcb_resultatB.setSelectedIndex(0);
				break;
			case InfosModelParticipation.RESULTAT_VICTOIRE:
				this.jcb_resultatB.setSelectedIndex(1);
				break;
			case InfosModelParticipation.RESULTAT_EGALITE:
				this.jcb_resultatB.setSelectedIndex(2);
				break;
			case InfosModelParticipation.RESULTAT_DEFAITE:
				this.jcb_resultatB.setSelectedIndex(3);
				break;
			case InfosModelParticipation.RESULTAT_FORFAIT:
				this.jcb_resultatB.setSelectedIndex(4);
				break;
		}
		this.jta_details.setText(infos.getThird().getDetails());
	}
	
}
