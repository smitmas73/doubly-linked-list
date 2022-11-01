import data.DoublyLinkedList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final String HEAD_INDEX_INDICATOR = "head";
    private static final String TAIL_INDEX_INDICATOR = "tail";
    private static final String REMOVE_NODE_INDICATOR = "removeNode";
    private static final String PRINT_INDICATOR = "print";
    private static final String SAVE_INDICATOR = "saveList";
    private static final String PROGRAM_TERMINATE_INDICATOR = "quit";
    private static final String INSTRUCTIONS = "Welcome to this implementation of a Doubly Linked List (DLL) as written in Java.\n\n" +
            "To add a new node, please type the node's contents\n" +
            "Followed by the desired node index separated by a space.\n" +
            "To add a head node or tail node explicitly, please type\n" +
            "the node's content followed by '" + HEAD_INDEX_INDICATOR + "' or '" + TAIL_INDEX_INDICATOR + "' respectively,\n" +
            "with a space separating the arguments.\n\n" +
            "To remove a Node from the list, type '" + REMOVE_NODE_INDICATOR + "'\n" +
            "followed by the index of the desired node to be removed or '" + HEAD_INDEX_INDICATOR + "' or '" + TAIL_INDEX_INDICATOR + "' to remove the head or tail.\n\n" +
            "To view the current contents of your DLL, type '" + PRINT_INDICATOR + "' at any time.\n\n" +
            "To save the current state of your DLL to a text file, type '" + SAVE_INDICATOR + "' and your DLL will be saved to the resources folder of the project.\n\n" +
            "To stop providing input, type '" + PROGRAM_TERMINATE_INDICATOR + "'";
    private static final String INVALID_INPUT_DETECTED = "Invalid input detected. Please only provide the content or '" + REMOVE_NODE_INDICATOR + "'\n" +
            "and the index of the desired node.";
    private static final DoublyLinkedList LIST = new DoublyLinkedList();

    public static void main(String[] args) {
        System.out.println(INSTRUCTIONS);

        while (true) {
            if (handleInput() == -1) {
                break;
            }
        }
    }

    private static int handleInput() {
        Scanner scanner = new Scanner(System.in);
        String[] command = scanner.nextLine().split(" ");

        if (!whereCommandIsTerminateProgram(command)) {
            if (command.length == 0 || command.length > 2) {
                System.out.println(INVALID_INPUT_DETECTED);
                return 0;
            }

            if (command.length == 1 && command[0].equalsIgnoreCase(SAVE_INDICATOR)) {
                try {
                    handleSaveList();
                } catch (IOException e) {
                    System.out.println("Could not save to file. Please try again. | " + e);
                }

                return 0;
            }

            if (command.length == 1 && command[0].equalsIgnoreCase(PRINT_INDICATOR)) {
                LIST.printContents();
                return 0;
            }

            if (command[0].equalsIgnoreCase(REMOVE_NODE_INDICATOR)) {
                handleRemoveNode(command);
                return 0;
            }

            if (command.length == 1) {
                LIST.addNodeAtIndex(command[0], null);
                LIST.printContents();
                return 0;
            }

            if (command[1].equalsIgnoreCase(HEAD_INDEX_INDICATOR)) {
                LIST.addEngine(command[0]);
                LIST.printContents();
                return 0;
            }

            if (command[1].equalsIgnoreCase(TAIL_INDEX_INDICATOR)) {
                LIST.addCaboose(command[0]);
                LIST.printContents();
                return 0;
            }

            try {
                Optional.of(command[1])
                        .map(Integer::parseInt)
                        .ifPresent(index -> LIST.addNodeAtIndex(command[0], index));
                LIST.printContents();
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT_DETECTED);
            }
            return 0;
        }

        System.out.print("Final DLL: ");
        LIST.printContents();
        return -1;
    }

    private static boolean whereCommandIsTerminateProgram(String[] command) {
        return command.length == 1 && command[0].equalsIgnoreCase(PROGRAM_TERMINATE_INDICATOR);
    }

    private static void handleSaveList() throws IOException {
        String text = LIST.asString();
        Files.write(Paths.get("./src/main/resources/list.txt"), text.getBytes());
        System.out.println("File saved.");
    }

    private static void handleRemoveNode(String[] command) {
        if (command.length == 1) {
            System.out.println(INVALID_INPUT_DETECTED);
            return;
        }

        String index = command[1];

        if (index.equalsIgnoreCase(HEAD_INDEX_INDICATOR)) {
            LIST.removeHeadNode();
            LIST.printContents();
            return;
        }

        if (index.equalsIgnoreCase(TAIL_INDEX_INDICATOR)) {
            LIST.removeTailNode();
            LIST.printContents();
            return;
        }

        try {
            Optional.of(index)
                    .map(Integer::parseInt)
                    .ifPresent(LIST::removeNodeAtIndex);
            LIST.printContents();
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT_DETECTED);
        }
    }
}
