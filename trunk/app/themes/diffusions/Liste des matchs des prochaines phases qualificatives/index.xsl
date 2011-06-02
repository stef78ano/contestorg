﻿<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />
	<xsl:param name="idPoule" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<html>
			<!-- Head -->
			<head>
				<title>Matchs des prochaines phases qualificatives</title>
				<link rel="shortcut icon" href="sport.png" type="image/x-icon" />
				<link href="common.css" rel="stylesheet" type="text/css" />
				<link href="style.css" rel="stylesheet" type="text/css" />
				<script type="text/javascript" src="common.js"></script>
			</head>
			<!-- Body -->
			<body onload="setTimeout('scrollit()', 3000);">
				<!-- Titre -->
				<h1>Matchs des prochaines phases qualificatives</h1>
			
				<!-- Liste des catégories -->
				<xsl:apply-templates select="/concours/listeCategories">
				</xsl:apply-templates>
			</body>
		</html>
	</xsl:template>
	
	<!-- Template d'une liste de catégories -->
	<xsl:template match="listeCategories">
		<xsl:choose>
			<!-- Catégorie non indiquée -->
			<xsl:when test="$idCategorie = ''">
				<xsl:apply-templates select="./categorie">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Catégorie existante -->
			<xsl:when test="count(./categorie[@id=$idCategorie]) = 1">
				<xsl:apply-templates select="./categorie[@id=$idCategorie]">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Catégorie inexistante -->
			<xsl:otherwise>
				<p class="erreur">La catégorie indiquée n'existe pas.</p>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Template d'une catégorie -->
	<xsl:template match="categorie">
		<xsl:if test="count(./listePoules/poule/listePhasesQualificatives/phaseQualificative) != 0">
			<!-- Titre -->
			<xsl:if test="count(../categorie) != 1">
				<h2><xsl:value-of select="./@nom" /></h2>
			</xsl:if>
			
			<!-- Liste des poules -->
			<xsl:apply-templates select="./listePoules">
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'une liste de poules -->
	<xsl:template match="listePoules">
		<xsl:choose>
			<!-- Poule non indiquée -->
			<xsl:when test="$idPoule = ''">
				<xsl:apply-templates select="./poule">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Poule existante -->
			<xsl:when test="count(./poule[@id=$idPoule]) = 1">
				<xsl:apply-templates select="./poule[@id=$idPoule]">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Poule inexistante -->
			<xsl:otherwise>
				<p class="erreur">La poule indiquée n'existe pas.</p>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Template d'une poule -->
	<xsl:template match="poule">
		<xsl:if test="count(./listeEquipes/equipe) != 0">
			<!-- Titre -->
			<xsl:if test="count(../poule) != 1">
				<h3><xsl:value-of select="./@nom" /></h3>
			</xsl:if>
			
			<!-- Prochaine phase qualificative -->
			<xsl:apply-templates select="./listePhasesQualificatives/phaseQualificative[position() = last()]">
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'une phase qualificative -->
	<xsl:template match="phaseQualificative">
		<table>
			<thead>
				<tr>
					<th id="th-equipeA">
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipe A
							</xsl:when>
							<xsl:otherwise>
								Joueur A
							</xsl:otherwise>
						</xsl:choose>
					</th>
					<th id="th-equipeB">
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipe B
							</xsl:when>
							<xsl:otherwise>
								Joueur B
							</xsl:otherwise>
						</xsl:choose>
					</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="./matchPhaseQualificative">
				</xsl:apply-templates>
			</tbody>
		</table>
	</xsl:template>
	
	<!-- Template d'un match -->
	<xsl:template match="matchPhaseQualificative">
		<xsl:variable name="idA" select="./participation[1]/@refEquipe" />
		<xsl:variable name="idB" select="./participation[2]/@refEquipe" />
		<tr>
			<td class="td-equipeA">
				<xsl:choose>
					<xsl:when test="$idA != ''">
						<xsl:value-of select="//equipe[@id=$idA]/@nom" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipe fantôme
							</xsl:when>
							<xsl:otherwise>
								Joueur fantôme
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td class="td-equipeB">
				<xsl:choose>
					<xsl:when test="$idB != ''">
						<xsl:value-of select="//equipe[@id=$idB]/@nom" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipe fantôme
							</xsl:when>
							<xsl:otherwise>
								Joueur fantôme
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
