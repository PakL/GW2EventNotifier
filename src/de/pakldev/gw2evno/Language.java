package de.pakldev.gw2evno;


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
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) skillLang = "est maintenant en phase d'échauffement";
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) skillLang = "se encuentra ahora en fase de calentamiento";

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

}
