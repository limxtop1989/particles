// A uniform is a global Shader variable declared with the "uniform" storage qualifier. These act
// as parameters that the user of a shader program can pass to that program. Their values are stored
// in a program object. Uniforms are so named because they do not change from one shader invocation
// to the next within a particular rendering call thus their value is uniform among all invocations.
uniform mat4 u_Matrix;
uniform float u_Time;

// attribute define variable only use in vertex shader.
attribute vec3 a_Position;
attribute vec3 a_Color;
attribute vec3 a_DirectionVector;
attribute float a_ParticleStartTime;

// varying define variable pass to fragment shader from vertex shader.
varying vec3 v_Color;
varying float v_ElapsedTime;

void main() {
    v_Color = a_Color;
    v_ElapsedTime = u_Time - a_ParticleStartTime;
    vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);
    gl_Position = u_Matrix * vec4(currentPosition, 1.0);
    gl_PointSize = 10.0;
}