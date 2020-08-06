package cn.deepbigdata.handle;

import cn.deepbigdata.entity.Node;
import cn.deepbigdata.entity.OperationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CommandHandler {

    public static boolean handle(String command, List<List<Node>> shipmentTree) {
        if ("exit".equals(command)) {
            return false;
        } else {
            if (null != command) {
                if (command.startsWith("root")) {
                    handleRoot(command, shipmentTree);
                } else if (command.startsWith("split")) {
                    handleSplit(command, shipmentTree);
                } else if (command.startsWith("merge")) {
                    handleMerge(command, shipmentTree);
                }
            }
        }
        return true;
    }

    private static void handleRoot(String command, List<List<Node>> shipmentTree) {
        String[] s = command.split(" ");
        if (s.length > 2) {
            System.out.println("Too many params! Only set one root quantity!");
        } else if (s.length < 2){
            System.out.println("Have you type in quantity yet?");
        } else {
            if (isNumber(s[1])) {
                List<Node> parents = new ArrayList<>();
                Node root = new Node(parents, OperationType.ROOT, new BigDecimal(s[1]), 0, 0);
                if (shipmentTree.isEmpty()) {
                    List<Node> row = new ArrayList<>();
                    row.add(root);
                    shipmentTree.add(row);
                } else {
                    root = shipmentTree.get(0).get(0);
                    root.setValue(new BigDecimal(s[1]));
                }
            } else {
                System.out.println("Quantity shuld be a number!");
            }
        }
    }

    private static void handleSplit(String command, List<List<Node>> shipmentTree) {
        String[] s = command.split(" ");
        if (s.length > 3) {
            System.out.println("Too many params! One time only one shipment can be split!");
        } else if (s.length < 3){
            System.out.println("Lack of necessary conditions!\n Example split 0,0 20,30,40");
        } else {
            Node parent = getNodeByIndex(s[1], shipmentTree);
            if (null == parent) return;
            List<Node> children = parent.getChildren();
            if ((null == children || children.isEmpty())) {
                BigDecimal parentValue = parent.getValue();
                String[] quantities = s[2].split(",");
                BigDecimal sum = new BigDecimal("0");
                for (String quantity : quantities) {
                    if (!isNumber(quantity)) {
                        System.out.println("Quantity should be a number!");
                        return;
                    }
                    sum = sum.add(new BigDecimal(quantity));
                }
                if (parentValue.compareTo(sum) != 0) {
                    System.out.println("Sum of all child shipment quantities should be equal to parent shipment quantity!");
                    return;
                }
                int level = parent.getLevel() + 1;
                List<Node> parentList = new ArrayList<>();
                parentList.add(parent);
                int x = parent.getxIndex();
                List<Node> row = new LinkedList<>();
                if (level < shipmentTree.size()) {
                    row = shipmentTree.get(level);
                    int xIndex = x;
                    for (int i = 0; i < row.size(); i++) {
                        Node node = row.get(i);
                        int parentX = node.getParents().get(0).getxIndex();
                        if (x < parentX) {
                            xIndex = i;
                            break;
                        }
                    }
                    if (xIndex == x) {
                        x = row.size();
                    }
                } else {
                    row = new LinkedList<>();
                    shipmentTree.add(level, row);
                }
                children = new ArrayList<>();
                for (String quantity : quantities) {
                    BigDecimal value = new BigDecimal(quantity);
                    Node item = new Node(parentList,OperationType.SPLIT,value,x,level);
                    row.add(x, item);
                    x++;
                    children.add(item);
                }
                parent.setChildren(children);
            } else {
                System.out.println("One node can not be operated twice!");
            }
        }
    }

    private static void handleMerge(String command, List<List<Node>> shipmentTree) {
        String[] s = command.split(" ");
        if (s.length < 3){
            System.out.println("Lack of necessary conditions!\n Example merge 1,1 1,2 ...");
        } else {
            List<Node> parents = new ArrayList<>();
            int level = 0;
            int xIndex = 0;
            for (int i = 1; i < s.length; i++) {
                String s1 = s[i];
                Node parent = getNodeByIndex(s[i], shipmentTree);
                if (parent == null) return;
                if (level < parent.getLevel()) {
                    level = parent.getLevel();
                }
                if (xIndex < parent.getxIndex()) {
                    xIndex = parent.getxIndex();
                }
                List<Node> children = parent.getChildren();
                if (null != children && !children.isEmpty()) {
                    System.out.println("One node can not be operated twice!");
                    return;
                }
                parents.add(parent);
            }
            level = level + 1;
            List<Node> row = new LinkedList<>();
            if (level < shipmentTree.size()) {
                row = shipmentTree.get(level);
                int x = xIndex;
                for (int i = 0; i < row.size(); i++) {
                    Node node = row.get(i);
                    int parentX = node.getParents().get(0).getxIndex();
                    if (xIndex < parentX) {
                        x = i;
                        break;
                    }
                }
                if (x == xIndex) {
                    xIndex = row.size();
                }
            } else {
                row = new LinkedList<>();
                shipmentTree.add(level, row);
                xIndex = 0;
            }
            Node node = new Node(parents, OperationType.MERGE, new BigDecimal("0"), xIndex, level);
            row.add(xIndex, node);
            List<Node> child = new ArrayList<>();
            child.add(node);
            for (Node parent : parents) {
                parent.setChildren(child);
            }
        }
    }

    private static boolean isNumber(String str) {
        String reg = "\\d+(\\.\\d+)?";
        return str.matches(reg);
    }

    private static int[] getIndex(String str) {
        String[] xAy = str.split(",");
        if (xAy.length != 2) {
            System.out.println("Warning! The index include level and location.");
            return null;
        }
        for (String s : xAy) {
            if(!s.matches("\\d+")) {
                System.out.println("level or location should be int type!");
                return null;
            }
        }
        int[] index = new int[2];
        index[0] = Integer.valueOf(xAy[0]);
        index[1] = Integer.valueOf(xAy[1]);
        return index;
    }

    private static Node getNodeByIndex(String str, List<List<Node>> shipmentTree) {
        int[] index = getIndex(str);
        if (index != null) {
            int level = index[0];
            if (level < shipmentTree.size()) {
                List<Node> row = shipmentTree.get(level);
                int location = index[1];
                if (location < row.size()) {
                    return row.get(location);
                }
            }
        }
        System.out.printf("Can not find the node!");
        return null;
    }

}
