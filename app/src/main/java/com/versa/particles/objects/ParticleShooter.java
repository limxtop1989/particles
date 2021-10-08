package com.versa.particles.objects;

public class ParticleShooter {

    private Geometry.Point position;
    private Geometry.Vector direction;
    private int color;

    public ParticleShooter(Geometry.Point position, Geometry.Vector direction, int color) {
        this.position = position;
        this.direction = direction;
        this.color = color;
    }

    public void addParticles(ParticleSystem particleSystem, float currentTime, int count) {
        for (int i = 0; i < count; i++) {
            particleSystem.addParticle(position, color, direction, currentTime);
        }
    }
}
