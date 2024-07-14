package solitario;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Implementa un juego de dados en que no hay más que un jugador y consiste en
 * lanzar uno o varios dados en cada tirada. La condición de victoria se
 * establece porque se alcanza una tirada ganadora o todas las tiradas ya
 * realizadas cumplen determinada condición.
 */
public class Solitario {
   
    private Function<int[][], Boolean> condicionVictoria;
    private Dado dado;
    private int numDados;
    private ArrayList<int[]> registro;
    private boolean acabada;

    /**
     * Constructor de la clase.
     * 
     * @param caras - Número de caras del dado.
     * @param num - Cantidad de dados que se usan en cada tirada.
     * @param condicion - Función que comprueba si se ha alcanzado la condición de vitoria.
     *   Para ello toma como argumento todas las tiradas y devuelve verdadero o falso.
     */
    public Solitario(int caras, int num, Function<int[][], Boolean> condicion) {
        condicionVictoria = condicion;
        dado = new Dado(caras);
        numDados = num;
        registro = new ArrayList<int[]>();
    }

    /**
     * Implementa una tirada de dados.
     * Si la partida ya se ha dado por ganada, no se tiras más los dados.
     * 
     * @return Verdadero si la jugada resultó ganadora.
     */
    public boolean jugar() throws EjecucionJuego {
        if(acabada) throw new EjecucionJuego("No puede jugar. La partida ya está acabada");

        registro.add(IntStream.generate(() -> dado.lanzar()).limit(numDados).toArray());
        acabada = condicionVictoria.apply(registro.toArray(int[][]::new));
        return acabada;
    }

    /**
     * Resetea la partida como si nunca se hubiera hecho ninguna tirada.
     */
    public void reset() {
        dado.reset();
        registro.clear();
    }

    /**
     * Getter que comprueba si se ha ganado la partida.
     * @return Verdadero si así ha sido.
     */
    public boolean ganada() {
        return acabada;
    }

    /**
     * Getter para saber qué ronda se acaba de jugar.
     * @return Cantidad de tiradas ya efectuadas.
     */
    public int ronda() {
        return registro.size();
    }

    /**
     * Devuelve el resultado de una determina tirada.
     * @param ronda - La ronda de la que se quiere obtener la tirada.
     *   Si no se especifica devuelve el de la última tirada.
     * @return La tirada correspondiente a la ronda.
     */
    public int[] tirada(int ronda) {
        if(ronda < 1 || ronda > ronda()) return null;

        return registro.get(ronda - 1);
    }

    public int[] tirada() {
        return tirada(ronda());
    }

    public int[][] tiradas() {
        return registro.toArray(int[][]::new);
    }
}
