import java.util.concurrent.atomic.AtomicBoolean;

class TASLock {
    private AtomicBoolean state = new AtomicBoolean(false);

    public void lock() {
        while (state.getAndSet(true)) {
           
        }
    }

    public void unlock() {
        state.set(false);
    }
}

