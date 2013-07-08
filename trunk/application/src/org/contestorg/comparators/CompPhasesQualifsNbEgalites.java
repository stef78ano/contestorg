package org.contestorg.comparators;

import org.contestorg.models.ModelParticipant;

/**
 * Comparateur qui prend en compte le nombre d'égalités aux phases qualificatives
 */
public class CompPhasesQualifsNbEgalites extends CompPhasesQualifs
{

	/**
	 * Constructeur
	 * @param sens sens (+1 pour un tri descendant, -1 pour un tri ascendant)
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifsNbEgalites(int sens,int phaseQualifMax) {
		this(null,sens,phaseQualifMax);
	}
	
	/**
	 * Constructeur
	 * @param comparateurSupp comparateur supplémentaire
	 * @param sens sens (+1 pour un tri descendant, -1 pour un tri ascendant)
	 * @param phaseQualifMax numéro de phase qualificative à ne pas dépasser (-1 pour ignorer le numéro de phase)
	 */
	public CompPhasesQualifsNbEgalites(CompPhasesQualifs comparateurSupp,int sens, int phaseQualifMax) {
		super(comparateurSupp,sens,phaseQualifMax);
	}

	/**
	 * @see CompPhasesQualifs#getValue(ModelParticipant, ModelParticipant, int)
	 */
	@Override
	protected double getValue (ModelParticipant participant, ModelParticipant adversaire, int phaseQualifMax) {
		return participant == null ? 0 : (double)participant.getNbEgalites(false,true,phaseQualifMax);
	}

	/**
	 * @see CompPhasesQualifs#getMaxParticipants()
	 */
	@Override
	protected int getMaxParticipants () {
		return -1;
	}

}
