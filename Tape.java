import java.util.ArrayList;
import java.util.Arrays;

enum Move{
    LEFT,
    RIGHT
}

class Tape{
    List<String> tapeList = new ArrayList<>();
    String startSymbol = "#";
    int kopf;

    public Tape(String input){
        kopf = input.length() + 1;
        String str = "#".repeat(1) + input + "#".repeat(1);
        char[] character = str.toCharArray();
    for(char c : character){
        tapeList.add(String.valueOf(c));
    }
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
        res = res + "{" + tapeList.get(i) + "}";
        }else{
        res = res + tapeList.get(i);
        }
    }

    return res.toString();
}
    
}

