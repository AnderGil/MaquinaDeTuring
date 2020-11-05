import java.util.HashSet;

public class Alfabeto {
    private HashSet<Character> alfabeto;

    public Alfabeto() {
        alfabeto = new HashSet<Character>();
    }
    public boolean estaEnAlfabeto (char c) {
        return alfabeto.contains(c);
    }

    public void anadirAlfabeto (char c) {
        if (Character.compare(c, ' ')!=0 && !estaEnAlfabeto(c))
            alfabeto.add(c);
    }

    public void print() {
        for (Character c: alfabeto)
            System.out.println(c);
    }
}
