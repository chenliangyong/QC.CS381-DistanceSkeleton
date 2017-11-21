import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DistanceSkeleton {
   private int numRows;
   private int numCols;
   private int minVal;
   private int maxVal;
   private int newMinVal;
   private int newMaxVal;
   private int[][] zeroFramedAry; 
   private int[][] distanceAry;
   private int[][] skeletonAry;
   private String inFile;
   private String outFile1;
   private String outFile2;
   private String outFile3;
   
   private int findNeighborMin(int i, int j, int pass) {
      int min1 = 0, min2 = 0;
      if (pass == 1) {
         if (distanceAry[i - 1][j - 1] == 0 || distanceAry[i - 1][j] == 0 || distanceAry[i - 1][j + 1] == 0
               || distanceAry[i][j - 1] == 0)
            min1 = 1;
         else {
            min1 = (distanceAry[i - 1][j - 1] <= distanceAry[i - 1][j]) ? distanceAry[i - 1][j - 1]
                  : distanceAry[i - 1][j - 1];
            min2 = (distanceAry[i - 1][j + 1] <= distanceAry[i][j - 1]) ? distanceAry[i - 1][j + 1]
                  : distanceAry[i][j - 1];
            min1 = (min1 <= min2) ? min1 : min2;
            min1 = min1 + 1;
         }
      } else {
         if (distanceAry[i][j + 1] == 0 || distanceAry[i + 1][j - 1] == 0 || distanceAry[i + 1][j] == 0
               || distanceAry[i + 1][j + 1] == 0)
            min1 = 1;
         else {
            min1 = (distanceAry[i][j + 1] <= distanceAry[i + 1][j - 1]) ? distanceAry[i][j + 1]
                  : distanceAry[i + 1][j - 1];
            min2 = (distanceAry[i + 1][j] <= distanceAry[i + 1][j + 1]) ? distanceAry[i + 1][j]
                  : distanceAry[i + 1][j + 1];
            min1 = (min1 <= min2) ? min1 : min2;
            min1 = min1 + 1;
            min1 = (min1 < distanceAry[i][j]) ? min1 : distanceAry[i][j];
         }
      }
      return min1;
   }
   
   public DistanceSkeleton(String[] args) throws IOException {
      // initialize file name;
      inFile = args[0];
      outFile1 = args[1];
      outFile2 = args[2];
      outFile3 = args[3];
      msg("Input image file name is : " + inFile);
      msg("Output file name is : " + outFile1 + "/" + outFile2 + "/" + outFile3);
      msg("Start loading image...");
      // output file initialize
      FileWriter fw = new FileWriter(outFile3);
      fw.close();
      
      
      
      // load the header,initialize Variables 
      Scanner sc;
      sc = new Scanner(new FileInputStream(inFile));
      if (!sc.hasNext()) {
         System.out.println("Error, Empty input file.");
         sc.close();
         return;
      } else {
         numRows = sc.nextInt();
         numCols = sc.nextInt();
         minVal = sc.nextInt();
         maxVal = sc.nextInt();
         newMinVal = minVal;
         newMaxVal = maxVal;
      }
      sc.close();

      // initialize Zero Framed Array ;
      zeroFramedAry = new int[numRows + 2][numCols + 2];
      distanceAry = new int[numRows + 2][numCols + 2];
      skeletonAry = new int[numRows + 2][numCols + 2];
      for (int i = 0; i < numRows + 2; i++) {
         for (int j = 0; j < numCols + 2; j++) {
            zeroFramedAry[i][j] = 0;
            distanceAry[i][j] = 0;
            skeletonAry[i][j] = 0;
         }
      }
      

      
      loadImage();
   }
   
   private void loadImage() throws FileNotFoundException {
      // Read the original image
      Scanner sc;
      sc = new Scanner(new FileInputStream(inFile));
      if (!sc.hasNext()) {
         System.out.println("Error, Empty input file.");
         sc.close();
         return;
      } else {
         String temp = sc.nextLine();
         msg(temp);
         int gVal = 0;
         for (int i = 1; i < numRows + 1; i++) {
            for (int j = 1; j < numCols + 1; j++) {
               if (sc.hasNext()) {
                  gVal = sc.nextInt();
               } else {
                  break;
               }
               zeroFramedAry[i][j] = gVal;
            }
         }
      }
      sc.close();
      msg("Finish Reading Original Image...");
   }
   
   private void fistPassDistance() throws IOException {
      for (int i = 1; i < numRows + 1; i++) {
         for (int j = 1; j < numCols + 1; j++) {
            if (zeroFramedAry[i][j] > 0) {
               distanceAry[i][j] = findNeighborMin(i, j, 1);
            }
         }
      }
      prettyPrintDistance(1);
   }

   private void secondPassDistance() throws IOException {
      for (int i = numRows + 1; i >= 0; i--) {
         for (int j = numCols + 1; j >= 0; j--) {
            if (zeroFramedAry[i][j] > 0) {
               distanceAry[i][j] = findNeighborMin(i, j, 2);
               newMinVal=(newMinVal<distanceAry[i][j])?newMinVal:distanceAry[i][j];
               newMaxVal=(newMaxVal>distanceAry[i][j])?newMaxVal:distanceAry[i][j];
            }
         }
      }
      prettyPrintDistance(2);
      printDistance();
   }
   
   public void computeDistance() throws IOException {
      fistPassDistance();
      secondPassDistance();
   }
   public void computeSkelton() throws IOException{
      for (int i = 1; i < numRows + 1; i++) {
         for (int j = 1; j < numCols + 1; j++) {
            if (zeroFramedAry[i][j] > 0) {
               if (distanceAry[i][j]>=distanceAry[i-1][j] && distanceAry[i][j]>=distanceAry[i+1][j] && distanceAry[i][j]>=distanceAry[i][j-1] && distanceAry[i][j]>=distanceAry[i][j+1] ) skeletonAry[i][j]=distanceAry[i][j];
               else skeletonAry[i][j]=0;
            }
         }
      }
      prettyPrintSkeleton();
      printSkeleton();
   }

   private void printDistance() throws IOException {
      FileWriter fw = new FileWriter(outFile1);
      fw.write(numRows + " " + numCols + " " + newMinVal + " " + newMaxVal + "\r\n");
      for (int i = 1; i < numRows + 1; i++) {
         for (int j = 1; j < numCols + 1; j++) {
            fw.write(distanceAry[i][j] + " ");
         }
         fw.write("\r\n");
       }
      fw.close();
      msg("Finished compute distance, result output at file "+outFile1);
   }
   
   private void printSkeleton() throws IOException {
      FileWriter fw = new FileWriter(outFile2);
      fw.write(numRows + " " + numCols + " " + 0 + " " + 1 + "\r\n");
      for (int i = 1; i < numRows + 1; i++) {
         for (int j = 1; j < numCols + 1; j++) {
            fw.write(skeletonAry[i][j] + " ");
         }
         fw.write("\r\n");
       }
      fw.close();
      msg("Finished compute skeleton, result output at file "+outFile2);
   }
   
   private void prettyPrintDistance(int id) throws IOException {
      FileWriter fw = new FileWriter(outFile3, true);
      fw.write("Pretty Print Distace of file '" + inFile + "' at Pass " + id + "\r\n");
      msg("Pretty Print Distace of file '" + inFile + "' at Pass " + id);
      for (int i = 1; i < numRows + 1; i++) {
         for (int j = 1; j < numCols + 1; j++) {
            if (distanceAry[i][j] > 0) {
               if (distanceAry[i][j] < 10) {
                  System.out.print(distanceAry[i][j] + " ");
                  fw.write(distanceAry[i][j] + " ");
               } else {
                  System.out.print(distanceAry[i][j]);
                  fw.write(distanceAry[i][j] + "");
               }
            } else {
               System.out.print("  ");
               fw.write("  ");
            }

         }
         System.out.println("");
         fw.write("\r\n");
      }
      fw.close();
   }

   private void prettyPrintSkeleton() throws IOException{
      FileWriter fw = new FileWriter(outFile3, true);
      fw.write("Pretty Print Skeleton of file '" + inFile + "'\r\n");
      msg("Pretty Print Skeleton of file '" + inFile +"'");
      for (int x = 1; x < numRows + 1; x++) {
         for (int y = 1; y < numCols + 1; y++) {
            if (skeletonAry[x][y] == 0){
               System.out.print("  ");
               fw.write("  ");
            }
            else{
               if (skeletonAry[x][y]<10){
                  System.out.print(skeletonAry[x][y] + " ");
                  fw.write(skeletonAry[x][y] + " ");
               }
               else{
                  System.out.print(skeletonAry[x][y]);
                  fw.write(skeletonAry[x][y] + "");
               }
            }
         }
         System.out.println("");
         fw.write("\r\n");
      }
      fw.close();
   }
   
   public static void msg(String m) {
      System.out.println("[" + m + "]");
   }
 
   public static void main(String[] args) {
      try {
         if (args.length != 4) {
            System.out
                  .println("Run as : java ConnectedComponent [inputFile1] [outputFile1] [outputFile2] [outputFile3]");
            return;
         }
         DistanceSkeleton DS = new DistanceSkeleton(args);
         DS.computeDistance();
         DS.computeSkelton();

      } catch (IOException e) {
         e.getMessage();
         System.out.println(e.getMessage() + "     ~~~ Try again ~~~");
      }
   }

}
