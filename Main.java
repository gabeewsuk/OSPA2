import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static int cycles = 0;

    public static void main(String[] args) {
        try {
            String filePath = "input.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            Process[] processes = new Process[100];
            int heapSize = 0;
            int processId = 0;

            while ((line = reader.readLine()) != null) {
                processId++;
                String[] numbers = line.split(" ");
                int arrivalTime = Integer.parseInt(numbers[0]);
                int runningTime = Integer.parseInt(numbers[1]);
                int priority = Integer.parseInt(numbers[2]);

                Process process = new Process(processId, priority, runningTime, arrivalTime);

                processes[heapSize] = process;
                heapSize++;
                if(cycles == 0){
                heapifyUp(processes, heapSize - 1);}
                else{
                    while(cycles>=arrivalTime){
                    Process minProcess = extractMin(processes, heapSize);
                    minProcess.run();}
                }
            }

            reader.close();



            // while (heapSize > 0) {
            //     Process minProcess = extractMin(processes, heapSize);
            //     heapSize--;
            //     minProcess.run();
            // }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    private static void heapifyUp(Process[] processes, int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (processes[index].compareTo(processes[parentIndex]) < 0) {
                swap(processes, index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private static Process extractMin(Process[] processes, int size) {
        if (size <= 0) {
            return null;
        }

        Process min = processes[0];
        processes[0] = processes[size - 1];
        heapifyDown(processes, size - 1, 0);
        return min;
    }

    private static void heapifyDown(Process[] processes, int size, int index) {
        int smallest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        if (left < size && processes[left].compareTo(processes[smallest]) < 0) {
            smallest = left;
        }

        if (right < size && processes[right].compareTo(processes[smallest]) < 0) {
            smallest = right;
        }

        if (smallest != index) {
            swap(processes, index, smallest);
            heapifyDown(processes, size, smallest);
        }
    }

    private static void swap(Process[] processes, int i, int j) {
        Process temp = processes[i];
        processes[i] = processes[j];
        processes[j] = temp;
    }
}

class Process implements Comparable<Process> {
    private int processId;
    private int priority;
    private int runningTime;
    private int cyclesRun;
    private int arrivalTime;

    public Process(int processId, int priority, int runningTime, int arrivalTime) {
        this.processId = processId;
        this.priority = priority;
        this.runningTime = runningTime;
        this.arrivalTime = arrivalTime;
        this.cyclesRun = 0;
    }

    public void run() {
        Main.cycles++;
        if (cyclesRun < runningTime) {
            cyclesRun++;
            System.out.println("On cycle: " + Main.cycles + " the process " + processId + " ran for " + cyclesRun + " time(s)" + " priority " + priority);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Process ID: " + processId + " has completed.");
        }
    }

    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.priority, other.priority);
    }
}
