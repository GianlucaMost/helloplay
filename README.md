helloplay
=========
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
	update: oder auch nicht :D
	
#Mannschaftsuebersicht->Spielplan Gastmannschaft bugfix

*Spieluebersicht-bugfix
*several improvements

*Ansicht der Spieluebersicht optimiert.

+Kompletten Spielplan auf hp

*Es werden nur noch anstehende Spiele auf der hp angezeigt