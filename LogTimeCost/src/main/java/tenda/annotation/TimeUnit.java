package tenda.annotation;

public enum TimeUnit {
    mills {
        public long now() {
            return System.currentTimeMillis();
        }
    },nano {
        public long now() {
            return System.nanoTime();
        }
    };
    public abstract long now();
}
