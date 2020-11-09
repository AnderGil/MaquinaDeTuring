public class Transicion {
    private String estadoAnterior;
    private char primerSimbolo;
    private String estadoSiguiente;
    private char simboloAEscribir;
    private char movimientoCinta;

    public Transicion(String eA, char pS, String eS, char sAE, char mC) {
        this.estadoAnterior = eA;
        this.primerSimbolo = pS;
        this.estadoSiguiente = eS;
        this.simboloAEscribir = sAE;
        this.movimientoCinta = mC;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public char getPrimerSimbolo() {
        return primerSimbolo;
    }


    public String getEstadoSiguiente() {
        return estadoSiguiente;
    }

    public char getMovimientoCinta() { return movimientoCinta; }

    public char getSimboloAEscribir() { return simboloAEscribir; }


    public void print()  {
        System.out.print("Estado anterior: " + estadoAnterior);
        System.out.print(" Primer simbolo: " + primerSimbolo);
        //System.out.println("Estado siguiente: " + estadoSiguiente);
        //System.out.println("Simbolos siguientes a empilar: " + simbolosPilaSiguiente);

    }
}
