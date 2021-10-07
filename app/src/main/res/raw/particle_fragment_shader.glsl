precision mediump float;
varying vec3 v_Color;
varying float v_ElapsedTime;
void main() {
    // divide by zero, does not cause the program crash, but the result is uncertain.
    gl_FragColor = (v_Color / v_ElapsedTime, 1.0);
}
