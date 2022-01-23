package com.versa.particles.data;

import android.opengl.GLES20;

import com.versa.particles.program.SkyBoxShaderProgram;

import java.nio.ByteBuffer;

public class SkyBox {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private final VertexArray vertexArray;
    private final ByteBuffer indexArray;

    public SkyBox() {
        /*
         * right-hand coordinate system.
         */
        vertexArray = new VertexArray(new float[] {
                -1, 1, 1,// 0. left top near
                 1, 1, 1,// 1. right top near
                -1,-1, 1,// 2. left bottom near
                 1,-1, 1,// 3. right bottom near
                -1, 1,-1,// 4. left top
                 1, 1,-1,// 5.
                -1,-1,-1,// 6.
                 1,-1,-1,// 7.
        });

        indexArray = ByteBuffer.allocate(6 * 6).put(new byte[] {
                // front
                1, 3, 0,
                0, 3, 2,
                // back
                4, 6, 5,
                5, 6, 7,
                // left
                0, 2, 4,
                4, 2, 6,
                // right
                5, 7, 1,
                1, 7, 3,
                // top
                5, 1, 4,
                4, 1, 0,
                // bottom
                6, 2, 7,
                7, 2, 3,
        });
        indexArray.position(0);
    }

    public void bindData(SkyBoxShaderProgram skyBoxShader) {
        vertexArray.setVertexAttributePointer(0,
                skyBoxShader.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        // TODO How does index array work?
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, indexArray);
    }
}
