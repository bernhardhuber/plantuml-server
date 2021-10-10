/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

/**
 *
 * @author berni3
 */
public class GenericWrappers {

    public static class Triple<U, V, W> {

        private final U u;
        private final V v;
        private W w;

        Triple(U uValue, V vValue, W wValue) {
            this.u = uValue;
            this.v = vValue;
            this.w = wValue;
        }

        public U getU() {
            return this.u;
        }

        public V getV() {
            return this.v;
        }

        public W getW() {
            return this.w;
        }

        void setW(W wValue) {
            this.w = wValue;
        }
    }

    public static class Tuple<U, V> {

        private final U u;
        private V v;

        Tuple(U uValue, V vValue) {
            this.u = uValue;
            this.v = vValue;
        }

        public U getU() {
            return this.u;
        }

        public V getV() {
            return this.v;
        }

        public void setV(V vValue) {
            this.v = vValue;
        }
    }

}
