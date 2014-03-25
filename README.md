helloplay (WMtipp)
=========

Author:		Gianluca Most
Firma:		InnoQ
Datum:		Feb.2014-Mar.2014
Kontakt:	most.gianluca@gmail.com

Dieses Projekt ist ein kleines TippSpiel zur Fussball Weltmeisterschaft 2014 in Brasilien.
Es ist im Rahmen meines Praktikums bei der Firma innoQ entstanden.

IDE:
	eclipse
Frameworks:
	play 2.2.1
	bootstrap v3.1.1
API:
	rome 1.0 (RSS)
	jbcrypt 0.3m (password cryp)
Languages (in etwa anteilmaessig sortiert):
	java
	scala
	JPA
	mySQL
Database:
	SQL
	hibernate-entitymanager

Quellen:
	Mannschaften:		http://de.fifa.com/worldcup/
	Spielplan:			http://www.wm2014-infos.de/wm-2014-spielplan/
	Ergebnisse(RSS):	http://rss.kicker.de/live/wm
=========

Anmerkungen zum Code:

Haeufig verwendete Kuerzel:
	tr -> Trunde -> TippRunde
	u -> user
	cU -> currentUser (angemeldeter Benutzer (session))
	uid -> user id
	trid -> TippRunde id
	mid -> mannschaft id
	sid -> spiel id
	utrid -> user-TippRunde id (Abhaengigkeiten zwischen Benutzer und TippRunde)
		
=========
	
Moegliche Probleme:
	Die Ergebnisse werden nicht korrekt geladen bzw. in die Datenbank geschrieben:
		Moegliche Ursache:
			- Der RSS-feed-title sieht nicht konstant gleich aus oder liegt nicht in der erwarteten Form vor
		Fehlerbehebung:
			- Checken wie der Titel des feeds aussieht (Quelle s.o.) und Pattern in /app/models/Spiel.java.setResultWithRss() anpassen.
			- Wie oben und dann switch-case zur Unbenennung der Mannschaften auf Korrektheit der Schreibweise ueberpruefen.
		Moegliche Folgen:
			- Benutzer bekommen keine Punkte fuer ihre Tipps
			- Finalspiele werden nicht zum Tippen freigegeben

=========	

Changelog:


*Beim Anlegen von Benutzern wird jetzt ueberprueft ob dieser schon existiert (einmalige Benutzernamen)
+Benutzer können nun editiert werden.

+Login implementiert
+Logout implementiert
+nur autorisierter Zugang (login erforderlich)
*Benutzername und Logot-link wird nun auf allen content-sites im header angezeigt.

+bootstrap importiert
*main und login view designed

*mehrere design-improvements

*hp jetzt ohne login einsehbar
+aktueller Benutzer im header

+Passwoerter werden nun verschluesselt und beim einloggen dementsprechend ueberprueft.

+Die Registrieren-Seite wurde implementiert

+Models mit Attributen, Konstruktoren und Standard-Methoden hinzugefügt (mapping für er fehlt noch)
+accountverwaltung implementiert.
+MannschaftsController hinzugefuegt
+Unterscheidung zwischen admin, user und guest schon bei der hp
*admin-hp geaendert
*user-hp ausgebaut (mannschaftstabelle)

+Gruppenuebersicht hinzugefuegt
+Mannschaftsuebersicht hinzugefuegt

*Gruppenuebersicht in der Ansicht verbessert, es herrscht aber noch optimierungsbedarf am Code (grp_preview.scala.html -> 2much copy'n paste)

*Codeoptimierung in der Gruppenuebersicht

*Codeoptimierung in der Gruppen- und Detail-/Ansicht

+Relationen implementiert.
#Relationship fuer spiel<->mannschaft implementieren

+Relation fuer spiel<->mannschaft implementiert
#getter and setter anpassen(http://en.wikibooks.org/wiki/Java_Persistence/OneToMany#Getters_and_Setters)
	update: nicht noetig
	
#Mannschaftsuebersicht->Spielplan Gastmannschaft bugfix

*Spieluebersicht-bugfix
*several improvements

*Ansicht der Spieluebersicht optimiert.

+Kompletten Spielplan auf hp

*Es werden nur noch anstehende Spiele auf der hp angezeigt

+Es werden jetzt eigene Tipps auf der hp angezeigt
*Spiele erscheinen jetzt nicht mehr doppelt auf der hp

+Es können nun Tipps auf der hp abgegeben und aktualisiert werden
*design improvements in login and register-form

*Es werden jetzt wieder nur noch nicht fertige Spiele auf der hp angezeigt
*Auf der hp kann jetzt nur noch fuer nicht laufende Spiele getippt werden.

+Tippen nun auch in der Mannschafts-Uebersicht moeglich.
*diverese design- und code-verbesserungen
*hack in mannschaft.findAll() als Vorbereitung auf Finalspiel-Implementierung

*verschiedene Ansichts-Verbesserungen

+Erste annaeherung an RSSimplementierung (testphase)

*RSS jetzt implementiert und wird in Intervallen gecheckt
#RSS RegEx not final yet, seems to need exception handling

*accVerwaltung erweitert/optimiert
*userVerwaltung(admin-Bereich) erweitert/optimiert
*Tipps-template erweitert

*Tipps-template erweitert/optimiert

*accVerwaltung optimiert
*registrierung optimiert

+First Trunde-overview implementation

*modfied trunde, tipps and oneuser-view

*optimized TippRunde-overview
*several other improvements (mainly in views)

*Tippen auf Spiele ab dem AchtelFinale jetzt mit Freischalt-Datum (wenn Ergebnisse auf jeden Fall feststehen)
*navbar lässt sich nun richtig "collapsen"
*several design-improvements

+Benutzer koennen sich jetzt in mehreren TippRunden befinden.

*bugfixes (trunden-view)

+TrundeMain added
+Verlassen und Erstellen von Trunden moeglich
	#admin muss noch gesetzt werden

*admin wird jetzt korrekt gesetzt.
+man kann jetzt trunden per link beitreten

+Verschiedene invite-Moeglichkeiten(beta)
+Admin-Bereich und zusaetzlich admin-funktionen jetzt durch AdminSecured geschuetzt.
*code-improvements(flash-messages parametrisiert)

*hardcodierte Links aus templates entfernt
*einige URLs angepasst (routes)
*es wird nun ueber einen externen mail-client eingeladen (tr)

*kleine Optimierungen und BugFixes



=========
Legende:
	Leerzeile = neuer commit
	+Neues Feature
	*Verbesserung / Anpassung / Bugfix
	-Feature entfernt
	#ToDo. Meistens beim nachsten commit erledigt.