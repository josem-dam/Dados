package multijugador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import solitario.EjecucionJuego;
import solitario.Solitario;

/**
 * Implementa una partida de dados con varios jugadores.
 * Al final de cada ronda se comprueba qué jugadores han obtenido punto
 * en esa ronda y se les suma. Ganará el jugador que antes alcance una
 * puntuación máxima.
 */
public class Partida {

   private int caras;
   private int numDados;
   private Function<int[][][], Integer[]> condicionVictoria;
   private ArrayList<Jugador> jugadores;
   private int turno;
   private boolean iniciada;
   private Jugador[] ganadores;
   private int maxPuntos;

   /**
    * Constructor de la clase.

    * @param caras - Número de caras los dados.
    * @param num - Cantidad de dados que se tiran a la vez.
    * @param condicion - Función que comprueba si se ha ganado.  Devuelve un array con los
    *    puntos que ha obtenido cada jugador.
    * @param max - Puntuación máxima con la que un jugador gana la partida.
    */
   public Partida(int caras, int num, Function<int[][][], Integer[]> condicion, int max) {
      this.caras = caras;
      this.numDados = num;
      condicionVictoria = condicion;
      jugadores = new ArrayList<Jugador>();
      iniciada = false;
      ganadores = null;
      maxPuntos = max;
   }

   /**
    * Comprueba si ya ha comenzado la partida. La partida comienza cuando se lenzan dados
    * por primera vez.
    * @return Verdadero si está comenzada.
    */
   public boolean comenzada() {
      return iniciada;
   }

   /**
    * Comprueba si la partida ya ha acabado por haber ganado uno de los participantes.
    * @return Verdadero si la partida está acabada.
    */
   public boolean ganada() {
      return ganadores != null;
   }

   /**
    * Devuelve los ganadores de la partida.
    * @return Un array con los jugadores vencedores.
    */
   public Jugador[] getGanadores() {
      return ganadores;
   }

   /**
    * Devuelve quién es el jugador que debe lanzar dados.
    * @return La posición del jugador.
    */
   public int getTurno() {
      return turno + 1;
   }

   /**
    * Añade jugadores a la partida.
    * @param jugador -  El jugador que se quiere añadir.
    * @return La cantidad de jugadores que ya se han añadido.
    */
   public int agregar(Jugador jugador) throws EjecucionJuego {
      if(comenzada()) throw new EjecucionJuego("No puede agregar más jugadores. La partida ya ha comenzado");

      jugadores.add(jugador);
      // Al lanzar los dados cada jugador no se realiza ninguna comprobación.
      jugador.asociarJuego(new Solitario(caras, numDados, r -> false));
      return jugadores.size();
   }

   /**
    * Da comienzo a la partida, cerrado la entrada de más jugadores
    * @return true si la partida no estaba empezada
    */
   public boolean start() {
      if(iniciada) return false;
      iniciada = true;
      // Sortea el orden de los jugadores.
      Collections.shuffle(jugadores);
      return true;
   }

   /**
    * Devuelve el jugador que ocupa un determinado turno.
    * @param turno - El turno del jugador. Si no se especifica, se devuelve el jugador que
    *   debe lanzar los dados.
    * @return El jugador que ocupa un determinado turno.
    */
   public Jugador jugador(int turno) {
      return jugadores.get(turno - 1);
   }

   public Jugador jugador() {
      return jugadores.get(getTurno() - 1);
   }

   /**
    * Lanza dados el jugador que tiene su turno.
    * @return Un array con los jugadores que han obtenido puntuación en la ronda. Si no
    *    es final de ronda, se devuelve null.
    */
   public Jugador[] jugar() throws EjecucionJuego {
      ArrayList<Jugador> ganadoresRonda;
      Jugador[] ganadores;

      if(!iniciada) throw new EjecucionJuego("Debe comenzar explícitamente la partida con start()");
      if(jugadores.size() == 0) throw new EjecucionJuego("No hay jugadores en la partida");
      if(ganada()) throw new EjecucionJuego("No puede jugarse más. La partida ya ha acabado");

      Solitario partida = jugadores.get(turno).juego;

      try { partida.jugar(); }
      catch(EjecucionJuego e) { assert false: "No puede haber acabado ninguna partida individual"; }

      turno = (turno + 1) % jugadores.size();

      // Si ha acabado el turno, se comprueba la victoria.
      if(turno != 0) return null;
      
      ganadoresRonda = new ArrayList<Jugador>();
      // A condicionVictoria hay que pasarle las tiradas de todos los jugadores.
      //Integer[] puntuacion = condicionVictoria.apply(jugadores.stream().map(j -> j.juego).map(ju -> ju.registro.toArray(int[][]::new)).toArray(int[][][]::new));
      Integer[] puntuacion = condicionVictoria.apply(jugadores.stream().map(j -> j.juego).map(ju -> ju.tiradas()).toArray(int[][][]::new));
      for(int i=0; i < jugadores.size(); i++) {
         jugadores.get(i).puntua(puntuacion[i]);
         if(puntuacion[i] > 0) ganadoresRonda.add(jugadores.get(i));
      }

      ganadores = jugadores.stream().filter(j -> j.getPuntos() == maxPuntos).toArray(Jugador[]::new);
      if(ganadores.length > 0) this.ganadores = ganadores;      

      return ganadoresRonda.toArray(Jugador[]::new);
   }

   /**
    * Indica en qué ronda se encuentra la partida.
    * @return Un entero que representa la ronda.
    */
   public int ronda() {
      int ronda = jugadores.get(0).juego.ronda();

      return (getTurno() == 1)?ronda + 1:ronda;
   }

   /**
    * Devuelve el valor de los dados de la última tirada.
    * @return Las puntuaciones de los dados lanzados en la última tirada.
    */
   public int[] tirada() {
      int previo = (turno + jugadores.size() - 1) % jugadores.size();
      return jugadores.get(previo).juego.tirada();
   }
}
