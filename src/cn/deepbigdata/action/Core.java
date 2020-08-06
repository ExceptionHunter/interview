package cn.deepbigdata.action;

import cn.deepbigdata.entity.Node;
import cn.deepbigdata.entity.OperationType;
import cn.deepbigdata.handle.CommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Core {
    private static List<List<Node>> shipmentTree = new ArrayList<>();
    public static void  run() {
        boolean flag = true;
        Scanner scanner = new Scanner(System.in);
        while (flag) {
            if (shipmentTree.isEmpty()) {
                System.out.println("You should use 'root' command to set a root quantity first.");
            } else {
                show();
            }
            System.out.print("->command:");
            String command = scanner.nextLine();
            flag = CommandHandler.handle(command, shipmentTree);
        }
    }

    private static void show() {
        for (List<Node> nodes : shipmentTree) {
            for (Node node : nodes) {
                StringBuilder sb = new StringBuilder();
                List<Node> children = node.getChildren();
                sb.append("  ");
                if (OperationType.MERGE == node.getOperationType()) {
                    sb.append("  ");
                }
                if (null != children) {
                    for (Node child : children) {
                        sb.append("  ");
                    }
                }
                System.out.print(sb.toString() + node.getValue() + sb.toString());
            }
            System.out.println();
        }
    }
}
