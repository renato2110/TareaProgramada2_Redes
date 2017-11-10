package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Menu {
    private Controller router;

    public void showMenu() {
        try {
            Scanner sc = new Scanner(System.in);
            String option;
            String subnet;
            String path;
            boolean contExecution = true;
            boolean isRunningRouter = false;
            while (contExecution) {
                System.out.println("Select an option:");
                System.out.println("1. Start router");
                System.out.println("2. Stop router");
                System.out.println("3. Add subnet");
                System.out.println("4. Show routes");
                System.out.println("5. Exit");
                System.out.print("Option: ");
                System.out.flush();
                option = sc.nextLine();
                switch (option) {
                    case "1":
                        System.out.println("Starting router");
                        if(!isRunningRouter){
                            System.out.printf("Insert the file name: ");
                            path = sc.nextLine();
                            this.router = new Controller(path);
                            isRunningRouter = true;
                        }else{
                            System.out.println("The router is running");
                        }
                        break;
                    case "2":
                        System.out.println("Stopping router");
                        if(isRunningRouter){
                            this.router.stopClients();
                            this.router.stopServers();
                        }else {
                            System.out.println("The router is not running");
                        }
                        break;
                    case "3":
                        System.out.flush();
                        System.out.print("Enter the subnet you want to add: ");
                        subnet = sc.nextLine();
                        if(isRunningRouter){
                            this.router.addSubnet(subnet);
                        }else {
                            System.out.println("The router is not running");
                        }
                        break;
                    case "4":;
                        System.out.println("Showing the routes:");
                        if(isRunningRouter){
                            this.router.showRoutes();
                        }else {
                            System.out.println("The router is not running");
                        }
                        break;
                    case "5":
                        contExecution = false;
                        System.out.println("Exit router.");
                        break;
                    default:
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
