package cn.deepbigdata.menu;

public class Menu {

    public static void printBanner() {
        System.out.println("This is a Shipment Manager Application.\n");
    }

    public static void helpInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Example usage:\n");
        sb.append("root [QUANTITY]\n");
        sb.append("split [X,Y] [QUANTITY,QUANTITY,...]\n");
        sb.append("merge [X,Y X,Y ...]\n");
        sb.append("exit");
        System.out.println(sb.toString());
    }

    public static void start() {
        printBanner();
        helpInfo();
    }
}
