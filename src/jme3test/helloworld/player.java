/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jme3test.helloworld;
import com.jme3.scene.Spatial;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Geometry;
import com.jme3.math.*;

/**
 *
 * @author Arthur
 */

/*Debido a ESTA CLASE y SU IMPLEMENTACION EN EL SIMPLEINITAPP tengo que reconfigurar
unas cosas en el programa principal. Igual esto me va a permitir almacenar los nicknames para mostrarlos, el color
para pintar, la porteria respectiva para sumar y/o restar puntos según el caso y las físicas
de la colisión. COSA QUE NO VOY A HACER AHORITA PORQUE TENGO QUE ESTUDIAR ESTRUCTURA

Eres bienvenido de añadir los atributos y métodos que desees aquí según lo que requieras y como
necesites para el networking.
*/
public class player {
    private Spatial puck;
    private RigidBodyControl phy;
    private Geometry porteria;
    private ColorRGBA color;
    private String nick;
    private Integer number;
    
    //TODO implementar nodo de la cámara
    
    public player(){
        
    }
    
    public player(Spatial p, RigidBodyControl phy, Geometry port, ColorRGBA color, String nick, Integer num){
        this.puck=p;
        this.phy=phy;
        this.porteria=port;
        this.color=color;
        this.nick=nick;
        this.number=num;
    }

    public Spatial getPuck() {
        return puck;
    }

    public void setPuck(Spatial puck) {
        this.puck = puck;
    }

    public RigidBodyControl getPhy() {
        return phy;
    }

    public void setPhy(RigidBodyControl phy) {
        this.phy = phy;
    }

    public Geometry getPorteria() {
        return porteria;
    }

    public void setPorteria(Geometry porteria) {
        this.porteria = porteria;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
    
    
}
