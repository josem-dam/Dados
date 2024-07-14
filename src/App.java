import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import multijugador.Jugador;
import multijugador.Partida;

public class App {
    public static void main(String[] args) throws Exception {
        String[] jugadores = {"Juan", "María", "Pedro"};
        Jugador[] ganadores = new Jugador[0];

        Function[] tiposJuego = {
            (Function<int[][][], Integer[]>) App::todoIgualMulti,
            (Function<int[][][], Integer[]>) App::masPuntuacion
        };

        //Partida partida = new Partida(6, 2, App::todoIgualMulti, 1);
        //Partida partida = new Partida(6, 2, App::masPuntuacion, 3);
        Partida partida = new Partida(6, 2, tiposJuego[1], 3);
        Arrays.stream(jugadores).forEach(j -> partida.agregar(new Jugador(j)));

        while(!partida.ganada()) {
            int turno = partida.getTurno();
            Jugador jugador = partida.jugador();
            if(turno == 1) System.out.printf("Ronda: %d.\n", partida.ronda());
            ganadores = partida.jugar();
            System.out.printf("  %-30s: %s.\n", jugador, Arrays.toString(partida.tirada()));
            if(ganadores != null) System.out.printf("Ganadores de la ronda: %s.\n", Arrays.toString(ganadores));
        }

        System.out.println("Ganadores: " + Arrays.toString(partida.getGanadores()));
    
    }

    /** 
     * Comprueba que en la última tirada todos las puntuaciones de los dados sean iguales.
     * 
     * @param tiradas - Las tiradas realizadas por un jugador. 
     * @return true si todas las puntuaciones son iguales.
     */
    private static boolean todoIgual(int[][] tiradas) {
        if(tiradas.length == 0) return false;

        // La comprobación se hace exclusivamente con la última tirada.
        int[] ultima = tiradas[tiradas.length - 1];

        return Arrays.stream(ultima).allMatch(e -> e == ultima[0]);
    }

    /**
     * Comprueba cuáles son las tiradas en cuya última ronda todas las puntuaciones son iguales.
     * @param tiradas - Las tiradas de todos los jugadores en todas las rondas.
     * @return Un array de enteros en que 1 indica que ese jugador sacó todas las puntuaciones iguales
     *  y 0 que no pasó eso.
     */
    private static Integer[] todoIgualMulti(int[][][] tiradas) {
        return Arrays.stream(tiradas).map(t -> App.todoIgual(t)?1:0).toArray(Integer[]::new);
    }

    /**
     * Comprueba qué tiradas de dados tienen más puntuación en su última ronda. La puntuación de la tirada
     *  es la suma de todas las puntuaciones de esa tirada.
     * @param tiradas - Las tiradas de todos los jugadores en todas las rondas.
     * @return Un array de enteros en que 1 significa que ese jugador obtuvo mayor puntuación.
     */
    private static Integer[] masPuntuacion(int[][][] tiradas) {
        int[][] ultimaT = Arrays.stream(tiradas).map(j -> j[j.length - 1]).toArray(int[][]::new);
        Integer[] sumas = Arrays.stream(ultimaT).map(t -> Arrays.stream(t).sum()).toArray(Integer[]::new);
        int max = Arrays.stream(sumas).max(Integer::compare).get();
        return Arrays.stream(sumas).map(n -> n == max?1:0).toArray(Integer[]::new);
    }
}
