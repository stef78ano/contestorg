﻿package models;

import interfaces.IEventListener;
import interfaces.IHistoryListener;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import common.Quadruple;
import common.TreeNodeAbstract;

import events.Action;
import events.Event;
import events.EventAdd;
import events.EventMove;
import events.EventRemove;
import events.EventUpdate;

@SuppressWarnings("serial")
public class TreeModelEquipes extends DefaultTreeModel implements IHistoryListener
{
	
	// Constructeur
	protected TreeModelEquipes(boolean remonterPoules, boolean remonterEquipes) {
		// Appeller le constructeur du parent
		super(new ConcoursTreeNode(FrontModel.get().getConcours(),remonterPoules, remonterEquipes));
		
		// Ecouter l'historique du concours
		FrontModel.get().getHistory().addListener(this);
	}
	
	// Rafraichir la structure de l'arbre
	private void refresh () {
		// Caster la node root
		ConcoursTreeNode root = (ConcoursTreeNode)this.getRoot();
		
		// FIXME Etre plus fin dans les fires tout en évitant de provoquer des exceptions
		
		// Fire des listeners
		for (Quadruple<TreeNode, TreeNode[], int[], TreeNode[]> event : root.getTreeNodesRemoved()) {
			//this.fireTreeNodesRemoved(event.getFirst(), event.getSecond(), event.getThird(), event.getFourth());
			this.reload(event.getFirst());
		}
		for (Quadruple<TreeNode, TreeNode[], int[], TreeNode[]> event : root.getTreeNodesInserted()) {
			//this.fireTreeNodesInserted(event.getFirst(), event.getSecond(), event.getThird(), event.getFourth());
			this.reload(event.getFirst());
		}
		for (Quadruple<TreeNode, TreeNode[], int[], TreeNode[]> event : root.getTreeNodesChanged()) {
			//this.fireTreeNodesChanged(event.getFirst(), event.getSecond(), event.getThird(), event.getFourth());
			this.reload(event.getFirst());
		}
		
		// Effacer les indicateurs
		root.clearIndicators();
	}
	
	// Implémentation de IHistoryListener
	@Override
	public void historyActionPushed (Action action) {
		// Fire des listeners
		this.refresh();
	}
	@Override
	public void historyActionPoped (Action action) {
		// Fire des listeners
		this.refresh();
	}
	@Override
	public void historyInit () {
		// Retenir le concours
		((ConcoursTreeNode)this.getRoot()).setConcours(FrontModel.get().getConcours());
		
		// Fire des listeners
		this.reload();
	}
	
	// Classe node représentant le concours
	private static class ConcoursTreeNode extends TreeNodeAbstract<ModelConcours> implements IEventListener
	{
		
		// Faut il remonter les poules/équipes ?
		private boolean remonterPoules;
		private boolean remonterEquipes;
		
		// Constructeur
		public ConcoursTreeNode(ModelConcours concours, boolean remonterPoules, boolean remonterEquipes) {
			// Appeller le constructeur parent
			super(concours);
			
			// Retenir s'il faut remonter les poules/équipes
			this.remonterPoules = remonterPoules;
			this.remonterEquipes = remonterEquipes;
			
			// Vérifier si le concours n'est pas nul
			if (this.object != null) {
				// Remplir la liste des nodes des catégories filles
				for (ModelCategorie categorie : this.object.getCategories()) {
					CategorieTreeNode node = new CategorieTreeNode(categorie, this.remonterPoules, this.remonterEquipes);
					this.children.add(node);
					node.setParent(this);
				}
				
				// Ecouter le concours
				this.object.addListener(this);
			}
		}
		
		// Définir le concours
		public void setConcours (ModelConcours concours) {
			// Retenir le concours
			this.object = concours;
			
			// S'assurer que la liste des catégories soit vide
			this.children.clear();
			
			// Vérifier si le concours n'est pas nul
			if (this.object != null) {
				// Remplir la liste des nodes des catégories filles
				for (ModelCategorie categorie : this.object.getCategories()) {
					CategorieTreeNode node = new CategorieTreeNode(categorie, this.remonterPoules, this.remonterEquipes);
					this.children.add(node);
					node.setParent(this);
				}
				
				// Ecouter le concours
				this.object.addListener(this);
			}
			
			// Nettoyer les indicateurs de modifications
			this.clearIndicators();
		}
		
		// Implémentation manquante de TreeNode
		@Override
		public boolean getAllowsChildren () {
			return this.object != null;
		}
		
		// Surcharge de getObject
		public Object getObject () {
			return this.object == null ? null : this.object.toInformation();
		}
		
		// Implémentation de IEventListener
		@Override
		public void event (Event event) {
			if (event instanceof EventAdd) {
				// Caster l'évenement
				EventAdd eventAdd = (EventAdd)event;
				
				// Vérifier s'il s'agit bien d'une catégorie qui a été ajoutée
				if (eventAdd.getAssociate() instanceof ModelCategorie) {
					// Ajouter la node enfant
					this.addChild(eventAdd.getIndex(),new CategorieTreeNode((ModelCategorie)eventAdd.getAssociate(), this.remonterPoules, this.remonterEquipes));
				}
			} else if (event instanceof EventRemove) {
				// Caster l'évenement
				EventRemove eventRemove = (EventRemove)event;
				
				// Vérifier s'il s'agit bien d'une catégorie qui a été supprimée
				if (eventRemove.getAssociate() instanceof ModelCategorie) {
					// Supprimer la node enfant
					this.removeChild(eventRemove.getIndex());
				}
			} else if (event instanceof EventMove) {
				// Caster l'évenement
				EventMove eventMove = (EventMove)event;
				
				// Vérifier s'il s'agit bien d'une categorie qui a été déplacée
				if (eventMove.getAssociate() instanceof ModelCategorie) {
					// Déplacer la node enfant
					this.moveChild(eventMove.getBefore(), eventMove.getAfter());
				}
			}
		}

	}
	
	// Classe node représentant une catégorie
	private static class CategorieTreeNode extends TreeNodeAbstract<ModelCategorie> implements IEventListener
	{
		
		// Faut il remonter les poules/équipes ?
		private boolean remonterPoules;
		private boolean remonterEquipes;
		
		// Constructeur
		public CategorieTreeNode(ModelCategorie categorie, boolean remonterPoules, boolean remonterEquipes) {
			// Appeller le constructeur du parent
			super(categorie);
			
			// Retenir s'il faut remonter les poules/équipes
			this.remonterPoules = remonterPoules;
			this.remonterEquipes = remonterEquipes;
			
			// Vérifier s'il faut remonter les poules
			if (this.remonterPoules) {
				// Remplir la liste des nodes des poules filles
				for (ModelPoule poule : this.object.getPoules()) {
					PouleTreeNode node = new PouleTreeNode(poule, this.remonterEquipes);
					this.children.add(node);
					node.setParent(this);
				}
			}
			
			// Ecouter la catégorie
			this.object.addListener(this);
		}
		
		// Implémentation de TreeNode
		@Override
		public boolean getAllowsChildren () {
			return this.remonterPoules;
		}
			
		// Surcharge de getObject
		public Object getObject() {
			return this.object.toInformation();
		}
		
		// Implémentation de IEventListener
		@Override
		public void event (Event event) {
			if (event instanceof EventAdd) {
				// Caster l'évenement
				EventAdd eventAdd = (EventAdd)event;
				
				// Vérifier s'il s'agit bien d'une poule qui a été ajoutée et s'il faut remonter les poules
				if (this.remonterPoules && eventAdd.getAssociate() instanceof ModelPoule) {
					// Ajouter la node enfant
					this.addChild(eventAdd.getIndex(), new PouleTreeNode((ModelPoule)eventAdd.getAssociate(), this.remonterEquipes));
				}
			} else if (event instanceof EventRemove) {
				// Caster l'évenement
				EventRemove eventRemove = (EventRemove)event;
				
				// Vérifier s'il s'agit bien d'une poule qui a été supprimée et s'il faut remonter les poules
				if (this.remonterPoules && eventRemove.getAssociate() instanceof ModelPoule) {
					// Supprimer la node enfant
					this.removeChild(eventRemove.getIndex());
				}
			} else if (event instanceof EventMove) {
				// Caster l'évenement
				EventMove eventMove = (EventMove)event;
				
				// Vérifier s'il s'agit bien d'une poule qui a été déplacée et s'il faut remonter les poules
				if (this.remonterPoules && eventMove.getAssociate() instanceof ModelPoule) {
					// Déplacer la node enfant
					this.moveChild(eventMove.getBefore(),eventMove.getAfter());
				}
			} else if(event instanceof EventUpdate) {
				// Signaler la modification de la node
				this.hasChanged();
			}
		}

	}
	
	// Classe node représentant une poule
	private static class PouleTreeNode extends TreeNodeAbstract<ModelPoule> implements IEventListener
	{
		// Faut il remonter les équipes ?
		private boolean remonterEquipes;
		
		// Constructeur
		public PouleTreeNode(ModelPoule poule, boolean remonterEquipes) {
			// Appeller le constructeur du parent
			super(poule);
			
			// Retenir s'il faut remonter les équipes
			this.remonterEquipes = remonterEquipes;
			
			// Vérifier s'il faut remonter les équipes
			if (this.remonterEquipes) {
				// Remplir la liste des nodes des équipes filles
				for (ModelEquipe equipe : this.object.getEquipes()) {
					EquipeTreeNode node = new EquipeTreeNode(equipe);
					this.children.add(node);
					node.setParent(this);
				}
			}
			
			// Ecouter la poule
			this.object.addListener(this);
		}
		
		// Implémentation manquante de TreeNode
		@Override
		public boolean getAllowsChildren () {
			return this.remonterEquipes;
		}
		
		// Surcharge de getObject
		public Object getObject () {
			return this.object.toInformation();
		}
				
		// Implémentation de IEventListener
		@Override
		public void event (Event event) {
			if (event instanceof EventAdd) {
				// Caster l'évenement
				EventAdd eventAdd = (EventAdd)event;
				
				// Vérifier s'il s'agit bien d'une équipe qui a été ajoutée et s'il faut remonter les équipes
				if (this.remonterEquipes && eventAdd.getAssociate() instanceof ModelEquipe) {
					// Ajouter la node enfant
					this.addChild(eventAdd.getIndex(), new EquipeTreeNode((ModelEquipe)eventAdd.getAssociate()));
				}
			} else if (event instanceof EventRemove) {
				// Caster l'évenement
				EventRemove eventRemove = (EventRemove)event;
				
				// Vérifier s'il s'agit bien d'une équipe qui a été supprimée et s'il faut remonter les équipes
				if (this.remonterEquipes && eventRemove.getAssociate() instanceof ModelEquipe) {
					// Supprimer la node enfant
					this.removeChild(eventRemove.getIndex());
				}
			} else if (event instanceof EventMove) {
				// Caster l'évenement
				EventMove eventMove = (EventMove)event;
				
				// Vérifier s'il s'agit bien d'une équipe qui a été déplacée et s'il faut remonter les équipes
				if (this.remonterEquipes && eventMove.getAssociate() instanceof ModelEquipe) {
					// Déplacer la node enfant
					this.moveChild(eventMove.getBefore(), eventMove.getAfter());
				}
			} else if(event instanceof EventUpdate) {
				// Signaler la modification de la node
				this.hasChanged();
			}
		}
	}
	
	// Classe node représentant une équipe
	private static class EquipeTreeNode extends TreeNodeAbstract<ModelEquipe> implements IEventListener
	{
		// Constructeur
		public EquipeTreeNode(ModelEquipe equipe) {
			// Appeller le constructeur parent
			super(equipe);
			
			// Ecouter l'équipe
			this.object.addListener(this);
		}
		
		// Implémentation manquante de TreeNode
		@Override
		public boolean getAllowsChildren () {
			return false;
		}
		
		// Surcharge de getObject
		public Object getObject () {
			return this.object.toInformation();
		}

		
		// Implémentation de IEventListener
		@Override
		public void event (Event event) {
			// Vérifier s'il s'agit d'un évenement de modification
			if(event instanceof EventUpdate) {
				// Signaler la modification de la node
				this.hasChanged();
			}
		}
				
	}
	
}
