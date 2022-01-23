#version 120
precision mediump float;

uniform samplerCube u_TexureUnit;
varying vec3 v_Position;

void main() {
    gl_FragColor = textureCube(u_TexureUnit, v_Position);
}
