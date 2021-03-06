helloplay (WMtipp)
=========

Author:		Gianluca Most
Firma:		InnoQ Deutschland GmbH
Datum:		Feb.2014-Mar.2014
GitHub:		https://github.com/GianlucaMost/helloplay
DB-dump:	https://drive.google.com/file/d/0Bx2ndN3Jq3k3NE8zRnJ6b3MxblU/edit?usp=sharing

Dieses Projekt ist ein kleines TippSpiel zur Fussball Weltmeisterschaft 2014 in Brasilien.
Es ist im Rahmen meines Praktikums bei der Firma innoQ entstanden.

IDE:
	eclipse
Frameworks:
	play 2.2.1
	bootstrap v3.1.1
API:
	JPA
	rome 1.0 (RSS)
	jbcrypt 0.3m (password cryp)
Languages (in etwa anteilmaessig sortiert):
	java
	scala
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
	p -> punkte
	a -> admin
	m -> mannschaft / member
	mh -> MannschaftHeim
	mg -> MannschaftGast
	s -> spiel
	t -> tipp
	th -> ToreHeim
	tg -> ToreGast
	acc -> account
	col -> collection
	pw -> password
	uid -> user id
	trid -> TippRunde id
	mid -> mannschaft id
	sid -> spiel id
	utrid -> user-TippRunde id (Abhaengigkeiten zwischen Benutzer und TippRunde)
	
Admin:
	Ein Benutzer kann Serverseitig zum Administrator ernannt werden (das mitspielen ist fuer ihn dann nicht mehr vorgesehen).
	mysql: UPDATE user SET admin=1 WHERE uid=X;
	ACHTUNG! Dieser Benutzer kann anschließend beliebige andere Benutzer zum Administrator ernennen, ihre Anmeldeinformationen aendern und ihren Account unwiederuflich loeschen!
		
=========
	
Moegliche Probleme:
	Die Ergebnisse werden nicht korrekt geladen bzw. in die Datenbank geschrieben:
		Moegliche Ursache:
			- Der RSS-feed-title sieht nicht konstant gleich aus oder liegt nicht in der erwarteten Form vor.
		Fehlerbehebung:
			- Checken wie der Titel des feeds aussieht (Quelle s.o.) und Pattern in /app/models/Spiel.java.setResultWithRss() anpassen.
			- Title checken (wie oben) und dann switch-case zur Unbenennung der Mannschaften auf Korrektheit der Schreibweise ueberpruefen.
		Moegliche Folgen:
			- Benutzer bekommen keine Punkte fuer ihre Tipps
			- Mannschaften die ab dem AchtelFinale gegeneinander spielen werden nicht richtig ermittelt
			- Finalspiele werden nicht zum Tippen freigegeben
			
	___			

	Die Finalisten werden nicht korrekt geladen bzw. in die Datenbank geschrieben:
		Moegliche Ursache:
			- s.o.
			- Algorithmus in models/Spiel.java -> setErgebnis() fehlerhaft.
			- Punktegleichstand in einer Gruppe zwischen mehr als 2 Mannschaften.
			- Alle angewendeten Regeln bringen keinen Gruppen-ersten/-zweiten hervor
		Fehlerbehebung:
			- mysql: UPDATE mannschaft SET status="Sieger/Zweiter <Gruppe>" WHERE mid=X;
		Moegliche Folgen:
			- Finalisten werden ab entsprechenden Zeitpunkt nicht gesetzt.
		
	___	
				
	Der Link einer e-Mail zur Einladung in eine TippRunde enthalten ist funktioniert nicht:
		Moegliche Ursache:
			- Der Link ist hard codiert in /app/views/trunde_detail.scala.html:55
		Fehlerbehebung:
			- Link anpassen
		Moegliche Folgen:
			- Es koennen keine neuen Benutzer zu TippRunden eingeladen werden

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

*code cleaning

*Hack aus mannschaft.findAll() entfernt
*Punkte werden nun an Mannschaften vergeben

*FinalSpiel-Vergabe angefangen (noch nicht alle Regeln fuer gg-entscheid fertig)

*FinalSpiel-Vergabe ready. Not tested yet.

*Final-Spiel-Vergabe now modular

*In TippRunde werden jetzt nur noch Tipps anderer angezeigt, wenn Spiel begonnen hat oder zu Ende ist.

*kleine Verbesserungen der TippRunden und Benutzer-Ansicht

*kleine Verbesserungen

*member und tipps in TippRunde jetzt sortiert

*spiele in tipps und mannschaft jetzt sortiert
*kleine optische Verbesserungen

*kleine Code-Verbesserungen
*Confirm-Abfragen hinzugefuegt
*Mail-client wird jetzt in neuem Tab geoeffnet

*verschiedene view-optimierungen

*alerts now dismissable
*verschiedene view-optimierungen

=========
Legende:
	Leerzeile = neuer commit
	+Neues Feature
	*Verbesserung / Anpassung / Bugfix
	-Feature entfernt
	#ToDo. Meistens beim nachsten commit erledigt.
