package de.pakldev.gw2evno;


import de.pakldev.gw2evno.gw2api.Events;
import de.pakldev.gw2evno.gw2api.MapNames;
import de.pakldev.gw2evno.gw2api.WorldNames;

public class Language {

	public static String language() {
		String worldLang = "Language";
		if( Configuration.language.equalsIgnoreCase(WorldNames.LANG_DE) ) worldLang = "Sprache";
		if( Configuration.language.equalsIgnoreCase(WorldNames.LANG_FR) ) worldLang = "Langue";
		if( Configuration.language.equalsIgnoreCase(WorldNames.LANG_ES) ) worldLang = "Idioma";

		return worldLang;
	}

	public static String world() {
		String worldLang = "World";
		if( Configuration.language.equalsIgnoreCase(WorldNames.LANG_DE) ) worldLang = "Welt";
		if( Configuration.language.equalsIgnoreCase(WorldNames.LANG_FR) ) worldLang = "Monde";
		if( Configuration.language.equalsIgnoreCase(WorldNames.LANG_ES) ) worldLang = "Mundo";

		return worldLang;
	}

	public static String map(){
		String mapLang = "Map";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) mapLang = "Karte";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) mapLang = "Carte";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) mapLang = "Mapa";

		return mapLang;
	}

	public static String skillChallenge() {
		String skillLang = "Skill Challenge";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) skillLang = "Fertigkeitsherausforderung";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) skillLang = "Défi de compétence";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) skillLang = "Desafío de habilidad";

		return skillLang;
	}

	public static String warmup() {
		String skillLang = "is now in warmup phase";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) skillLang = "ist in der Aufwärmphase";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) skillLang = "est maintenant en phase de préparation";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) skillLang = "se encuentra ahora en fase de preparar";

		return skillLang;
	}

	public static String preparation() {
		String skillLang = "is starting soon";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) skillLang = "beginnt bald";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) skillLang = "débute bientôt";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) skillLang = "está empezando pronto";

		return skillLang;
	}

	public static String active() {
		String skillLang = "has started";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) skillLang = "hat gerade begonnen";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) skillLang = "a commencé";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) skillLang = "ha comenzado";

		return skillLang;
	}

	public static String inactive() {
		String skillLang = "is waiting to start";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) skillLang = "wartet auf Beginn";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) skillLang = "est en attente pour démarrer";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) skillLang = "en espera de activación";

		return skillLang;
	}

	public static String helpMessage() {
		String helpLang = "To change to map quicker press CTRL+Backspace (everywhere, in game works too). A dialog should pop up to easily enter and search a map by its name. Press Enter when you found the right map or press ESC to cancel.";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) )
			helpLang = "Um die Karte schneller zu wechseln drücke STRG+Rücktaste (überall, im Spiel funktioniert auch). Ein Dialog sollte nun erscheinen wo du einfach den Kartennamen eingeben und suchen kannst. Drücke Enter wenn du deine Karte gefunden hast oder ESC um abzubrechen.";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) )
			helpLang = "Pour modifier pour cartographier rapidement appuyez sur CTRL+Backspace (partout, dans le jeu fonctionne aussi). Une boîte de dialogue devrait apparaître pour entrer facilement et rechercher une carte par son nom. Appuyez sur Entrée lorsque vous avez trouvé la bonne carte ou appuyez sur ESC pour annuler. <i>(Translated by Google Translator)</i>";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) )
			helpLang = "Para cambiar a asignar más rápido presione CTRL+Backspace (en todas partes, en el juego también funciona). Un cuadro de diálogo debería aparecer para entrar y buscar en el mapa por su nombre. Pulse Intro cuando encontraste el mapa de la derecha o presione ESC para cancelar. <i>(Translated by Google Translator)</i>";

		return helpLang;
	}

	public static String stateRefresh(){
		String mapLang = "Status refresh in";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) mapLang = "Statusaktualisierung in";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) mapLang = "Rafraîchissement de statut dans";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) mapLang = "Actualizar estado en";

		return mapLang;
	}

	public static String seconds(){
		String mapLang = "seconds";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) mapLang = "Sekunden";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) mapLang = "secondes";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) mapLang = "segundo";

		return mapLang;
	}

	public static String interestingOnly() {
		String mapLang = "Interesting events only";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) mapLang = "Nur interessante Events";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) mapLang = "Des événements intéressants seulement";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) mapLang = "Eventos de interés solamente";

		return mapLang;
	}

	public static String group() {
		String groupLang = "Group";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) groupLang = "Gruppe";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) groupLang = "Groupe";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) groupLang = "Grupo";

		return groupLang;
	}

	public static String state(int state) {
		String stateLang = "Successful";

		if( state == Events.STATE_SUCCESS ) {
			stateLang = "Successful";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) stateLang = "Erfolgreich";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) stateLang = "Succès";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) stateLang = "Exitosa";
		} else if( state == Events.STATE_FAIL ) {
			stateLang = "Failed";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) stateLang = "Fehlgeschlagen";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) stateLang = "Échec";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) stateLang = "Error";
		} else if( state == Events.STATE_ACTIVE ) {
			stateLang = "Active";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) stateLang = "Aktiv";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) stateLang = "Active";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) stateLang = "Activa";
		} else if( state == Events.STATE_WARMUP ) {
			stateLang = "Warming up";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) stateLang = "Aufwärmen";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) stateLang = "L'échauffement";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) stateLang = "Calentamiento";
		} else if( state == Events.STATE_PREPARATION ) {
			stateLang = "About to begin";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) stateLang = "Beginnt bald";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) stateLang = "Commencer";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) stateLang = "A punto de comenzar";
		} else if( state == Events.STATE_INACTIVE ) {
			stateLang = "Waiting/Inactive";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) stateLang = "Wartet/Inaktiv";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) stateLang = "Attente/Inactivité";
			if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) stateLang = "Espera/Inactividad";
		}

		return stateLang;
	}

}
