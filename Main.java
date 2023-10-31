import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Provide the path to your input file
            String filePath = "input.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split(" ");
                int processId = Integer.parseInt(numbers[0]);
                int priority = Integer.parseInt(numbers[1]);
                int runningTime = Integer.parseInt(numbers[2]);

                // Create an instance of the Process class
                Process process = new Process(processId, priority, runningTime);

                // Run the process
                while (process.getCyclesRun() < process.getRunningTime()) {
                    process.run();
                }
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }
}

class Process {
    private int processId;
    private int priority;
    private int runningTime;
    private int cyclesRun;

    public Process(int processId, int priority, int runningTime) {
        this.processId = processId;
        this.priority = priority;
        this.runningTime = runningTime;
        this.cyclesRun = 0;
    }

    public void run() {
        if (cyclesRun < runningTime) {
            cyclesRun++;
            System.out.println("Process ID: " + processId + ", Cycles Run: " + cyclesRun);
        } else {
            System.out.println("Process ID: " + processId + " has completed.");
        }
    }

    public int getCyclesRun() {
        return cyclesRun;
    }

    public int getRunningTime() {
        return runningTime;
    }
}
