public class Transicion {
    private String estadoAnterior;
    private char primerSimbolo;
    private char simboloCimaPila;
    private String estadoSiguiente;
    private String simbolosPilaSiguiente;

    public Transicion(String eA, char pS, char sCP, String eS, String sPS) {
        this.estadoAnterior = eA;
        this.primerSimbolo = pS;
        this.simboloCimaPila = sCP;
        this.estadoSiguiente = eS;
        this.simbolosPilaSiguiente = sPS;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public char getPrimerSimbolo() {
        return primerSimbolo;
    }

    public char getSimboloCimaPila() {
        return simboloCimaPila;
    }

    public String getEstadoSiguiente() {
        return estadoSiguiente;
    }

    public String getSimbolosPilaSiguiente() {
        return simbolosPilaSiguiente;
    }

    public void print()  {
        System.out.print("Estado anterior: " + estadoAnterior);
        System.out.print(" Primer simbolo: " + primerSimbolo);
        System.out.println(" Simbolo de la cima de la pila: " + simboloCimaPila);
        //System.out.println("Estado siguiente: " + estadoSiguiente);
        //System.out.println("Simbolos siguientes a empilar: " + simbolosPilaSiguiente);

    }
}
