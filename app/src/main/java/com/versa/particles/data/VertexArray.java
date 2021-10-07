package com.versa.particles.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.versa.particles.Constants.BYTES_PER_FLOAT;

public class VertexArray {

    private FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData) {
        // Create native memory used by OpenGL.
        floatBuffer = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        floatBuffer.put(vertexData);
    }

    public void setVertexAttributePointer(int dataOffset, int attributeLocation, int componentCount,
                                          int stride) {
        floatBuffer.position(dataOffset);
        // TODO: API comment.
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT,
                false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }

    /**
     * Copies length floats from the given array into this buffer, starting at the given offset in
     * the array and at the current position of this buffer. The position of this buffer is then
     * incremented by length.
     * @param particle
     * @param start
     * @param count
     */
    public void updateBuffer(float[] particle, int start, int count) {
        floatBuffer.position(start);
        floatBuffer.put(particle, start, count);
        // why reset to zero here?
        floatBuffer.position(0);
    }
}
