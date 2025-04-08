import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileOperations {

    public static void writeFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
            System.out.println("Content written to file successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    public static void modifyFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(content);
            System.out.println("Content appended to file successfully.");
        } catch (IOException e) {
            System.out.println("Error modifying file: " + e.getMessage());
        }
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.delete()) {
            System.out.println("File deleted successfully.");
            return true;
        } else {
            System.out.println("Error deleting file.");
            return false;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter file path:");
        String filePath = scanner.nextLine();

        System.out.println("Choose an operation:");
        System.out.println("1. Write to file");
        System.out.println("2. Read from file");
        System.out.println("3. Modify file");
        System.out.println("4. Delete file");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Enter content to write:");
                String content = scanner.nextLine();
                writeFile(filePath, content);
                break;
            case 2:
                String fileContent = readFile(filePath);
                if (fileContent != null) {
                    System.out.println("File content: " + fileContent);
                }
                break;
            case 3:
                System.out.println("Enter content to append:");
                String appendContent = scanner.nextLine();
                modifyFile(filePath, appendContent);
                break;
            case 4:
                deleteFile(filePath);
                break;
            default:
                System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}
