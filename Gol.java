class Gol{
   public int heigth;
   public int withd;
    int [] world;
    Gol(int withd, int heigth){
        assert(withd > 0|| heigth > 0);
        this.heigth = heigth;
        this.withd = withd ;
         this.world = new int[this.withd * this.heigth];
    }

int rule(int center){
        int counterB = 0;
        for(int i = 0; i < world.length;i++){
            if(world[i] == 1 && i!=center){
                counterB++;
            }
        }if(counterB == 3 && world[center] == 0){
            world[center] = 1;
        } if((world[center] == 1 && counterB == 1) ||(world[center] == 1 && counterB == 0)){
            world[center] = 0;
        } if((world[center] == 1 && counterB == 2) ||(world[center] == 1 && counterB == 3)){
            world[center] = 1;
        }if(world[center] == 1 && counterB > 3){
            world[center] = 0;
        }
        return world[center];
    }

    Gol set(int row, int col){
        setLive(row,col);
        if(row > heigth || row < 1 || col > withd|| col < 1){
                    throw new IllegalArgumentException ("Index out of board");
                }
        int position=0;
        for(int i = 1; i<= heigth; i++){
            for(int j = 1; j<= withd; j++){
                position++;
                if(i == row && j == col){
                    world[position - 1] = 1;
                   
                }
            }
        } return this;
    }

    Gol timestep(){
        for(int i = 0; i< this.world.length; i++){
            rule(i);
        }
        return this;
    }

   

    Gol rotate(){
        String s = "";
        int pos=0;
        int newHeigth = heigth ;
        int[] newWorld = new int[this.world.length];
        for(int row = 1; row <= this.heigth; row++){
            newHeigth = newHeigth - 1;
            for(int col = 1; col<= this.withd; col++ ){
                newWorld[pos] = this.world[newHeigth];
                 newHeigth= newHeigth + heigth;
                pos++;
            }
            heigth = heigth -1;
            newHeigth = heigth;
        }
        this.world = newWorld;
         return this;
    }
    

    Gol insert(int row, int col, Gol source) {
    int[] newW = new int[this.world.length + (source.withd * source.heigth)];
    if (row > heigth || row < 1 || col > withd || col < 1) {
        throw new IllegalArgumentException("Index out of board");
    }

    int position = 0;  
    int loc = 0;       

    for (int i = 1; i <= this.heigth; i++) {
        for (int j = 1; j <= this.withd; j++) {
            if (i == row && j == col) {
                for (int k = 1; k <= source.heigth; k++) {
                    for (int t = 1; t <= source.withd; t++) {
                        newW[position] = source.world[loc];
                        position++;
                        loc++;
                    }
                }
            }
            if (position < newW.length) {
                newW[position] = this.world[(i - 1) * this.withd + (j - 1)];
                position++;
            }
        }
    }
    this.world = newW;
    return this;
}


    Turtle t = new Turtle();

    @Override 
public String toString() {

    live(t);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < world.length; i++) {
        sb.append(world[i]); 
        if ((i + 1) % withd == 0) { 
            sb.append("\n");
        } else {
            sb.append(" "); 
        }
    }
    return sb.toString();
}

void live(Turtle t){
    int x = 100;
    int y = 100;
    t.moveTo(x,y);
     for (int i = 0; i < world.length; i++) {
        for(int j=0; j<4;j++){
                t.forward(20);
                t.right(90);
        }t.forward(20);
        if ((i + 1) % withd == 0) { 
            y = y +20;
            t.moveTo(x,y);
        } 
    }
}


int ruleLive(int center){
        int counterB = 0;
        for(int i = 0; i < world.length;i++){
            if(world[i] == 1 && i!=center){
                counterB++;
            }
        }if(counterB == 3 && world[center] == 0){
            world[center] = 1;
        } if((world[center] == 1 && counterB == 1) ||(world[center] == 1 && counterB == 0)){
            world[center] = 0;
        } if((world[center] == 1 && counterB == 2) ||(world[center] == 1 && counterB == 3)){
            world[center] = 1;
        }if(world[center] == 1 && counterB > 3){
            world[center] = 0;
        }
        return world[center];
    }

    void setLive(int row, int col){
        if(row > heigth || row < 1 || col > withd|| col < 1){
                    throw new IllegalArgumentException ("Index out of board");
                }
        int position=0;
        for(int i = 1; i<= heigth; i++){
            for(int j = 1; j<= withd; j++){
                position++;
                if(i == row && j == col){
                    t.moveTo(row,col).text("Hi");
                   
                }//t.color(0,0,0);
            }
        } //return this;
    }
    
}

/*class live {
    Gol gol;
    Turtle t = new Turtle();

    int rule(int center){
        int counterB = 0;
        for(int i = 0; i < world.length;i++){
            if(world[i] == 1 && i!=center){
                counterB++;
            }
        }if(counterB == 3 && world[center] == 0){
            world[center] = 1;
        } if((world[center] == 1 && counterB == 1) ||(world[center] == 1 && counterB == 0)){
            world[center] = 0;
        } if((world[center] == 1 && counterB == 2) ||(world[center] == 1 && counterB == 3)){
            world[center] = 1;
        }if(world[center] == 1 && counterB > 3){
            world[center] = 0;
        }
        return world[center];
    }
}*/


        