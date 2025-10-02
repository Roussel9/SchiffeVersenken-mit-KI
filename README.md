
# Schiffe Versenken – Java Version

## Projektbeschreibung
„Schiffe Versenken“ ist eine digitale Umsetzung des klassischen Strategie- und Ratespiels für zwei Spieler, bei dem das Ziel ist, die Flotte des Gegners zu zerstören. In dieser Version hast du die Möglichkeit, gegen eine KI zu spielen, die ihre Schiffe intelligent platziert und auf die Züge des Spielers reagiert.

Dieses Projekt ist mein **erstes Java-Programmierprojekt** und zeigt meine Fähigkeit, Spiel-Logik, Benutzerinteraktion und algorithmische Denkweise zu kombinieren. Der Schwerpunkt liegt auf der Implementierung der Spielregeln, Spielfeldverwaltung und KI-Logik.

**Use Case:**  
- Spieler möchte gegen den Computer spielen  
- Grundlegendes Training für Strategie, Logik und Programmierfähigkeiten  

---

## Technologien & Tools
- **Sprache:** Java  
- **IDE:** IntelliJ IDEA / Eclipse / NetBeans (beliebige Java IDE)  
- **Bibliotheken:** Nur Standard-Java-Bibliotheken (keine externen Dependencies)  
- **Versionierung:** Git / GitHub  

---

## Architektur & Struktur
Das Projekt ist modular aufgebaut, um die Lesbarkeit und Erweiterbarkeit zu gewährleisten.

**Verzeichnisstruktur (Beispiel):**





**Datenfluss & Komponenten:**  
- `Main` startet das Spiel und initialisiert Spieler & KI  
- `Game` verwaltet das Spielfeld, überprüft Treffer/Verfehlen  
- `Player` liest Eingaben vom Benutzer ein  
- `AIPlayer` entscheidet Züge basierend auf bisherigen Treffern  
- `Ship` repräsentiert einzelne Schiffe und ihre Positionen  

---

## Funktionen & Features
- Standard-Spielregeln von „Schiffe Versenken“  
- Spiel gegen **KI** mit einfacher Strategie  
- Dynamisches Spielfeld, das Treffer und verfehlte Schüsse anzeigt  
- Spielende-Bedingungen: Alle Schiffe des Gegners zerstört  
- Konsolenbasiertes Interface – einfach und übersichtlich  

---

## KI & besondere Logik
- Die KI **platziert Schiffe zufällig**, achtet aber auf Kollisionen  
- Bei einem Treffer wählt die KI den nächsten Zug basierend auf angrenzenden Feldern  
- Einfache Entscheidungsstrategie, um menschliche Fehler nachzuahmen  

---

## Installation & Nutzung
1. **Projekt herunterladen / klonen:**  
```bash
git clone https://github.com/Roussel9/SchiffeVersenken-mit-KI.git
