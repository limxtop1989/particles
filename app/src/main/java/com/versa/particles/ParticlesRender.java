package com.versa.particles;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.versa.particles.objects.Geometry;
import com.versa.particles.objects.ParticleShooter;
import com.versa.particles.objects.ParticleSystem;
import com.versa.particles.program.ParticleShaderProgram;
import com.versa.particles.utils.LogWrapper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ParticlesRender implements GLSurfaceView.Renderer {

    private Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    private ParticleShaderProgram particleProgram;
    private ParticleSystem particleSystem;

    private ParticleShooter redParticleShooter;
    private ParticleShooter greenParticleShooter;
    private ParticleShooter blueParticleShooter;

    private final float angleVariance = 5f;// degree unit
    private final float speedVariance = 1f;

    private long globalStartTime;

    public ParticlesRender(Context context) {
        this.context = context;
        LogWrapper.d();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        LogWrapper.d();
        particleProgram = new ParticleShaderProgram(context);
        particleSystem = new ParticleSystem(10000);
        globalStartTime = System.nanoTime();

        final Geometry.Vector particleDirection = new Geometry.Vector(0.0f, 1.0f, 0.0f);

        redParticleShooter = new ParticleShooter(new Geometry.Point(-0.8f, 0.0f, 0.0f),
                particleDirection, Color.rgb(255, 50, 5), angleVariance, speedVariance);

        greenParticleShooter = new ParticleShooter(new Geometry.Point(0.0f, 0.0f, 0.0f),
                particleDirection, Color.rgb(25, 255, 25), angleVariance, speedVariance);

        blueParticleShooter = new ParticleShooter(new Geometry.Point(0.8f, 0.0f, 0.0f),
                particleDirection, Color.rgb(5, 50, 255), angleVariance, speedVariance);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Matrix.perspectiveM(projectionMatrix, 0,45, (float) width
                / (float) height, 1f, 10f);
        setIdentityM(viewMatrix, 0);
        translateM(viewMatrix, 0, 0f, -1.5f, -5f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
                viewMatrix, 0);
        LogWrapper.d();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        LogWrapper.d();
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;

        redParticleShooter.addParticles(particleSystem, currentTime, 5);
        greenParticleShooter.addParticles(particleSystem, currentTime, 5);
        blueParticleShooter.addParticles(particleSystem, currentTime, 5);

        particleProgram.useProgram();
        particleProgram.setUniforms(viewProjectionMatrix, currentTime);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
    }
}
