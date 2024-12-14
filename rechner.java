 Turtle t = new Turtle();
 Random rand = new Random();
float evaluateRPN(String value) {
    float resultValue;
    String graph = "";
    if (value == null || value.trim().isEmpty()) {
        throw new IllegalArgumentException("Ausdruck darf nicht leer sein.");
    }

    if (!value.contains(" ") && !value.contains("+") && !(value.substring(1).contains("-")) && 
        !value.contains("/") && !value.contains("*")) {
            resultValue = Float.parseFloat(value.trim());
            graph = resultValue + "";
            int x= 150;
            int y = 150;
                for(int i = 0; i <graph.length();i++){
                    t.textSize = 20;
                    t.moveTo(x,y);
                    for(int j = 1; j<= 4;j++){
                        t.lineWidth(7).forward(50).right(90).lineWidth(1);
                    }t.forward(25).right(90).penUp().forward(25).penDown().right(180).color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256)).text(Character.toString(graph.charAt(i))).left(270).color(0,0,0);
                    x = x + 50;
                }
        return resultValue;
    }

    String[] array = value.trim().split("");
    StringBuilder f = new StringBuilder();
    StringBuilder rest = new StringBuilder();

    for (int i = 0; i < array.length; i++) {
        if (array[i].equals("+") || array[i].equals("-") || array[i].equals("*") || array[i].equals("/")) {
            for (int j = 0; j <= i; j++) {
                f.append(array[j]);
            }
            for (int t = i + 1; t < array.length; t++) {
                rest.append(array[t]);
            }
            break;
        }
    }

    return space(f.toString().trim(), rest.toString().trim());
}

float space(String wert, String rest) {
    List<String> list = new ArrayList<>();
    String v = "";
    int k = 0;

    for (int i = k; i < wert.length(); i++) {
        String current = Character.toString(wert.charAt(i));
        if (current.equals(" ") || current.equals("+") || current.equals("-") || current.equals("/") || current.equals("*")) {
            int b = i - 1;
            for (int a = k; a <= b; a++) {
                v = v + Character.toString(wert.charAt(a));
            }
            if (!v.isEmpty()) {
                list.add(v);
            }
            v = "";
            k = i + 1;
        }
    }
    list.add(Character.toString(wert.charAt(wert.length() - 1)));
 //  System.out.println("list:" + list);
    return methode(list, rest);
}

float methode(List<String> liste, String rest) {
    if (liste.size() < 3) {
        throw new IllegalArgumentException("Ungültiger Ausdruck: Liste muss mindestens 3 Werte enthalten, mit jeweils 2 Operand.");
    }

    String operator = liste.remove(liste.size() - 1); 
    float b = Float.parseFloat(liste.remove(liste.size() - 1)); 
    float a = Float.parseFloat(liste.remove(liste.size() - 1)); 

    float result;
    switch (operator) {
        case "+":
            result = a + b;
            break;
        case "-":
            result = a - b;
            break;
        case "*":
            result = a * b;
            break;
        case "/":
            if (b == 0) {
                throw new ArithmeticException("Division durch Null!");
            }
            result = a / b;
            break;
        default:
            throw new IllegalArgumentException("Ungültiger Operator: " + operator);
    }

    String newValue = String.join(" ", liste) + " " + result + (rest.isEmpty() ? "" : " " + rest);
  // System.out.println("newValue" + newValue);
    return evaluateRPN(newValue.trim());
}



