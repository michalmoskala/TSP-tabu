public class Timer implements Runnable {
    int time=0;
    int limit;

    public Timer(int limit){

        this.limit=limit;

    }

    public void run(){
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            time++;
            if (time==limit)
                Menu.getData();
        }
        }
    }


