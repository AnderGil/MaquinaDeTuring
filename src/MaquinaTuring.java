import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class MaquinaTuring {

    private String cadena;
    private int cabezaLectora;
    private char simboloBlanco;
    private String estadoActual;
    private String estadoInicial;
    private HashSet<String> estados;
    private HashSet<String> estadosFinales;
    private Alfabeto alfabeto;
    private Alfabeto alfabetoCinta;
    private ArrayList<Transicion> transiciones;

    public MaquinaTuring() {
        estados = new HashSet<String>();
        alfabeto = new Alfabeto();
        transiciones = new ArrayList<Transicion>();
        cabezaLectora = 0;
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

        String lineaAlfabetoCinta = sc.nextLine();
        for(int i = 0; i < lineaAlfabetoCinta.length(); i++) {
            char c = lineaAlfabetoCinta.charAt(i);
            alfabetoCinta.anadirAlfabeto(c);
        }

        String estadoInicial = sc.nextLine();
        if (estados.contains(estadoInicial)) {
            this.estadoInicial =estadoInicial;
            estadoActual = this.estadoInicial;
        }
        else
            throw new Exception("El estado inicial no se encuentra en la lista de estados");


        char simboloBlanco = sc.nextLine().charAt(0);
        if (alfabetoCinta.estaEnAlfabeto(simboloBlanco)) {
            this.simboloBlanco = simboloBlanco;
        }
        else
            throw new Exception("El simbolo blanco no se encuentr en el alfabeto de la cinta");

        String lineaEstadosFinales = sc.nextLine();
        sc2 = new Scanner(lineaEstadosFinales);
        while (sc2.hasNext()) {
            String estadoFinal = sc2.next();
            estadosFinales.add(estadoFinal);
        }
        sc2.close();

        while (sc.hasNextLine()) {           //Leemos las transiciones
            sc2 = new Scanner(sc.nextLine());
            String estadoAnterior = sc2.next();
            char primerSimbolo = sc2.next().charAt(0);
            String estadoSiguiente = sc2.next();
            char simboloCinta = sc2.next().charAt(0);
            char movimiento = sc2.next().charAt(0);
            sc2.close();

            if (estados.contains(estadoAnterior) && estados.contains(estadoSiguiente)
                && (alfabeto.estaEnAlfabeto(primerSimbolo) || primerSimbolo == simboloBlanco )
                    && (alfabetoCinta.estaEnAlfabeto(simboloCinta)) || simboloCinta == simboloBlanco) {
                Transicion transicion = new Transicion(estadoAnterior, primerSimbolo, estadoSiguiente, simboloCinta, movimiento);
                transiciones.add(transicion);
            }
        }

        sc.close();
    }

    public void recorrer() {
            while (true) {
                if (cabezaLectora >= 0 && cabezaLectora < cadena.length()) {
                    char simbolo = cadena.charAt(cabezaLectora);
                    boolean transicionEncontrada = false;
                    for (Transicion tr : transiciones) {
                        if (simbolo == tr.getPrimerSimbolo() && (estadoActual.compareTo(tr.getEstadoAnterior()) == 0)) {
                            transicionar(tr);
                            transicionEncontrada = true;
                        }
                    }
                    if (!transicionEncontrada) break;
                } else {
                    break;
                }
            }

            if (estadosFinales.contains(estadoActual)) {
                System.out.println("La máquina se ha detenido cuando se encontraba en un estado final");
                while (cadena.charAt(cabezaLectora) != simboloBlanco) {
                    System.out.print(cadena.charAt(cabezaLectora));
                    cabezaLectora++;
                }
            }
    }

    private void transicionar(Transicion transicion) {
        estadoActual = transicion.getEstadoSiguiente();

        char[] newString = cadena.toCharArray();
        newString[cabezaLectora] = transicion.getSimboloAEscribir();
        cadena = String.valueOf(newString);

        if (transicion.getMovimientoCinta() == 'R') {
            cabezaLectora = cabezaLectora + 1;
        } else {
            cabezaLectora = cabezaLectora - 1;
        }
    }

    private void resetAutomata(String cadena) {
        estadoActual = estadoInicial;
        cabezaLectora = 0;
        this.cadena = cadena + (simboloBlanco * 30);
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
                maquinaTuring.resetAutomata(cadena);
                maquinaTuring.recorrer();
            }
            System.out.println("Introduce la cadena que quieras comprobar. Para terminar, introduce FIN");
            cadena = input.nextLine();
        }
    }
}
