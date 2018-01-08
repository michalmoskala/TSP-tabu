public class Timer implements Runnable {
    private int limit;

    Timer(int limit){

        this.limit=limit;

    }

    public void run(){
        while (true) {
            try {
                Thread.sleep(limit);
            } catch (Exception e) {
                e.printStackTrace();
            }

                Menu.getData();
        }
        }
    }


