import java.io.*;

public class Main {

    static public int DP(int p, int c, int costMatrix[][], int [] demandsByYear) {

        int totalDemand = 0;
        for (int i = 0; i < demandsByYear.length; i++) {
            totalDemand += demandsByYear[i];
        }

        for (int i = 1; i < costMatrix.length; i++) {
            totalDemand -= demandsByYear[i-1];
            for (int j = 0; j < costMatrix[0].length; j++){
                if (totalDemand == 0) {
                    totalDemand +=1;
                }
                if (j >= totalDemand) {
                    break;
                }
                if (demandsByYear[i-1] <= p) {
                    int length = p - demandsByYear[i-1] + 1 + j;
                    int minValue = Integer.MAX_VALUE;

                    for (int k = 0; k < length; k++) {
                        int diff = demandsByYear[i-1] - p + k;
                        int secondIndex = 0;
                        if (diff - k + j > 0) {
                            secondIndex = diff - k + j;
                        }
                        if (diff - k + j >= costMatrix[0].length) {
                            break;
                        }

                        int multiplier = k;
                        if (diff < 0) {
                            multiplier = 0;
                        }
                        int cost = costMatrix[i-1][secondIndex] + (multiplier * c) + costMatrix[0][j];
                        if (cost < minValue) {
                            minValue = cost;
                        }
                    }
                    costMatrix[i][j] = minValue;
                } else {

                    int length = demandsByYear[i - 1] - p + 1 + j;
                    int minValue = Integer.MAX_VALUE;

                    for (int k = 0; k < length; k++) {
                        int diff = demandsByYear[i - 1] - p;
                        int secondIndex = 0;
                        if (diff - k + j > 0) {
                            secondIndex = diff - k + j;
                        }
                        if (diff - k + j >= costMatrix[0].length) {
                            break;
                        }

                        int cost = costMatrix[i-1][secondIndex] + (k * c) + costMatrix[0][j];

                        if (cost < minValue) {
                            minValue = cost;
                        }
                    }
                    costMatrix[i][j] = minValue;
                }
            }
        }

        return costMatrix[costMatrix.length - 1][0];
    }

    static void displayScreen(int costMatrix[][]) {
        for (int i = 1; i < costMatrix.length; i++) {
            int currentYearMinCost = costMatrix[i][0];
            System.out.println(i + ". year current minimum total cost : " + currentYearMinCost);
        }
    }

    static void fillFirstRowOfCostMatrix(int costMatrix[][], int playersSalary[][]) {
        costMatrix[0][0] = 0;
        for (int i = 1; i < costMatrix[0].length; i++) {
            costMatrix[0][i] = playersSalary[i-1][1];
        }
    }

    static public void readFromFileAndFillArray(String path, int array[][]) throws IOException {
        FileReader in = new FileReader(path);
        BufferedReader br = new BufferedReader(in);

        int index = -1;
        String st;
        while ((st = br.readLine()) != null) {
            if (index == -1) {
                index++;
                continue;
            }
            String [] splittedArray = st.split("\t");
            int firstElement = Integer.parseInt(splittedArray[0]);
            int secondElement = Integer.parseInt(splittedArray[1]);

            array[index][0] = firstElement;
            array[index][1] = secondElement;
            index++;
        }
        in.close();
    }

    public static void main(String[] args) throws Exception {
        int n=3, p=5, c=5;
        int playersSalary[][] = new int [310][2];
        int yearlyPlayerDemands[][] = new int [50][2];

        String playersSalaryFilePath = "C:\\Users\\Mert\\IdeaProjects\\CME2204-2\\players_salary.txt";
        String yearlyPlayerDemandFilePath = "C:\\Users\\Mert\\IdeaProjects\\CME2204-2\\yearly_player_demand.txt";
        readFromFileAndFillArray(playersSalaryFilePath, playersSalary);
        readFromFileAndFillArray(yearlyPlayerDemandFilePath, yearlyPlayerDemands);

        int matrixRow = n + 1;
        int matrixColumn = 0;

        int [] demandsByYear = new int [n];

        for (int i = 0; i < n ; i ++) {
            matrixColumn += yearlyPlayerDemands[i][1];
            demandsByYear[i] = yearlyPlayerDemands[i][1];
        }

        int costMatrix[][] = new int [matrixRow][matrixColumn];

        fillFirstRowOfCostMatrix(costMatrix, playersSalary);

        int cost = DP(p,c,costMatrix, demandsByYear);

        displayScreen(costMatrix);
        System.out.println("DP Results : " + cost);

    }
}