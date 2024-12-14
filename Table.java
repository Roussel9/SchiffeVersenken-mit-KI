import java.util.ArrayList;
import java.util.Arrays;


class TM{
    Tape tape;
    Table table ;
    String state;

    public TM(Tape tape , Table table, String state){
        this.tape = tape;
        this.table = table;
        this.state = state;
    }

    void step(String instruction){
        if(instruction == "DEKREMENTIERUNG"){
            table.trigger = new Trigger(state, (tape.read().charAt(0)));
            if(table.trigger.fromState == "S"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "#":
                        interMethod("S","#","#",Move.LEFT,"S");
                    break;
                    case "1":
                        interMethod("S","1","0",Move.RIGHT,"R");
                    break;
                    case "0":
                        interMethod("S","0","1",Move.LEFT,"L");
                    break;
                    default:
                       System.out.println("invalid input");

                }
            }

            if(table.trigger.fromState == "R"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "0":
                        interMethod("R","0","0",Move.RIGHT,"R");
                    break;
                    case "1":
                        interMethod("R","1","1",Move.RIGHT,"R");
                    break;
                    case "#":
                        interMethod("R","#","#",Move.LEFT,"W");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }

            if(table.trigger.fromState == "W"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "1":
                        interMethod("W","1","1",Move.RIGHT,"HALT");
                    break;
                    case "0":
                        interMethod("W","0","0",Move.RIGHT,"HALT");
                    break;
                    case "#":
                        interMethod("W","#","#",Move.RIGHT,"HALT");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }

             if(table.trigger.fromState == "L"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "0":
                        interMethod("L","0","1",Move.LEFT,"L");
                    break;
                    case "1":
                        interMethod("L","1","0",Move.RIGHT,"R");
                    break;
                    case "#":
                        interMethod("L","#","#",Move.RIGHT,"R");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }
        }
        if(instruction == "EinsenNachRecht"){
            table.trigger = new Trigger(state, (tape.read().charAt(0)));
            if(table.trigger.fromState == "S"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "1":
                        interMethod("S","1","1",Move.LEFT,"S");
                    break;
                    case "S":
                        interMethod("S","S","S",Move.RIGHT,"HALT");
                    break;
                    case "0":
                        interMethod("S","0","0",Move.LEFT,"0");
                    break;
                    default:
                       System.out.println("invalid input");

                }
            }

            if(table.trigger.fromState == "0"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "0":
                        interMethod("R","0","0",Move.RIGHT,"R");
                    break;
                    case "1":
                        interMethod("R","1","1",Move.RIGHT,"R");
                    break;
                    case "#":
                        interMethod("R","#","#",Move.LEFT,"W");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }

            if(table.trigger.fromState == "W"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "1":
                        interMethod("W","1","1",Move.RIGHT,"HALT");
                    break;
                    case "0":
                        interMethod("W","0","0",Move.RIGHT,"HALT");
                    break;
                    case "#":
                        interMethod("W","#","#",Move.RIGHT,"HALT");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }

             if(table.trigger.fromState == "L"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "0":
                        interMethod("L","0","1",Move.LEFT,"L");
                    break;
                    case "1":
                        interMethod("L","1","0",Move.RIGHT,"R");
                    break;
                    case "#":
                        interMethod("L","#","#",Move.RIGHT,"R");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }
        }
    }

    void run (String instruction){
        int count = 0;
        table.trigger = new Trigger(state, (tape.read().charAt(0)));
        table.action = table.actionMethod(table.trigger);
         System.out.println(" "+count + ": " + this);
        do{
            step(instruction);
            count++;
            if(count>9){
                System.out.println(count + ": " + this);
            }else {
                System.out.println(" "+count + ": " + this);
            }
        } while(table.action.toState != "HALT");
    }

    void interMethod(String fromStates, String read,String writeValue,Move mo,String toStates ){
        table.trigger = new Trigger(fromStates, read.charAt(0));
                    tape.write(writeValue);
                    char c = tape.read().charAt(0);
                    tape.move(mo);
                    state = toStates;
                    table.action = new Action(c,Move.LEFT,state);
                    table.bau(fromStates, read.charAt(0), c, mo, state);
    }

    @Override
public String toString(){
    String st= "";
    st += tape.toString() + "--" + state;
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
    List<String> tapeList = new ArrayList<>();
    int kopf;

    public Tape(String input, char vorZeichnen){
        assert vorZeichnen == '#' || vorZeichnen == 'S';
         String str =("" + vorZeichnen).repeat(1) + input + ( "" + vorZeichnen).repeat(1);
        char[] character = str.toCharArray();
    for(char c : character){
        tapeList.add(String.valueOf(c));
    }
        kopf = vorZeichnen == '#'? input.length() +1: input.length();
       
    }

String read(){
    return tapeList.get(kopf);
}

boolean write(String value){
    tapeList.set(kopf,value);
    return true;
}

void move(Move direction){
    if(direction == Move.LEFT) kopf--;
    if(direction == Move.RIGHT) kopf++;
}
@Override 
public String toString(){
    String res = "";
    for(int i = 0; i<tapeList.size(); i++){
        if(i == kopf){
        res = res +  "{" + tapeList.get(i) + "}";
        }else{
        res = res + " " + tapeList.get(i) + " ";
        }
    }

    return res.toString();
}
    
}

void step(String instruction){
        if(instruction == "DEKREMENTIERUNG"){
            table.trigger = new Trigger(state, (tape.read().charAt(0)));
            if(table.trigger.fromState == "S"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "#":
                        interMethod("S","#","#",Move.LEFT,"S");
                    break;
                    case "1":
                        interMethod("S","1","0",Move.RIGHT,"R");
                    break;
                    case "0":
                        interMethod("S","0","1",Move.LEFT,"L");
                    break;
                    default:
                       System.out.println("invalid input");

                }
            }

            if(table.trigger.fromState == "R"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "0":
                        interMethod("R","0","0",Move.RIGHT,"R");
                    break;
                    case "1":
                        interMethod("R","1","1",Move.RIGHT,"R");
                    break;
                    case "#":
                        interMethod("R","#","#",Move.LEFT,"W");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }

            if(table.trigger.fromState == "W"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "1":
                        interMethod("W","1","1",Move.RIGHT,"HALT");
                    break;
                    case "0":
                        interMethod("W","0","0",Move.RIGHT,"HALT");
                    break;
                    case "#":
                        interMethod("W","#","#",Move.RIGHT,"HALT");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }

             if(table.trigger.fromState == "L"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "0":
                        interMethod("L","0","1",Move.LEFT,"L");
                    break;
                    case "1":
                        interMethod("L","1","0",Move.RIGHT,"R");
                    break;
                    case "#":
                        interMethod("L","#","#",Move.RIGHT,"R");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }
        }if(instruction == "EinsenNachRecht"){
            table.trigger = new Trigger(state, (tape.read().charAt(0)));
            if(table.trigger.fromState == "S"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "1":
                        interMethod("S","1","1",Move.LEFT,"S");
                    break;
                    case "S":
                        interMethod("S","S","S",Move.RIGHT,"HALT");
                    break;
                    case "0":
                        interMethod("S","0","0",Move.LEFT,"0");
                    break;
                    default:
                       System.out.println("invalid input");

                }
            }

            if(table.trigger.fromState == "0"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "0":
                        interMethod("0","0","0",Move.LEFT,"0");
                    break;
                    case "1":
                        interMethod("0","1","0",Move.RIGHT,"1");
                    break;
                    case "S":
                        interMethod("0","S","S",Move.RIGHT,"HALT");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }

            if(table.trigger.fromState == "1"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "0":
                        interMethod("1","0","0",Move.RIGHT,"1");
                    break;
                    case "1":
                        interMethod("1","1","1",Move.LEFT,"D");
                    break;
                    case "S":
                        interMethod("S","S","S",Move.LEFT,"D");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }

             if(table.trigger.fromState == "D"){
                String read = "" + table.trigger.read;
                read = tape.read();
                switch(read){
                    case "0":
                        interMethod("D","0","1",Move.LEFT,"S");
                    break;
                    default:
                        System.out.println("invalid input");

                }
            }
        }
    }*/

/*
Table t = new Table();
        t.aufbau("S",'#','#',Move.LEFT,"S");
        t.aufbau("S",'1','0',Move.RIGHT,"R");
        t.aufbau("S",'0','1',Move.LEFT,"L");
        t.aufbau("R",'0','0',Move.RIGHT,"R");
        t.aufbau("R",'1','1',Move.RIGHT,"R");
        t.aufbau("R",'#','#',Move.LEFT,"W");
        t.aufbau("W",'1','1',Move.RIGHT,"HALT");
        t.aufbau("W",'0','0',Move.RIGHT,"HALT");
        t.aufbau("W",'#','#',Move.RIGHT,"HALT");
        t.aufbau("L",'0','1',Move.LEFT,"L");
        t.aufbau("L",'1','0',Move.RIGHT,"R");
        t.aufbau("L",'#','#',Move.RIGHT,"R");

        Tape tape = new Tape("11000", '#', t, "S");

        TM tm = new TM(tape);