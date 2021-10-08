package com.versa.particles.objects;

public class Geometry {

    public static class Point {
        private float x;
        private float y;
        private float z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }
    }

    public static class Vector {
        private float x;
        private float y;
        private float z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }
    }
}
