package mqtt;

public class Engine implements Runnable {
    private boolean wait = true;
    private boolean stop = false;

    @Override
    public void run() {
        while (!stop) {
            try {
                Thread.sleep(1000);
                if (wait) {
                    Repository.getInstance().addData("time", System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(boolean stop) {
        this.stop = stop;
    }

    public void pause(boolean wait) {
        this.wait = wait;
    }
}
