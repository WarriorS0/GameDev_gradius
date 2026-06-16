package engine.logs;

import java.util.logging.ConsoleHandler;

/**
 * Override du SystemOutHandler du java.util.logging
 * 
 * Cette classe permet juste d'écrire les logs dans la sortie normale et pas la
 * sortie des erreurs, évitant ainsi le texte rouge dans Eclipse.
 * 
 * Pour que ça marche comme il faut, ajouter
 * 
 * "-cp bin -Djava.util.logging.config.file=jul.properties"
 * 
 * dans les VM arguments du launch configuration d'Eclispe
 */
public class SystemOutHandler extends ConsoleHandler {
	public SystemOutHandler() {
		super();
		// On force l'utilisation de System.out au lieu de System.err
		setOutputStream(System.out);
	}
}
