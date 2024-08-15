package solitario;
/**
 * Implementa el comportamiento de un dado de "n" caras.
 * 
 * @author José Miguel Sánchez Alés
 * @version 1.0
 */
public class Dado {
    
    private int caras;
    private Integer valor;

    /**
     * Genera un entero aleatorio entre dos límites
     * @param min - El mínimo (incluido)
     * @param max - El máximo (incluido)
     * @return El entero aleatorio
     */
    public static int obtenerEnteroAleatorio(int min, int max) {
        return (int) (Math.random()*(max + 1 - min) + min);
    }

    /**
     * Constructor de la clase.
     * 
     * @param caras - El número de caras del dado.
     */
    public Dado(int caras) {
        this.caras = caras;
    }

    /**
     * Lanza el dado.
     * @return El resultado del lanzamiento.
     */
    public int lanzar() {
        return this.valor = obtenerEnteroAleatorio(1, caras);
    }

    /**
     * Devuelve las caras que tiene el dado.
     * @return Un entero que representa el número de caras.
     */
    public int getCaras() {
        return caras;
    }

    /**
     * Devuelve el resultado de la última tirada.
     * @return Un entero con el valor de la última tirada.
     */
    public int getValor() {
        return valor;
    }

    /**
     * Deja el dado como si nunca se hubiera tirado.
     */
    public void reset() {
        this.valor = null;
    }

    /**
     * Genera una representación textual del dado.
     * @return El valor de la última tirada en forma de cadena.
     */
    public String toString() {
        return (this.valor == null)?null:Integer.toString(this.valor);
    }

    /**
     * Compara el dado con otro.
     * @return Verdadero si ambos dados obtuvieron una misma puntuación en la última tirada.
     */
    public boolean equals(Object dado) {
        return valor == ((dado instanceof Dado)?((Dado) dado).valor:dado);
    }
}
