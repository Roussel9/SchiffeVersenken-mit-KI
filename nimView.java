    
Zug: (0, 1), Gewinnrate: 40%, Gewinne: 4, Verluste: 6

Zug: (0, 2), Gewinnrate: 50%, Gewinne: 5, Verluste: 5

Zug: (1, 1), Gewinnrate: 30%, Gewinne: 3, Verluste: 7

Zug: (1, 2), Gewinnrate: 60%, Gewinne: 6, Verluste: 4

Zug: (1, 3), Gewinnrate: 30%, Gewinne: 3, Verluste: 7

Zug: (1, 4), Gewinnrate: 80%, Gewinne: 8, Verluste: 2

Zug: (1, 5), Gewinnrate: 40%, Gewinne: 4, Verluste: 6

Zug: (2, 1), Gewinnrate: 50%, Gewinne: 5, Verluste: 5

Zug: (2, 2), Gewinnrate: 40%, Gewinne: 4, Verluste: 6

Zug: (2, 3), Gewinnrate: 20%, Gewinne: 2, Verluste: 8

Zug: (2, 4), Gewinnrate: 50%, Gewinne: 5, Verluste: 5

Zug: (2, 5), Gewinnrate: 70%, Gewinne: 7, Verluste: 3

Zug: (2, 6), Gewinnrate: 50%, Gewinne: 5, Verluste: 5

Bester Zug: (1, 4), mit Gewinnrate: 80%
    (1,4)
    
    Clerk.markdown(
    Text.fillOut(
    """
    

    # Nim-Spiel mit LiveView

        Roussel Dongmo

    > Das [Nim-Spiel](https://de.wikipedia.org/wiki/Nim-Spiel) ist ein bekanntes Spiel ,das in der Regel zu zweit gespielt wird und , bei dem der Gewinner  derjenige ist , der nicht das letzte  Hölzchen nimmt . Das Spielt kann sehr viel attraktiv gestaltet werden . Ich habe aber nur relevante oder die wichtigsten Methoden aus dem Datei von Herr Prof _Dominikus Herzberg_ benutzt. 
        um so ein Spiel zu programmieren ,sind einige Klasse in der Java Bibliothek zu importieren 
    

    ```nimView
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.stream.IntStream;
    ```

    """,Text.cutOut("./nimView.java", "// klasssenimport")));
Die mcMove oder [Monte-Carlo](https://en.wikipedia.org/wiki/Monte_Carlo_tree_search)  methode wird in der Nim Spiel benutzt um den besten Zug herauszufinden,indem eine statische Berechnung von Gewinn und Verlust nach nach jeder N Simulationen durchgeführt wird . Anbei erkläre ich ausfühlicher , wie ich verfahren habe. Hier sind die notwendige Bibliothek
        //Klaasenimport
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Random;
    import java.util.stream.IntStream;
        //Klaasenimport



    Clerk.markdown(
    Text.fillOut(
    """ 

    ## Klasse Move
    > Eigentlich besteht das Spiel auf Reihen . Und auf jede Reihe gibt es eine bestimmmte Anzahl von Hölzchen. Deswegen ist uns wichtig eine Klasse Move zu definieren , die als Objektvariablen die Reihe (row) und die Anzahl(number) der Hölzchen auf diese Reihe. Diese Variable können später als Parameter für Methode in anderen Klasse wie Nim in der gleichen Package deklariert werden . Aber bevor wir auf Nim laden, möchte ich zuerst auf eine Schnittstelle eingehen. 
    ```nimView
     class Move {
    final int row, number;
    static Move of(int row, int number) {//nimm ein Number auf eine Zeile
        return new Move(row, number);
    }
    private Move(int row, int number) {
        if (row < 0 || number < 1) throw new IllegalArgumentException();//muss mindestens eine Zeile sein oder 
        this.row = row;
        this.number = number;
    }
    public String toString() {
        return "(" + row + ", " + number + ")";
    }
    }
    ```


    """,Text.cutOut("./nimView.java", "// klassseMove")));
    //klasseMove
    class Move {
    final int row, number;
    static Move of(int row, int number) {//nimm ein Number auf eine Zeile
        return new Move(row, number);
    }
    private Move(int row, int number) {
        if (row < 0 || number < 1) throw new IllegalArgumentException();//muss mindestens eine Zeile sein oder 
        this.row = row;
        this.number = number;
    }
    public String toString() {
        return "(" + row + ", " + number + ")";
    }
}
    //klasseMove
    Clerk.markdown(
    Text.fillOut(

    """
    ## Interface NimGame
    > NimGame ist ein Interface .Die Rolle eines Interfaces in java ist , abstrakte Methoden zu definieren , die in einer Klasse implemnentiert werden müssen. Unser Interface hier hat  abstrakte Methoden und eine implementierte Methode isWinning , die ergibt , ob ein Spieler gewonnen hat oder nicht. Um zu wissen , wie diese Methode isWinning das Gewinnt erkennt , ist notwendig zu wissen dass, wir über Gewinnstellung sprechen , wenn die Summe von Hölzchen auf allen Reihe ungerade ist . Um diese Geradzahligkeit zu prüfen, nutzen wir die Methode reduce von Stream . Durch reduce wird ein exclusiv-Oder Operation mit  und erstem Ziffer im Array gemacht und dann macht das ergebnis nochmal ein Bitweise exclusiv-Oder mit nächstem Ziffer im Array und so weiter ,bis das Resultat auf ein Ziffer gesetzt wird , der füu eine Gewinngstellung umgleich null sein soll . Andere relevante Methode bezülich der verschiedenen Stituationen vom Spiel sind in Klasse Nim.
    ```nimView
    interface NimGame {
    static boolean isWinning(int... numbers) {
        return Arrays.stream(numbers).reduce(0, (i,j) -> i ^ j) != 0;
        // klassische Variante:
        // int res = 0;
        // for(int number : numbers) res ^= number;
        // return res != 0;
    }
    boolean isGameOver();
    String toString();
    }
    ```


    """,Text.cutOut("./nimView.java", "// interface")));
        //interface
    interface NimGame {
    static boolean isWinning(int... numbers) {
        return Arrays.stream(numbers).reduce(0, (i,j) -> i ^ j) != 0;
        // klassische Variante:
        // int res = 0;
        // for(int number : numbers) res ^= number;
        // return res != 0;
    }
    Move randomMove();
    Move bestMove();
    boolean isGameOver();
    String toString();
}
        //interface
    Clerk.markdown(
    Text.fillOut(
    """
    ## Klasse Nim
    > Die Klasse Nim implement unser vorher Interface NimGame und nimmt ein Array als Parameter , das uns zwei Informationen für das Spiel liefert: zuerst die Anzahl der Reihen im Spiel durch die Länge des Arrays und dann die Anzahl der Hölzchen auf jede Reihe durch das Ziffer in jedem Indexe vom Array. Als Bedingung muss das Array nicht leer sein und nicht negative Zahlen erhalten. Noch dazu soll die Anzahl der  Reihen im Spiel maximal 5 sein und pro Reihe maximal 7 Hölzchen geben .Noch aktraktiver enthäht die Klasse Nim Verschiedene Methoden als Züge oder Aktionen , die im Spiel gemacht werden können. Die relevante , auf die ich eingehen möchte , sind: play(), isGameOver(), toString(), equals() und hashcode().
    
    - ## play()
     Diese Methode macht  ein Zug im das Nim-Spiel in einer Reihe und entfernt eine bestimmte Anzahl von Hölzchen . Um das zu machen muss zuerst das Spiel nicht fertig sein und die betroffene Reihe muss nicht grösser als gesamte Reihen sein . Genau so muss Anzahl von zu entfernenden auch kleiner oder gleich auf Anzahl in dieser Reihe sein . Sind diese Bedingungen erfüllt, dann erfolgt das Entfernen von Hölzchen auf betrofene Reihe und zum Schull wird den aktullen Stand vom Spiel zurückgegeben
    - ## isGameOver()
    Hier wird überprüft , ob das Spielt fertig ist oder nicht , indem wir sicherstellen ,dass es kein Hölzchen mehr gibt. Das wird im Code mit der Methode allMacht von Stream gemacht
    - ## toString()
    Dank dieser Methode haben wir die Darstellung von unserem  Spiel in einer gewünschten Form 
    - ## equals()
     Die überschriebene Methode equals() vergleicht eigentlich 2 Objekte von der Klasse Nim aber nach dem Spielprinzip . Dafür werden die Bedingungen für die Glechheit von 2 Objekten geprüft genau ,wie es in der Vorlesung erklärt wurde. nur bei der letzten Zeile habe ich eher die hashcode methode aufgerufen, die ich auch implementiert habe 
    - ## hashcode()

    Die hashcode ist eine Methode, die jedem Objekt ein wert als Identifierung angibt.Wenn 2 Object nämlich 2 Spielständen durch equals gleich sind , müssen diese Objekte gleiche Identifierungswert haben . es kommt manchmal vor , dass die Objekt gleiche hashcode haben aber nicht gleich sind :das nennt man Kollision . Um diese Kollision zu minimieren habe ich eine Primezahl ausgewählt , die mit einer rekusive Addition und Multiplikation multipliteiert wird. Jetzt ergibt sich eine Frage : wie können wird dieses Spiel im Browser spielen lassen . Dafür können wir eine Andere Klasse implementieren , die NimView heisst .
    

    ```nimView
    class Nim implements NimGame {
    int[] rows;
    public static Nim of(int... rows) {
        return new Nim(rows);
    }

    private Nim(int... rows) {
        assert rows.length >= 1;
        assert Arrays.stream(rows).allMatch(n -> n >= 0);
        if(rows.length > 5 || !(Arrays.stream(rows).allMatch(n -> n<=7))) {
             throw new IllegalArgumentException("Please enter  correct values");
        };
        this.rows = Arrays.copyOf(rows, rows.length);
    }
    private Nim play(Move m) {
        assert !isGameOver();
        assert m.row < rows.length && m.number <= rows[m.row];
        Nim nim = Nim.of(rows);
        nim.rows[m.row] -= m.number;
        return nim;
    }
    public boolean isGameOver() {
        return Arrays.stream(rows).allMatch(n -> n == 0);
    }
    public String toString() {
        String s = "";
        for(int n : rows) s += "\n" + "I ".repeat(n);
        return s;
    }
    @ Override
    public int hashCode() {
        int sum = 0;
        int mul = 1;
        if(this.rows.length > 5 || Arrays.stream(rows).allMatch(n -> n>7)) {
             throw new IllegalArgumentException("Please enter  correct values");
        }
        if(this.rows == null) return 0;
        int prime = 31;
        for(int value : this.rows){
            if(value == 0) continue;
            sum += value;
            mul *= (value + prime);
        } return sum * prime + mul;
    }
    @Override
    public boolean equals (Object other){
    int wert;
    if(other == null) return false;
    if(other == this) return true;
    if(this.getClass() != other.getClass()) return false;
    Nim that = (Nim)other;
    return this.hashCode() == that.hashCode();
    }
    }
    ```
    """,Text.cutOut("./nimView.java", "// nimklasse")));
        //nimklasse
        class Nim implements NimGame {
    private Random r = new Random();
    int[] rows;
    public static Nim of(int... rows) {
        return new Nim(rows);
    }

    private Nim(int... rows) {
        assert rows.length >= 1;
        assert Arrays.stream(rows).allMatch(n -> n >= 0);
         if(rows.length > 5 || !(Arrays.stream(rows).allMatch(n -> n<=7))) {
             throw new IllegalArgumentException("Please enter  correct values");
        }
        this.rows = Arrays.copyOf(rows, rows.length);
    }
    private Nim play(Move m) {
        assert !isGameOver();
        assert m.row < rows.length && m.number <= rows[m.row];
        Nim nim = Nim.of(rows);
        nim.rows[m.row] -= m.number;
        return nim;
    }
    public Nim play(Move... moves) {
        Nim nim = this;
        for(Move m : moves) nim = nim.play(m);
        return nim;
    }
    public Move randomMove() {
        assert !isGameOver();
        int row;
        do {
            row = r.nextInt(rows.length);
        } while (rows[row] == 0);
        int number = r.nextInt(rows[row]) + 1;
        return Move.of(row, number);
    }
    public Move bestMove() {
        assert !isGameOver();
        if (!NimGame.isWinning(rows)) return randomMove();
        Move m;
        do {
            m = randomMove();
        } while(NimGame.isWinning(play(m).rows));
        return m;
    }
    
    public boolean isGameOver() {
        return Arrays.stream(rows).allMatch(n -> n == 0);
    }
    public String toString() {
        String s = "";
        for(int n : rows) s += "\n" + "I ".repeat(n);
        return s;
    }
@ Override
    public int hashCode() {
        int sum = 0;
        int mul = 1;
        if(this.rows == null) return 0;
        int prime = 31;
        for(int value : this.rows){
            if(value == 0) continue;
            sum += value;
            mul *= (value + prime);
        } return sum * prime + mul;
    }
    @Override
    public boolean equals (Object other){
    int wert;
    if(other == null) return false;
    if(other == this) return true;
    if(this.getClass() != other.getClass()) return false;
    Nim that = (Nim)other;
    return this.hashCode() == that.hashCode();
}
    public Nim makeMove(Move m) {
        return play(m);  // Aufruf der privaten Methode
    }
}
        //nimklasse
    Clerk.markdown(
    Text.fillOut(
    """
    ## Klasse NimView
    > Die Klasse NimView sorgt durch verschiede Methoden für Die Darstellung des aktullen Spielstands auf der Browser Dank Hilfe von LiveViewProgramming . Sie nimmt deshalb als Attribut ein Parameter von Typ Turtle ;  Koordinate x,y und yInitial , das unveränderdt bleibt , um das Spiel nach dem Aktualisierung auf die  gleiche Position anzuzeigen; und ein von Typ Nim , um Zugriff auf Elemente in Klasse Nim zu haben. NimView enthält  4 Methoden : nämlich toString() , show() , play() und isGameOver. Wie am Anfang von Dokumentation gesagt wird , können noch beliebige Methode hinzügefüft werden , um das Spiel attraktiver zu gestalten; ich habe aber nur die relevante benutzt.
    
    - ## play()
    Diese Methode ruft nimmt ein Parameter von Typ Move und rutf mit einem Objekt von der Klasse NimView die Methode play(Move) von der Klasse Nim. So kann man ein Zug führen und beoachten ,dass die Spielstand aktualisiert wurde . In dieser Methode wird nach dem Aufruf zuerst mit tutrle.reset() das lestzte Spielstand gelöscht und das Turtle auf die Initial Position gelegt , bevor wir die Methode show() zur Visualisierung von neuen Stand aufrufen 
    - ## isGameOver()
    Hier wird die Methode isGameOver von Nim mit einem Objekt von NimView aufgeruft , damit wir eine Rückgabe bezüglich dem Spielstand im Browser haben
    - ## toString()
    Diese Methode wie oben gesagt ist nützich , um das Spiel zu repräsentieren . Was zusätzlich hier ist aber den Aufruf von Methode show()
    - ## show()
    Die Methode show() ist die prinzipale Methode von NimView. zuerst wird mithilfe von einem Variable des Typs Turtle einen senkrechten Strich mit Länge 50 dargestellt, der das Hölzchen repräsentiert. um Hölzchen auf jeweilige Reihe zu Zeichnen , benötigen eine for-Schleife, die unser Array rows durchläuft .Wir wissen schon ,dass rows[] Reihe und Anzahl von Hölzchen auf jede Reihen repräsentiert . Deshalb brauchen wir nach Zuweisung von x auf 100 eine zweite for-Schleife, die nicht grösser als Ziffer in jeder Indixe des Arrays sein sollte. Diese zweite for Scheile zeichnen die benötigten Hölzchen auf die jeweilige Reihe ,so dass am Ende der Schleife , gehen wir auf die neue Zeile , und wir fügen 60 auf unser y und bewegen unser Turtle Objekt auf die neue Postion von x und y , bevor wir mit der ersten Scheile weitermachen .  
    ```nimView
    class NimView  {
        Nim ni;
        Turtle turtle;
        int x ;
        int y ;
        final int yInitial = 100;
    public NimView(Turtle turtle, Nim ni){
        this.ni = ni;
        this.turtle = turtle;
        this.y = yInitial;
    }
        public NimView play(Move move) {
        if (!ni.isGameOver()) { 
            ni = ni.play(move);  
            turtle.reset();
            turtle.moveTo(x,y);
            show();              
        }return this;
    }
    public boolean isGameOver() {
        return ni.isGameOver();
    }
        public String toString() {
            show();
        String s = "";
        for(int n : ni.rows) s += "\n" + "I".repeat(n) ;
        return s;
    }
    private void show(){
        y = yInitial;
        String str = "";
        for(int i = 0; i < ni.rows.length; i++){
            x = 100;
            for(int j = 0; j < ni.rows[i]; j++){
                turtle.moveTo(x,y);
                turtle.left(90);
                turtle.lineWidth(10);
                turtle.forward(50);
                x = x + 50;
                turtle.right(90).moveTo(x, y);
            } str = "\n";
            y = y + 60;
            turtle.moveTo(100,y);
        }
    }
    }
    ```

    """,Text.cutOut("./nimView.java", "// nimViewklasse")));
     //nimViewklasse
    class NimView  {
        Nim ni;
        Turtle turtle;
        int x ;
        int y ;
        final int yInitial = 100;
    public NimView(Turtle turtle, Nim ni){
        this.ni = ni;
        this.turtle = turtle;
        this.y = yInitial;
    }
       public NimView play(Move move) {
        if (!ni.isGameOver()) { 
            ni = ni.play(move);  
            turtle.reset();
            turtle.moveTo(x,y);
            show();              
        }return this;
    }
    public boolean isGameOver() {
        return ni.isGameOver();
    }
        public String toString() {
            show();
        String s = "";
        for(int n : ni.rows) s += "\n" + "I".repeat(n) ;
        return s;
    }
    private void show(){
        y = yInitial;
        String str = "";
        for(int i = 0; i < ni.rows.length; i++){
            x = 100;
            for(int j = 0; j < ni.rows[i]; j++){
                turtle.moveTo(x,y);
                turtle.left(90);
                turtle.lineWidth(10);
                turtle.forward(50);
                x = x + 50;
                turtle.right(90).moveTo(x, y);
            } str = "\n";
            y = y + 60;
            turtle.moveTo(100,y);
        }
    }
    }
     //nimViewklasse

     Clerk.markdown(
    Text.fillOut(
    """ 
    ## Versuch zum Spielen :
    Zuerst erstelle ich ein Objekt von Typ Turtle und dann ein Objekt von Typ NimView , so dass wir 3 Reihen bekommen. Mit 2 Hölzchen auf erste Linie, 5 auf die zweite und 6 auf die letzte
    ```nimView
    Turtle t = new Turtle();
    NimView n = new NimView(t,Nim.of(2,5,6));
    ```
    """,Text.cutOut("./nimView.java", "// spielstand1")));
    //spielstand1
    Turtle t = new Turtle();
    NimView n = new NimView(t,Nim.of(2,5,6));
    //spielstand1
    Clerk.markdown(
    Text.fillOut(
    """
    Jetzt führe ich einen Zug durch Methode play() , um 2 Hölzchen auf die 2. Reihe zu entfernen. Wir bekommen dann Folgendes
    ```nimView
    Turtle t = new Turtle();
    NimView n = new NimView(t,Nim.of(2,5,6));
    n.play(Move.of(1,2));

    ```
    """,Text.cutOut("./nimView.java", "// play1")));
    //play1
    Turtle t = new Turtle();
    NimView n = new NimView(t,Nim.of(2,5,6));
     n.play(Move.of(1,2));
    //play1

    
























  






















