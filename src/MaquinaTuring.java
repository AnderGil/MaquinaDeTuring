import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class MaquinaTuring {

    private String cadena;
    private String estadoActual;
    private String estadoInicial;
    private char simboloInicialPila;
    private HashSet<String> estados;
    private Pila pila;
    private Alfabeto alfabeto;
    private ArrayList<Transicion> transiciones;

    public MaquinaTuring() {
        estados = new HashSet<String>();
        pila = new Pila();
        alfabeto = new Alfabeto();
        transiciones = new ArrayList<Transicion>();
    }

    private void leerFichero(String path) throws Exception {
        File fichero = new File(path);
        Scanner sc = new Scanner(fichero);
        String lineaEstados;
        while (true) {
            String linea = sc.nextLine();
            if (Character.compare(linea.charAt(1), '#') != 0 && Character.compare(linea.charAt(0), '#') != 0){
                lineaEstados = linea;
                break;
            }
        }
        Scanner sc2 = new Scanner(lineaEstados);
        while (sc2.hasNext()) {
            String estado = sc2.next();
            estados.add(estado);
        }
        sc2.close();

        String lineaAlfabeto = sc.nextLine();
        for(int i = 0; i < lineaAlfabeto.length(); i++) {
            char c = lineaAlfabeto.charAt(i);
            alfabeto.anadirAlfabeto(c);
        }

        String lineaAlfabetoPila = sc.nextLine();
        for(int i = 0; i < lineaAlfabetoPila.length(); i++) {
            char c = lineaAlfabetoPila.charAt(i);
            pila.anadirAlfabeto(c);
        }

        String estadoInicial = sc.nextLine();
        if (estados.contains(estadoInicial)) {
            this.estadoInicial =estadoInicial;
            estadoActual = this.estadoInicial;
        }
        else
            throw new Exception("El estado inicial no se encuentra en la lista de estados");


        char simboloInicial = sc.nextLine().charAt(0);
        if (pila.estaEnAlfabeto(simboloInicial)) {
            pila.empilar(simboloInicial);
            this.simboloInicialPila = simboloInicial;
        }
        else
            throw new Exception("El simbolo inicial de la pila no se encuentra en el alfabeto de la pila");

        while (sc.hasNextLine()) {           //Leemos las transiciones
            sc2 = new Scanner(sc.nextLine());
            String estadoAnterior = sc2.next();
            char primerSimbolo = sc2.next().charAt(0);
            char simboloCimaPila = sc2.next().charAt(0);
            String estadoSiguiente = sc2.next();
            String simbolosPilaSiguiente = "";
            while (sc2.hasNext()){
                char otroSimbolo = sc2.next().charAt(0);
                if (pila.estaEnAlfabeto(otroSimbolo) || otroSimbolo=='.') {
                    simbolosPilaSiguiente = simbolosPilaSiguiente + otroSimbolo;
                }
                else throw new Exception("La transicion contiene algun simbolo que no esta en el alfabeto de la pila");
            }
            sc2.close();

            if (estados.contains(estadoAnterior) && estados.contains(estadoSiguiente)
                && (alfabeto.estaEnAlfabeto(primerSimbolo) || primerSimbolo=='.' ) && pila.estaEnAlfabeto(simboloCimaPila)) {
                Transicion transicion = new Transicion(estadoAnterior, primerSimbolo, simboloCimaPila, estadoSiguiente, simbolosPilaSiguiente);
                transiciones.add(transicion);
            }
        }

        sc.close();
    }
    public void iniciar(String cad) {
        if (recorrerRecursivo(cad, pila)) {
            System.out.println("La palabra SÍ es aceptada por el lenguaje");
        }
        else {
            System.out.println("La palabra NO es aceptada por el lenguaje");
        }
    }
    public boolean recorrerRecursivo(String cad, Pila pila) {
        System.out.println("Cadena actual: " + cad + " Estado actual de la pila: " + pila + " Estado actual: " + estadoActual);
        if (cad.length() == 0 && pila.estaVacia()) {
            return true;
        }
        else if (cad.length() > 0 && pila.estaVacia()) {
            return false;
        }
        else {
            char simbolo = '.';
            if(cad.length()>0) {
                simbolo = cad.charAt(0);
            }
            String copyEstado = estadoActual + "";
            String copyCad = cad + "";
            Pila copyPila = pila.copiarPila();
            for (Transicion tr : transiciones) {
                if ((pila.cima() == tr.getSimboloCimaPila()) && (estadoActual.compareTo(tr.getEstadoAnterior()) == 0)) {
                    if (tr.getPrimerSimbolo() == '.' || simbolo == tr.getPrimerSimbolo()) {
                        if(tr.getPrimerSimbolo() != '.')
                            cad = cad.substring(1);
                        transicionar(tr, pila);
                        boolean isAccepted = recorrerRecursivo(cad, pila);
                        if (isAccepted) return true;
                        else {
                            cad = copyCad;
                            pila = copyPila.copiarPila();
                            estadoActual = copyEstado;
                            System.out.println("Solución no encontrada por esa rama. Retrocediendo.");
                            System.out.println("Cadena actual: " + cad + " Estado actual de la pila: " + pila.toString() + " Estado actual: " + estadoActual);
                        }
                    }
                }
            }
            return false;
        }
    }

    private void transicionar(Transicion transicion, Pila pila) {
        pila.desempilar();
        estadoActual = transicion.getEstadoSiguiente();
        if (transicion.getSimbolosPilaSiguiente().compareTo(".") != 0) {
            String aEmpilar = transicion.getSimbolosPilaSiguiente();
            for (int i = aEmpilar.length()-1; i >= 0; i--) {
                pila.empilar(aEmpilar.charAt(i));
            }
        }
    }

    private void resetAutomata() {
        pila.vaciar();
        pila.empilar(simboloInicialPila);
        estadoActual = estadoInicial;
    }

    private boolean comprobarCadena (String cadena) {
        for (int i = 0; i < cadena.length(); i++) {
            if (!alfabeto.estaEnAlfabeto(cadena.charAt(i))) {
                System.out.println("Algún caracter introducido no pertence al alfabeto del lenguaje");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        MaquinaTuring maquinaTuring = new MaquinaTuring();
        System.out.println("Automáta de vaciado de pila. Introduce el nombre del fichero que quieres procesar: ");
        String fichero = input.nextLine();
        maquinaTuring.leerFichero(fichero);
        System.out.println("Fichero leído correctamente. Introduce la cadena que quieras comprobar. Para terminar, introduce FIN");

        String cadena = input.nextLine();

        while (cadena.compareTo("FIN") != 0) {
            if (maquinaTuring.comprobarCadena(cadena)) {
                maquinaTuring.resetAutomata();
                maquinaTuring.iniciar(cadena);
            }
            System.out.println("Introduce la cadena que quieras comprobar. Para terminar, introduce FIN");
            cadena = input.nextLine();
        }
    }
}
