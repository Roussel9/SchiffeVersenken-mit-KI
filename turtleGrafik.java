public class Draw{
  private int x;
  private int y;
  private int positionCounter;
  Random random = new Random();

  void mainGrafik(Turtle tu, double size){
    assert (size >=20 && size <=80);
    x=100;
    y=100;
    positionCounter=0;
    tu.color(0,0,0);
    grafik(tu,size,4);
    colorDraw(tu,3);
  }

  private void grafik(Turtle tu, double size,int value){
    if(value <=0 || positionCounter >= 4){
      return;
    }
    int count=0;
    tu.moveTo(x,y);
    while(count<40){
      for(int i = 1; i<=4;i++){  
        tu.forward(size);
        tu.left(90);   
      }
      tu.left(10);
      count++;
    }
    newPosition();
    grafik(tu,size,value - 1);
 }

 private void newPosition(){
    if (x == 100 && y == 100) {
        x += 300; 
        positionCounter++;
    } else if (x == 400 && y == 100) {
        x -= 300; 
        y += 300; 
        positionCounter++;
    } else if (x == 100 && y == 400) {
        x += 300; 
        positionCounter++;
    }else{
      positionCounter++;
    }
 }

 private void colorDraw(Turtle tur,int x){
    int counter=0;
    tur.color(random.nextInt(251),random.nextInt(251),random.nextInt(251));
    tur.moveTo(250, 250);
    while(counter <=15){
      for(int i= 1;i<=2;i++){
        tur.left(120);
        tur.forward(30);
      }
      tur.left(60);
      tur.forward(30);
      tur.left(120);
      tur.forward(30);
      tur.left(10);
      counter++;
    }
  }
}
  

 