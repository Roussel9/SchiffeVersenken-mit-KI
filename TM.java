import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

Clerk.markdown(
    Text.fillOut(
"""
# Turing-Maschine
## Dekrementierung einer Binärzahl
1. Tabelle mit Transitionen

fromState | read | write | move | toState
----------|------|-------|------|---------
S |# |# |L |S
S |1 |0 |R |R
S |0 |1 |L |L
R |0 |0 |R |R
R |1 |1 |R |R
R |# |# |L |W
W |1 |1 |R |HALT
W |0 |0 |R |HALT
W |# |# |R |HALT
L |0 |1 |L |L
L |1 |0 |R |R
L |# |# |R |R

2. Bemerkung:
- Das Band ist wie folgt initialisiert: # 1 1 0 0 0 {#}
- Vorbelegungszeichen: #
- Start-Zustand: S
3. Durchlauf:

> 0: # 1 1 0 0 0{#} -- S <br>
1: # 1 1 0 0{0}#  -- S <br>
2: # 1 1 0{0}1 #  -- L <br>
3: # 1 1{0}1 1 #  -- L <br>
4: # 1{1}1 1 1 #  -- L <br>
5: # 1 0{1}1 1 #  -- R <br>
6: # 1 0 1{1}1 #  -- R <br>
7: # 1 0 1 1{1}#  -- R <br>
8: # 1 0 1 1 1{#} -- R <br>
9: # 1 0 1 1{1}#  -- W <br>
10: # 1 0 1 1 1{#} -- HALT <br>

4. Erläuterung:
- Erster Schritt:
Die Maschine befindet sich im Zustand S und der Lesekopf steht auf dem letzten Zeichen des Bandes.
Sie liest das Zeichen # (die Bedingung ist also (S, #) und löst normalerweise die Aktion (#, L, S) aus); sie schreibt jedoch #, bewegt sich um eine Position nach links und bleibt im Zustand S, wie erwartet.

- Zweiter Schritt:
Die Maschine ist weiterhin im Zustand S, und der Kopf steht jetzt auf der vorletzten Position (5. Position).
Sie liest das Zeichen 0 (die Bedingung (S, 0) würde normalerweise die Aktion (1, L, L) auslösen); sie schreibt jedoch 1, bewegt sich nach links und wechselt in den Zustand L, wie erwartet.

-Dritter Schritt:
Die Maschine ist nun im Zustand L und der Kopf befindet sich auf der 4. Position.
Sie liest erneut das Zeichen 0 (die Bedingung (L, 0) würde normalerweise die Aktion (1, L, L) auslösen); sie schreibt jedoch 1, bewegt sich eine Position nach links und bleibt im Zustand L, wie erwartet.
## Einsen nach rechts Schieben
1. Tabelle mit Transitionen

fromState | read | write | move | toState
----------|------|-------|------|---------
S |1 |1 |L |S
S |S |S |R |HALT
S |0 |0 |L |0
0 |0 |0 |L |0
0 |1 |0 |R |1
0 |S |S |R |HALT
1 |0 |0 |R |1
1 |1 |1 |L |D
1 |S |S |L |D
D |0 |1 |L |S

2. Bemerkung:
- Das Band ist wie folgt initialisiert:  S 0 1 0 1 0 {1}
- Vorbelegungszeichen: S
- Start-Zustand: S

3. Durchlauf:

> 0: S 0 1 0 1 0{1} -- S <br>
1: S 0 1 0 1{0}1  -- S <br>
2: S 0 1 0{1}0 1  -- 0 <br>
3: S 0 1 0 0{0}1  -- 1 <br>
4: S 0 1 0 0 0{1} -- 1 <br>
5: S 0 1 0 0{0}1  -- D <br>
6: S 0 1 0{0}1 1  -- S <br>
7: S 0 1{0}0 1 1  -- 0 <br>
8: S 0{1}0 0 1 1  -- 0 <br>
9: S 0 0{0}0 1 1  -- 1 <br>
10: S 0 0 0{0}1 1 -- 1 <br>
11: S 0 0 0 0{1}1  -- 1 <br>
12: S 0 0 0{0}1 1  -- D <br>
13: S 0 0{0}1 1 1  -- S <br>
14: S 0{0}0 1 1 1  -- 0 <br>
15: S{0}0 0 1 1 1  -- 0 <br>
16: {S}0 0 0 1 1 1  -- 0 <br>
17: S{0}0 0 1 1 1  -- HALT <br>



## Live-View-Prozess von der Turing-Maschine
    
""", Text.cutOut("./TM.java", "// code")));

enum Move {
    LEFT,
    RIGHT
    }

public class TM {
    Tape tape;
    boolean isFertig = false;
    String endState = "HALT";
    TM(Tape tape) {
        this.tape = tape;
    }
    
    Turtle turtle = new Turtle(1000,400);
    TM step() {
        if(tape.state != endState) {
            tape.read();
            tape.write();
            tape.move();
        } else {
            System.out.println("Sie haben den Haltezustand erreicht!");
            isFertig = true;
        }
        return this;
    }

    TM run() {
        while(isFertig == false) {
            this.step();
        }
        return this;
    }

@Override
    public String toString () {
        int x = 250;
        int y = 250;
        turtle.reset();
        turtle.moveTo(x, y);
        String s = "";

        if(this.tape.i < 5 ) {
            for(int j = 0; j < (5 - this.tape.i); j++){
                turtle.moveTo(x, y).left(90).penUp().forward(20).penDown().left(90).forward(15).left(90).forward(40).left(90).forward(30).left(90).forward(40).left(90).forward(15).left(180);
                x = x + 40;
            } 
            for(int j = 0; j <= this.tape.i; j++) {
                if(j == this.tape.i) {
                    s = s + this.tape.band.get(tape.i);
                    turtle.moveTo(x, y).lineWidth(7).left(90).text(s).penUp().forward(20).penDown().left(90).forward(15).left(90).forward(40).left(90).forward(30).left(90).forward(40).left(90).forward(15).left(180).lineWidth(1);
                    s = "";
                    x = x + 40;
                } else {
                    s = this.tape.band.get(j) + s;
                    turtle.moveTo(x, y).left(90).text(s).penUp().forward(20).penDown().left(90).forward(15).left(90).forward(40).left(90).forward(30).left(90).forward(40).left(90).forward(15).left(180);
                    s = "";
                    x = x + 40;
                }
            }
        } else 
        if(this.tape.i >= 5){
            for(int j = this.tape.i - 5; j <= this.tape.i; j++) {
                if(j == this.tape.i) {
                    s = s + this.tape.band.get(tape.i);
                    turtle.moveTo(x, y).lineWidth(7).left(90).text(s).penUp().forward(20).penDown().left(90).forward(15).left(90).forward(40).left(90).forward(30).left(90).forward(40).left(90).forward(15).left(180).lineWidth(1);
                    s = "";
                    x = x + 40;
                } else {
                    s = this.tape.band.get(j) + s;
                    turtle.moveTo(x, y).left(90).text(s).penUp().forward(20).penDown().left(90).forward(15).left(90).forward(40).left(90).forward(30).left(90).forward(40).left(90).forward(15).left(180);
                    s = "";
                    x = x + 40;
                }
            }
        }

        if((this.tape.band.size() - this.tape.i) < 6) {  
            for(int j = (this.tape.i + 1); j < this.tape.band.size(); j++) {
                s = this.tape.band.get(j) + s;
                turtle.moveTo(x, y).left(90).text(s).penUp().forward(20).penDown().left(90).forward(15).left(90).forward(40).left(90).forward(30).left(90).forward(40).left(90).forward(15).left(180);
                s = "";
                x = x + 40;
            }
            for(int j = this.tape.band.size(); j < (this.tape.band.size() + (5 - (this.tape.band.size() - this.tape.i) + 1)); j++) {
                turtle.moveTo(x, y).left(90).penUp().forward(20).penDown().left(90).forward(15).left(90).forward(40).left(90).forward(30).left(90).forward(40).left(90).forward(15).left(180);
                x = x + 40;
            }
        } else 
        if((this.tape.band.size() - this.tape.i) >= 6 ) {
            for(int j = (this.tape.i + 1); j < this.tape.i + 5; j++) {
                s = this.tape.band.get(j) + s;
                turtle.moveTo(x, y).left(90).text(s).penUp().forward(20).penDown().left(90).forward(15).left(90).forward(40).left(90).forward(30).left(90).forward(40).left(90).forward(15).left(180);
                s = "";
                x = x + 40;
            }
        }
        return s;
    }
     
}

class Tape {
    Table table;
    List<Character> band = new ArrayList<>();
    int i;
    String state, next;
    char c;
    Tape(List<Character> band, Table table, String state) {
        assert band.size() > 1;
        this.band = band;
        this.table = table;
        this.state = state;
        this.i = band.size() - 1;
        
    }

    Character read() {
        c = band.get(i);
        return c;
    }

    Tape write() {
        this.table.transitionen.forEach((Trigger t, Action a) -> {
            if(t.fromState == state && this.c == t.read) {
                this.band.add(i, a.write);
                this.band.remove(i + 1);
            }
        });
        return this;
    }

    Tape move() {
        this.table.transitionen.forEach((Trigger trigger, Action action) -> {
            if(trigger.fromState == state && this.c == trigger.read) {

                 if(table.transitionen.get(trigger).move == Move.LEFT) {
                    this.i = i - 1;
                } else {
                    this.i = i + 1;
                }
                next = action.toState;
            } 
        });
        state = next;
        return this;
    }
}

class Table {
    Map<Trigger, Action> transitionen = new HashMap<Trigger,Action>();

    Map<Trigger,Action> aufbau(String fromState, char read, char write, Move move, String toState) {
                Trigger trigger = new Trigger(fromState, read);
                Action action = new Action(write, move, toState);
                transitionen.put(trigger, action); 
                return transitionen; 
        }

    Action aufruf(Trigger trigger) {
        return transitionen.get(trigger);
    }
}

class Trigger {
    String fromState;
    char read;

    Trigger(String fromState, char read) {
        this.fromState = fromState;
        this.read = read;
    }
}

class Action {
    char write;
    Move move;
    String toState;

    Action(char write, Move move, String toState) {
        this.write = write;
        this.move = move;
        this.toState = toState;
    }
}


        
