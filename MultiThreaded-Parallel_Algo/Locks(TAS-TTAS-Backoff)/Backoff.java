import java.util.concurrent.atomic.AtomicBoolean;

class Backoff {
    private AtomicBoolean state = new AtomicBoolean(false);
    private int backoffTime = 1; // Initial backoff time

    public void lock() {
        while (true) {
            while (state.get()) {
                // Busy wait while the state is true
            }
            if (!state.getAndSet(true)) {
                return; // Acquired the lock
            } else {
                try {
                    // Backoff and sleep for backoffTime milliseconds
                    Thread.sleep(backoffTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // Double the backoff time for the next iteration
                backoffTime *= 2;
            }
        }
    }

    public void unlock() {
        state.set(false);
        backoffTime = 1; // Reset backoff time when releasing the lock
    }
}
