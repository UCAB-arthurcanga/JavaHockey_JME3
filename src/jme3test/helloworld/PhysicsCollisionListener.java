/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jme3test.helloworld;

import com.jme3.bullet.collision.PhysicsCollisionEvent;

/**
 *
 * @author Arthur
 */
public interface PhysicsCollisionListener {
    
    public void collision(PhysicsCollisionEvent event);
    
}
