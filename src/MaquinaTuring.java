import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
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
        estadosFinales = new HashSet<String>();
        alfabeto = new Alfabeto();
        alfabetoCinta = new Alfabeto();
        transiciones = new ArrayList<Transicion>();
        cabezaLectora = 30;
    }

    private void leerFichero(String path) throws Exception {
        File fichero = new File(path);
        if (!fichero.exists()) throw new Exception("El fichero no existe");
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
            throw new Exception("El simbolo blanco no se encuentra en el alfabeto de la cinta");

        String lineaEstadosFinales = sc.nextLine();
        sc2 = new Scanner(lineaEstadosFinales);
        while (sc2.hasNext()) {
            String estadoFinal = sc2.next();
            if(estados.contains(estadoFinal)) {
                estadosFinales.add(estadoFinal);
            }
            else
                throw new Exception("Un estado final no pertenece al conjunto de estados posibles");
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
                && ((alfabeto.estaEnAlfabeto(primerSimbolo) || alfabetoCinta.estaEnAlfabeto(primerSimbolo)) || primerSimbolo == simboloBlanco )
                    && (alfabetoCinta.estaEnAlfabeto(simboloCinta) || simboloCinta == simboloBlanco)
                        && (movimiento == 'L' || movimiento == 'R')) {
                Transicion transicion = new Transicion(estadoAnterior, primerSimbolo, estadoSiguiente, simboloCinta, movimiento);
                transiciones.add(transicion);
            }
            else {
                throw new Exception("Transicion mal formalizada. Repasa que los estados, los simbolos y el movimiento sean correctos");
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
                            break;
                        }
                    }
                    if (!transicionEncontrada) break;
                } else {
                    break;
                }
            }

            imprimirResultado();

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

    private void imprimirResultado() {
        if (estadosFinales.contains(estadoActual)) {
            System.out.println("La máquina SI se ha detenido en un estado final. La maquina SI ha reconocido la cadena.");
        }
        else {
            System.out.println("La maquina se ha detenido en un estado NO final. La maquina NO ha reconocido la cadena.");
        }

        if (cadena.charAt(cabezaLectora) == simboloBlanco) {
            System.out.println("La cabeza lectora se ha detenido en un simbolo vacio. No quedan caracteres a la derecha de la cabeza lectora.");
        }
        else {
            System.out.println("Cadena desde la cabeza lectora hasta primer simbolo blanco:");
            while(cadena.charAt(cabezaLectora) != simboloBlanco) {
                System.out.print(cadena.charAt(cabezaLectora));
                cabezaLectora++;
            }
            System.out.println();
        }

    }

    private void resetAutomata(String cadena) {
        estadoActual = estadoInicial;
        cabezaLectora = 30;

        char[] rellenoSimbolosBlancos = new char[30];
        Arrays.fill(rellenoSimbolosBlancos, simboloBlanco);
        this.cadena = new String(rellenoSimbolosBlancos) + cadena + new String(rellenoSimbolosBlancos);
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
        System.out.println("Maquina de Turing. Introduce el nombre del fichero que quieres procesar: ");
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
