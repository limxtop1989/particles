package com.versa.particles.objects;

import static com.versa.particles.Constants.BYTES_PER_FLOAT;

import android.graphics.Color;
import android.opengl.GLES20;

import com.versa.particles.data.VertexArray;
import com.versa.particles.program.ParticleShaderProgram;

public class ParticleSystem {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int VECTOR_COMPONENT_COUNT = 3;
    private static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;

    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT
            + VECTOR_COMPONENT_COUNT + PARTICLE_START_TIME_COMPONENT_COUNT;

    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private final float[] particles;
    private final VertexArray vertexArray;
    private final int maxParticleCount;

    private int currentParticleCount;
    private int nextParticle;

    public ParticleSystem(int maxParticleCount) {
        particles = new float[maxParticleCount * STRIDE];
        vertexArray = new VertexArray(particles);
        this.maxParticleCount = maxParticleCount;
    }

    public void addParticle(Geometry.Point position, int color, Geometry.Vector direction,
                            float particleStartTime) {
        final int particleOffset = nextParticle * STRIDE;

        int currentOffset = particleOffset;
        nextParticle++;

        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++;
        }

        if (nextParticle == maxParticleCount) {
            // Start over at the beginning, but keep currentParticleCount so
            // that all the other particles still get drawn.
            nextParticle = 0;
        }

        particles[currentOffset++] = position.getX();
        particles[currentOffset++] = position.getY();
        particles[currentOffset++] = position.getZ();
        // [0, 255] -> [0.0, 1.0]
        particles[currentOffset++] = Color.red(color) / 255f;
        particles[currentOffset++] = Color.blue(color) / 255f;
        particles[currentOffset++] = Color.green(color) / 255f;

        particles[currentOffset++] = direction.getX();
        particles[currentOffset++] = direction.getY();
        particles[currentOffset++] = direction.getZ();

        particles[currentOffset++] = particleStartTime;

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT);
    }

    public void bindData(ParticleShaderProgram program) {
        int dataOffset = 0;
        vertexArray.setVertexAttributePointer(dataOffset, program.getPositionLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(dataOffset, program.getColorLocation(),
                COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(dataOffset, program.getDirectionVectorLocation(),
                VECTOR_COMPONENT_COUNT, STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(dataOffset, program.getParticleStartTimeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, currentParticleCount);
    }
}
