import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    //defining global vars
    public static int cycles = 0;
    public static List<Process> processHeap = new ArrayList<>();
    public static List<Process> pendingProcesses = new ArrayList<>();

    public static void main(String[] args) {
        try {
            String filePath = "input.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int processId = 0;

            while ((line = reader.readLine()) != null) {
                processId++;
                String[] numbers = line.split(" ");
                int arrivalTime = Integer.parseInt(numbers[0]);
                int runningTime = Integer.parseInt(numbers[1]);
                int priority = Integer.parseInt(numbers[2]);

                Process process = new Process(processId, priority, runningTime, arrivalTime);

                if (arrivalTime == 0) {
                    // adding first arrived directly into heap
                    addToHeap(process);
                } else {
                    // adding all others to pendingProcess heap
                    pendingProcesses.add(process);
                }
            }

            reader.close();

            // Main loop to simulate processes
            while (!processHeap.isEmpty() || !pendingProcesses.isEmpty()) {
                // Adding pending processes that are ready to our heap
                for (Process pendingProcess : new ArrayList<>(pendingProcesses)) {
                    if (pendingProcess.getArrivalTime() == 0) {
                        addToHeap(pendingProcess);
                        pendingProcesses.remove(pendingProcess);
                    }
                }

                // Decrement arrival times of all pending processes so that we can add them to the que when we are ready
                for (Process pendingProcess : new ArrayList<>(pendingProcesses)) {
                    pendingProcess.decrementArrivalTime();
                }

                
                if (!processHeap.isEmpty()) {
                    //min process always sits at 0 index
                    Process minPriorityProcess = processHeap.get(0);
                    //run min process
                    boolean processComplete = minPriorityProcess.run();

                    //if it is done remove it from the heap
                    if (processComplete) {
                        extractMinPriorityProcess();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("err" + e.getMessage());
        }
    }

    //adding processes to heap and sorting 
    public static void addToHeap(Process process) {
        processHeap.add(process);
        heapify();
    }

    //pulling min value and removing it from the heap then resorting
    public static Process extractMinPriorityProcess() {
        Process minProcess = processHeap.remove(0);
        heapify();
        return minProcess;
    }

    //sorting
    public static void heapify() {
        int size = processHeap.size();
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapifyDown(i, size);
        }
    }

    //sorting heap
    public static void heapifyDown(int index, int size) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int smallest = index;

        if (left < size && processHeap.get(left).getPriority() < processHeap.get(smallest).getPriority()) {
            smallest = left;
        }

        if (right < size && processHeap.get(right).getPriority() < processHeap.get(smallest).getPriority()) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest, size);
        }
    }

    public static void swap(int i, int j) {
        Process temp = processHeap.get(i);
        processHeap.set(i, processHeap.get(j));
        processHeap.set(j, temp);
    }


    public static class Process {
        private int processId;
        private int priority;
        private int runningTime;
        private int cyclesRun;
        private int arrivalTime;

        public Process(int processId, int priority, int runningTime, int arrivalTime) {
            this.processId = processId;
            this.priority = priority;
            this.runningTime = runningTime;
            this.cyclesRun = 0;
            this.arrivalTime = arrivalTime;
        }

        public int getPriority() {
            return priority;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public void decrementArrivalTime() {
            arrivalTime--;
        }

        public boolean run() {
            //incriment global
            cycles++;
            cyclesRun++;
            System.out.println("On cycle: " + cycles + " the process " + processId + " ran for " + cyclesRun + " time(s)" + " priority " + priority);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (cyclesRun == runningTime) {
                System.out.println("Process ID: " + processId + " has completed.");
                return true;
            }
            return false;
        }
    }
}
