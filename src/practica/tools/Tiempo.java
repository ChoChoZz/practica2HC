/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.tools;

/**
 *
 * @author marco
 */
public class Tiempo {
    
    private Long latencia;
    private Long compensacion;
    
    public Tiempo() {
        
    }
    
    public Tiempo(long latencia,long compensacion) {
        this.latencia = latencia;
        this.compensacion = compensacion;
    }

    /**
     * @return the latencia
     */
    public Long getLatencia() {
        return latencia;
    }

    /**
     * @param latencia the latencia to set
     */
    public void setLatencia(Long latencia) {
        this.latencia = latencia;
    }

    /**
     * @return the compensacion
     */
    public Long getCompensacion() {
        return compensacion;
    }

    /**
     * @param compensacion the compensacion to set
     */
    public void setCompensacion(Long compensacion) {
        this.compensacion = compensacion;
    }
    
}
