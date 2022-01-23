#version 120
uniform mat4 u_Matrix;
attribute vec3 a_Position;
varying vec3 v_Position;

void main() {
    v_Position = a_Position;
    v_Position.z = a_Position.z;
    gl_Position = u_Matrix * (v_Position, 1.0);
    // TODO: How does this work?
    gl_Position = gl_Position.xyww;//set the z component equal to the w component.
}
