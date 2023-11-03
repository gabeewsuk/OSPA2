import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static int cycles = 0;
    public static ArrayList<Process> processHeap = new ArrayList<>();
    public static ArrayList<Process> pendingProcesses = new ArrayList<>();

    public static void main(String[] args) {
        try {
            String file = "input.txt";
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int processId = 0;

            while ((line = reader.readLine()) != null) {
                processId++;
                String[] data = line.split(" ");
                int arrivalTime = Integer.parseInt(data[0]);
                int runningTime = Integer.parseInt(data[1]);
                int priority = Integer.parseInt(data[2]);

                Process process = new Process(processId, priority, runningTime, arrivalTime);

                if (arrivalTime == 0) {
                    addToHeap(process);
                } else {
                    pendingProcesses.add(process);
                }
            }

            reader.close();

            while (!processHeap.isEmpty() || !pendingProcesses.isEmpty()) {
                for (Process pendingProcess : new ArrayList<>(pendingProcesses)) {
                    if (pendingProcess.getArrivalTime() == 0) {
                        addToHeap(pendingProcess);
                        pendingProcesses.remove(pendingProcess);
                    }
                }

                for (Process pendingProcess : new ArrayList<>(pendingProcesses)) {
                    pendingProcess.decrementArrivalTime();
                }

                if (!processHeap.isEmpty()) {
                    Process minPriorityProcess = processHeap.get(0);
                    boolean processComplete = minPriorityProcess.run();

                    if (processComplete) {
                        extractMinPriorityProcess();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void addToHeap(Process process) {
        processHeap.add(process);
        heapify();
    }

    public static Process extractMinPriorityProcess() {
        Process minProcess = processHeap.remove(0);
        heapify();
        return minProcess;
    }

    public static void heapify() {
        int size = processHeap.size();
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapifyDown(i, size);
        }
    }

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
