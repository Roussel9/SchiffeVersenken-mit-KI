Clerk.markdown(
    Text.fillOut(
"""
# Umzusetzende Beispiele
## Dekrementierung einer Binärzahl
Gegeben sei das nachstehende Programm für die Turing-Maschine.

fromState|read|write|move|toState
---------|----|-----|----|-------
S|#|#|L|S
S|1|0|R|R
S|0|1|L|L
R|0|0|R|R
R|1|1|R|R
R|#|#|L|W
W|1|1|R|HALT
W|0|0|R|HALT
W|#|#|R|HALT
L|0|1|L|L
L|1|0|R|R
L|#|#|R|R


## Einsen nach rechts schieben
Gegeben sei das nachstehende Programm für die Turing-Maschine

fromState|read|write|move|toState
---------|----|-----|----|-------
S|1|1|L|S
S|S|S|R|HALT
S|0|0|L|0
0|0|0|L|0
0|1|0|R|1
0|S|S|R|HALT
1|0|0|R|HALT
1|1|1|L|D
1|S|S|L|D
D|0|1|L|S
""", Text.cutOut("./TM.java", "// code")));



import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;


class TM{
    Tape tape;
   // Table table ;
   // String state;

    public TM(Tape tape){
        this.tape = tape;
      //  this.table = table;
       // this.state = state;
    }

   
   TM step(){
    if(!(tape.state.equals("HALT"))){
        tape.read();
        tape.write();
        tape.move();
    }else{
        System.out.println("Sie haben Haltezustand erreicht");
    }
    return this;
   }

     TM run (){
        int count = 0;
       // table.trigger = new Trigger(state, (tape.read().charAt(0)));
      // table.action = table.actionMethod(table.trigger);
         System.out.println(" "+count + ": " + this);
        do{
            step();
            count++;
            if(count>9){
                System.out.println(count + ": " + this);
            }else {
                System.out.println(" "+count + ": " + this);
            }
        } while(!(tape.state.equals("HALT")));
        return this;
    }

   /* void interMethod(String fromStates, String read,String writeValue,Move mo,String toStates ){
        table.trigger = new Trigger(fromStates, read.charAt(0));
                    tape.write(writeValue);
                    char c = tape.read().charAt(0);
                    tape.move(mo);
                    state = toStates;
                    table.action = new Action(c,Move.LEFT,state);
                    table.bau(fromStates, read.charAt(0), c, mo, state);
    }*/
    Turtle t = new Turtle();
     int x = 150;
    int y = 100;
    @Override
public String toString(){
    String st= "";
    st += tape.toString() + "--" + state;
    t.textSize = 20;
    t.textFont = Font.COURIER;
    t.moveTo(x,y).left(90).text(st).right(90);
    y = y + 20;
    t.moveTo(x,y);
    return st;
}
}





class Table{
    Map <Trigger, Action> map ;
   public  Trigger trigger;
    public  Action action;
    public Table(){
         map = new HashMap<>();
    }

    Map<Trigger,Action> bau (String fromState, char read,char write,Move move,String toState){
        trigger = new Trigger(fromState,read);
        action = new Action(write,move,toState);
        map.put(trigger,action);
        return map;
    }

    Action actionMethod(Trigger t){
        return map.get(t);
    }
    
}

class Trigger{
    String fromState;
    char read;
    public Trigger(String fromState, char read){
        this.fromState = fromState;
        this.read = read;
    }
}

class Action{
    char write;
    Move move;
    String toState;
    public Action(char write, Move move, String toState){
        this.write = write;
        this.move = move;
        this.toState = toState;
    }
}



enum Move{
    LEFT,
    RIGHT
}

class Tape{
    List<Character> tapeList = new ArrayList<>();
    int kopf;
    char c;
    String vor,nach;
    Table table;


    public Tape(String input, Table table , String vor,char vorZeichnen){
        assert vorZeichnen == '#' || vorZeichnen == 'S';
       if(input.equals("")) {
            throw new IllegalArgumentException("Geben Sie etwas ein!");
        }
         String str =("" + vorZeichnen).repeat(1) + input + ( "" + vorZeichnen).repeat(1);
        char[] character = str.toCharArray();
    for(char c : character){
        tapeList.add(c);
    }
        kopf = vorZeichnen == '#'? input.length() +1: input.length();
       this.table = table;
       this.vor = vor;
    }

char read(){
    return tapeList.get(kopf);
}

Tape write(){
    this.table.map.forEach((Trigger trigger, Action action) -> {
            if(trigger.fromState == vor && this.c == trigger.read) {
                this.tapeList.add(kopf, action.write);
                this.tapeList.remove(kopf + 1);
            }
        });
  //  tapeList.set(kopf,value);
    return this;
}

Tape move(){
    this.table.map.forEach((Trigger trigger , Action action)-> {
        if(trigger.fromState == vor && this.c == trigger.read){
        if((table.actionMethod(trigger)).move == Move.LEFT){
            kopf--;
        }else{
            kopf++;
        }
        }
    });
    nach = table.action.toStates;
    return this;
}
@Override 
public String toString(){
    Turtle t = new Turtle(550,50);
    String res = "";
    for(int i = 0; i<tapeList.size(); i++){
        if(i == kopf){
        res = res +  "{" + tapeList.get(i) + "}";
        }else{
        res = res + " " + tapeList.get(i) + " ";
        }
    }
     drawHeadCenteredTape(t);

    return res.toString();
}

void drawHeadCenteredTape(Turtle t) {
    t.moveTo(0,50);
    for(int i = 0; i < 11; i++) { 

        
        if(i==5){
        
        t.lineWidth(10).forward(50).left(90).forward(50).left(90).forward(50).left(90).forward(50).left(90).lineWidth(1); 
        }else{ for(int j = 0; j < 4; j++) {

        t.lineWidth(0).forward(50).left(90); 
        }}
        if(i< tapeList.size()-1){
        t.forward(25).left(90); 
        t.textSize = 50;
        t.textFont = Font.COURIER;

        t.right(90).forward(25); 
        }else{
             t.lineWidth(1).forward(25).left(90);
              t.right(90).forward(25);  
        }
    }
}
}


Map<Trigger,Action> aufbaus(List<Trigger> triggers, List<Action> actions) {
        assert triggers.size() == actions.size();
        for(Trigger t: triggers) {
            for(Action a: actions) {
                transitionen.put(t, a);  
            }
        }
        return transitionen;
    }

/*
# Turing-Maschine
                
    Hier wird der Ablauf einer Turing-Maschine anhand 02 Beispielen veranschaulicht

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
die Maschine ist im Zustand S;der S/L Kopf ist an der letzen.
Er liest das Zeichen `#`,(das heißt trigger ist (S,#) und führt normalerweise zur Action(#, L, S));er schreibt doch # und geht links.
Die Machine bleibt im Zustand S wie erwatet.

    - Zweiter Schritt:
die Maschine ist im Zustand S;der Kopf ist an der vorletzen(diesmal der 5-ten Position).
Er liest das Zeichen `0`,(das heißt trigger ist (S,0) und führt normalerweise zur Action(1, L, L));er schreibt doch 1 und geht links;
Die Machine geht dann zum Zustand L wie erwatet.

    - Dritter Schritt:
die Maschine ist im Zustand L;der S/L Kopf ist an der 4-ten Position.
Er liest das Zeichen `0`,(das heißt trigger ist (S,0) und führt normalerweise zur Action(1, L, L));er schreibt doch 1 und geht links;
Die Machine bleibt dann im Zustand L wie erwatet.


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
