package com.versa.particles;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.versa.particles.data.SkyBox;
import com.versa.particles.objects.Geometry;
import com.versa.particles.objects.ParticleShooter;
import com.versa.particles.objects.ParticleSystem;
import com.versa.particles.program.ParticleShaderProgram;
import com.versa.particles.program.SkyBoxShaderProgram;
import com.versa.particles.utils.LogWrapper;
import com.versa.particles.utils.TextureHelper;

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

    private int particleTexture;

    private SkyBoxShaderProgram skyBoxShaderProgram;
    private SkyBox skyBox;
    private int skyBoxTexture;

    public ParticlesRender(Context context) {
        this.context = context;
        LogWrapper.d();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        LogWrapper.d();

        skyBoxShaderProgram = new SkyBoxShaderProgram(context);
        skyBox = new SkyBox();
        skyBoxTexture = TextureHelper.loadCubeMap(context, new int[] {
                R.drawable.left,
                R.drawable.right,
                R.drawable.bottom,
                R.drawable.top,
                R.drawable.front,
                R.drawable.back,
        });

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

        // Enable blending. blend equation:
        // output = (source factor * source fragment) + (destination factor * destination fragment)
        // source fragment comes from the fragment shader.
        // destination fragment is what's already there in the frame buffer.
        glEnable(GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);

        //
        particleTexture = TextureHelper.loadTexture(context, R.drawable.particle_texture);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Matrix.perspectiveM(projectionMatrix, 0,45, (float) width
                / (float) height, 1f, 10f);

        LogWrapper.d();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        LogWrapper.d();

        drawSkyBox();
        drawParticles();
    }

    private void drawSkyBox() {
        setIdentityM(viewMatrix, 0);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        skyBoxShaderProgram.useProgram();
        skyBoxShaderProgram.setUniforms(viewProjectionMatrix, skyBoxTexture);
        skyBox.bindData(skyBoxShaderProgram);
        skyBox.draw();
    }

    private void drawParticles() {
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;

        redParticleShooter.addParticles(particleSystem, currentTime, 5);
        greenParticleShooter.addParticles(particleSystem, currentTime, 5);
        blueParticleShooter.addParticles(particleSystem, currentTime, 5);

        setIdentityM(viewMatrix, 0);
        translateM(viewMatrix, 0, 0f, -1.5f, -5f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
                viewMatrix, 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);

        particleProgram.useProgram();
        particleProgram.setUniforms(viewProjectionMatrix, currentTime, particleTexture);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();

        glDisable(GL_BLEND);
    }
}
