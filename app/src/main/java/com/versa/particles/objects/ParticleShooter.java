package com.versa.particles.objects;

import android.opengl.Matrix;

import java.util.Random;

import static android.opengl.Matrix.multiplyMV;

public class ParticleShooter {

    private Geometry.Point position;
    private Geometry.Vector direction;
    private int color;

    private final float angleVariance;
    private final float speedVariance;

    private final Random random = new Random();

    private float[] rotationMatrix = new float[16];
    private float[] directionVector = new float[4];
    private float[] resultVector = new float[4];

    public ParticleShooter(Geometry.Point position, Geometry.Vector direction, int color,
                           float angleVariance, float speedVariance) {
        this.position = position;
        this.direction = direction;
        this.color = color;
        this.angleVariance = angleVariance;
        this.speedVariance = speedVariance;

        directionVector[0] = direction.getX();
        directionVector[1] = direction.getY();
        directionVector[2] = direction.getZ();
    }

    public void addParticles(ParticleSystem particleSystem, float currentTime, int count) {
        for (int i = 0; i < count; i++) {
            Matrix.setRotateEulerM(rotationMatrix, 0, (random.nextFloat() - 0.5f) * angleVariance,
                    (random.nextFloat() - 0.5f) * angleVariance, (random.nextFloat() - 0.5f) * angleVariance);
            multiplyMV(
                    resultVector, 0,
                    rotationMatrix, 0,
                    directionVector, 0);
            float speedAdjustment = 1f + random.nextFloat() * speedVariance;
            Geometry.Vector shootDirection = new Geometry.Vector(
                    resultVector[0] * speedAdjustment,
                    resultVector[1] * speedAdjustment,
                    resultVector[2] * speedAdjustment);
            particleSystem.addParticle(position, color, shootDirection, currentTime);
        }
    }
}
