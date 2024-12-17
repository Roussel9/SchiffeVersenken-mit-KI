class SchiffeVersenken {
    constructor(canvasElement, instanceID) {
        this.canvas = canvasElement;
        this.context = this.canvas.getContext("2d");
        this.instanceID = instanceID;
        this.feldGroesse = 50; // Jede Zelle auf dem Spielfeld ist 50x50 Pixel groß
        this.spielfeldSpieler = []; // Spielfeld des Spielers (10x10)
        this.spielfeldKI = []; // Spielfeld der KI (10x10)
        this.initSpielfeld();
    }

    // Initialisiert das Spielfeld (10x10)
    initSpielfeld() {
        // Initialisiere Spielfeld für Spieler und KI
        for (let i = 0; i < 10; i++) {
            let zeileSpieler = [];
            let zeileKI = [];
            for (let j = 0; j < 10; j++) {
                zeileSpieler.push("~"); // '~' bedeutet Wasser
                zeileKI.push("~"); // '~' bedeutet Wasser
            }
            this.spielfeldSpieler.push(zeileSpieler);
            this.spielfeldKI.push(zeileKI);
        }
        this.zeichneSpielfeld(); // Zeichne das Spielfeld für beide (Spieler und KI)
    }

    // Zeichnet das Spielfeld auf dem Canvas (sowohl für den Spieler als auch für die KI)
    zeichneSpielfeld() {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height); // Löscht das Canvas

        // Zeichne das Spielfeld des Spielers
        for (let i = 0; i < 10; i++) {
            for (let j = 0; j < 10; j++) {
                this.zeichneFeld(i, j, 0); // Spielerfeld oben
            }
        }

        // Zeichne das Spielfeld der KI
        for (let i = 0; i < 10; i++) {
            for (let j = 0; j < 10; j++) {
                this.zeichneFeld(i, j, 1); // KI-Feld unten
            }
        }
    }

    // Zeichnet ein einzelnes Feld (Zelle) auf dem Spielfeld
    zeichneFeld(x, y, feldTyp) {
        let status = (feldTyp === 0) ? this.spielfeldSpieler[x][y] : this.spielfeldKI[x][y];

        // Bestimme die Farbe
        if (status === "~") {
            this.context.fillStyle = "#87CEEB"; // Wasserfarbe
        } else if (status === "S") {
            this.context.fillStyle = "#8B4513"; // Schifffarbe
        } else if (status === "X") {
            this.context.fillStyle = "#FF0000"; // Treffer
        } else if (status === "O") {
            this.context.fillStyle = "#A9A9A9"; // Verfehlt
        }

        // Berechne die Position für das Feld (Berücksichtige, dass die KI das untere Spielfeld hat)
        let offsetX = feldTyp === 0 ? 0 : 10; // Spieler ist oben, KI unten
        let offsetY = feldTyp === 0 ? 0 : 10;

        // Zeichne das Rechteck
        this.context.fillRect(y * this.feldGroesse, (x + offsetX) * this.feldGroesse, this.feldGroesse, this.feldGroesse);
        this.context.strokeRect(y * this.feldGroesse, (x + offsetY) * this.feldGroesse, this.feldGroesse, this.feldGroesse); // Rand des Feldes
    }

    // Zeichnet ein Schiff auf dem Spielfeld (in einem bestimmten Feld)
    zeichneSchiff(x, y, horizontal) {
        this.spielfeldSpieler[x][y] = "S"; // 'S' steht für ein Schiff
        this.zeichneSpielfeld(); // Das Spielfeld nach dem Platzieren neu zeichnen

        if (horizontal) {
            // Zeichne horizontal weiter
            for (let i = 1; i < 5; i++) { // Beispiel: 5 Zellen Schiff
                if (y + i < 10) {
                    this.spielfeldSpieler[x][y + i] = "S";
                    this.zeichneSpielfeld();
                }
            }
        } else {
            // Zeichne vertikal weiter
            for (let i = 1; i < 5; i++) { // Beispiel: 5 Zellen Schiff
                if (x + i < 10) {
                    this.spielfeldSpieler[x + i][y] = "S";
                    this.zeichneSpielfeld();
                }
            }
        }
    }

    // Eine Methode zum Setzen eines Schusses auf das Spielfeld (Test)
    schussAbgeben(x, y) {
        if (this.spielfeldKI[x][y] === "S") {
            this.spielfeldKI[x][y] = "X"; // Schiff getroffen
            this.zeichneSpielfeld();
        } else if (this.spielfeldKI[x][y] === "~") {
            this.spielfeldKI[x][y] = "O"; // Verfehlt
            this.zeichneSpielfeld();
        }
    }
}

// Diese Methode ruft das Erstellen des Spielfelds aus dem Java-Backend auf
function createSchiff(id) {
    const canvas = document.getElementById('battleshipCanvas' + id);
    const schiffeVersenkenSpiel = new SchiffeVersenken(canvas, id);
    return schiffeVersenkenSpiel;
}
