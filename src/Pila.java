import java.util.Stack;

public class Pila {
    private Stack<Character> pila;
    private Alfabeto alfabeto;

    public Pila() {
        pila = new Stack<Character>();
        alfabeto = new Alfabeto();
    }

    public Pila(Stack<Character> p, Alfabeto a) {
        pila = p;
        alfabeto = a;
    }

    public Alfabeto getAlfabeto() {
        return alfabeto;
    }
    public Stack<Character> getPila() {
        return pila;
    }

    public char cima() {
        return pila.peek();
    }

    public void empilar(char c) {
        pila.push(c);
    }

    public char desempilar() {
        char c = pila.pop();
        return c;
    }

    public boolean estaVacia(){
        return pila.isEmpty();
    }

    public void vaciar() {
        pila.clear();
    }

    public boolean estaEnAlfabeto (char c) {
        return alfabeto.estaEnAlfabeto(c);
    }

    public void anadirAlfabeto (char c) {
        alfabeto.anadirAlfabeto(c);
    }

    public Pila copiarPila () {
        Pila aux = new Pila(), copia = new Pila();
        while (!estaVacia())
            aux.empilar(pila.pop());
        while (!aux.estaVacia()){
            pila.push(aux.cima());
            copia.empilar(aux.desempilar());
        }
        return copia;
    }

    @Override
    public String toString() {
        String str = "";
        Pila pilaAux = new Pila();
        while (!estaVacia()) {
            str = str + pila.peek();
            pilaAux.empilar(pila.pop());
        }
        while (!pilaAux.estaVacia()) {
            pila.push(pilaAux.desempilar());
        }
        return str;
    }
}
