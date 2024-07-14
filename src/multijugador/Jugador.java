package multijugador;

import java.util.UUID;
import solitario.Solitario;

public class Jugador {
    
    private String nombre;
    Solitario juego;
    private UUID id;
    private int puntos;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.id = UUID.randomUUID();
        puntos = 0;
    }

    void asociarJuego(Solitario solitario) {
        juego = solitario;
    }

    public String getNombre() {
        return nombre;
    }

    public UUID getID() {
        return id;
    }

    public int getPuntos() {
        return puntos;
    }

    public int puntua(int puntuacion) {
        puntos += puntuacion;
        return puntos;
    }

    public String toString() {
        return getNombre();
    }
}
