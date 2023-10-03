/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.unaplanilla.model;

/**
 *
 * @author ANTHONY
 */
public class Gasto {
    public double monto;
    public String descripcion;

    public Gasto(double monto, String descripcion) {
        this.monto = monto;
        this.descripcion = descripcion;
    }
    
    public double getMonto() {
        return monto;
    }
   
}
